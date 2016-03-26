package com.fratics.precis.fis.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;

import com.fratics.precis.fis.base.Schema.SchemaElement;
import com.fratics.precis.fis.feed.BaseFeedPartitioner;
import com.fratics.precis.fis.feed.dimval.DimValIndex;
import com.fratics.precis.fis.util.PrecisConfigProperties;

/*
 * The Input Object is the Abstract Base Class for all the Input Objects in this application.
 * The is the main holder class on which all the data is captured from the input feed.
 * 
 * The input object will be passed from one flow processor to the other in sequence of data massaging steps.
 * The Input feed's state alters the input object and gets captured as we progress in applying all our data 
 * massaging processors.
 *  
 */

public abstract class InputObject implements Serializable {

    private static final long serialVersionUID = -6326248205037370805L;

    // No of Dimensions in the Input feed.
    protected int noOfFields = 0;

    // No of Lines in the data stream (or) flat file.
    private long noOfLines = 0;

    // Field Objects represents a column in the data feed.
    protected FieldObject[] fieldObjects = null;

    // the current application is a Count Precis (or) Metric Precis.
    protected boolean countPrecis = true;

    // Partitioned Input Feed.
    protected BaseFeedPartitioner partitioner = null;

    // Column index of the metric field in the input data record.
    protected int metricIndex = -1;

    // Threshold applied for candidate generation.
    protected double threshold;

    // Current Precis Stage's Candidate Partition,
    // The Candidates as partitioned & grouped in a arraylist by a bitset
    // object.
    // The bitset object is derived from the first (n-1) dim bits set & (n-1)
    // value bits set.
    public HashMap<BitSet, ArrayList<BaseCandidateElement>> currCandidatePart = new HashMap<BitSet, ArrayList<BaseCandidateElement>>();

    // Previous Precis Stage's Candidates Partition, the current candidate
    // Partition
    // is generated from the previous candidate partition.
    public HashMap<BitSet, ArrayList<BaseCandidateElement>> prevCandidatePart = new HashMap<BitSet, ArrayList<BaseCandidateElement>>();

    // Current Precis Stage's Candidates List.
    public CandidateElementList currCandidateSet = new CandidateElementList();

    // Successful Precis Stage's Candidates List.
    public CandidateElementList succCandidateSet = new CandidateElementList();

    // Previous Precis Stage's Candidate List.
    public CandidateElementList prevCandidateSet = new CandidateElementList();

    // Precis First Stage Candidate Partition.
    public HashMap<BitSet, BaseCandidateElement> firstStageCandidates = new HashMap<BitSet, BaseCandidateElement>();

    // Tells the threads invoked that processing has infact completed & which
    // stage its completed.
    public int exitedStage = 2;
    public boolean processingCompleted = false;

    // Thread Handler for Writing
    public Thread threadHandle = null;

    // Candidates Persistant Store, used at the end to generate Candidates and
    // their metrics.
    public class CandidateElementList extends HashSet<BaseCandidateElement> {
	private static final long serialVersionUID = 1L;
	public AtomicBoolean dataLoaded = new AtomicBoolean(false);
	public boolean isPresent(BaseCandidateElement bce) {
	    return super.contains(bce);
	}
    }

    public CandidateElementList[] candidateStore = new CandidateElementList[PrecisConfigProperties.NO_OF_STAGES];

    public InputObject() {

	for (int i = 0; i < PrecisConfigProperties.NO_OF_STAGES; i++) {
	    candidateStore[i] = new CandidateElementList();
	}
    }

    public int currentStage = -1;

    public double getThreshold() {
	return threshold;
    }

    public void setThreshold(double threshold) {
	this.threshold = threshold;
    }

    public BaseFeedPartitioner getPartitioner() {
	return partitioner;
    }

    public void setPartitioner(BaseFeedPartitioner partitioner) {
	this.partitioner = partitioner;
    }

    public boolean isCountPrecis() {
	return this.countPrecis;
    }

    public int getMetricIndex() {
	return this.metricIndex;
    }

    public void setMetricIndex(int metricIndex) {
	this.metricIndex = metricIndex;
    }

    public int getNoOfFields() {
	return noOfFields;
    }

    public void setNoOfFields(int noOfFields) {
	this.noOfFields = noOfFields;
    }

    public FieldObject[] getFieldObjects() {
	return fieldObjects;
    }

    public void setNoOfLines(long noOfLines) {
	this.noOfLines = noOfLines;
    }

    public long getNoOfLines() {
	return this.noOfLines;
    }

    public void incNoOfLines(long inc) {
	noOfLines = noOfLines + inc;
    }

    /*
     * Add a candidate to the Candidate Partition and Candidate List.
     */

    public void addCandidate(BitSet b) {
	currCandidateSet.add(new BaseCandidateElement(b, 0.0));
    }

    public void addSuccessFulCandidate(BitSet b) {
	int dimSetBit = b.previousSetBit(DimValIndex.dimMap.size() - 1);
	int valSetBit = b.previousSetBit(DimValIndex.dimMap.size()
		+ DimValIndex.dimValMap.size() - 1);
	BitSet tmp = (BitSet) b.clone();
	tmp.clear(dimSetBit);
	tmp.clear(valSetBit);
	BaseCandidateElement e = new BaseCandidateElement(b, 0.0);
	if (currCandidatePart.containsKey(tmp)) {
	    currCandidatePart.get(tmp).add(e);
	} else {
	    ArrayList<BaseCandidateElement> al = new ArrayList<BaseCandidateElement>();
	    al.add(e);
	    currCandidatePart.put(tmp, al);
	}
	succCandidateSet.add(e);
    }

    /*
     * Add a Fist Stage Candidate.
     */

    public void addFirstStageCandidateElement(BaseCandidateElement b) {
	if (this.firstStageCandidates.containsKey(b.getBitSet())) {
	    this.firstStageCandidates.get(b.getBitSet()).incrMetricBy(
		    b.getMetric());
	} else {
	    this.firstStageCandidates.put(b.getBitSet(), b);
	}
    }

    /*
     * As a Precis Stage Completes, this method moves Precis Execution context
     * from the current stage to the Next Stage.
     */

    public void moveToNextStage() {
	prevCandidatePart = currCandidatePart;
	currCandidatePart = new HashMap<BitSet, ArrayList<BaseCandidateElement>>();
	// preserve the current stage.
	candidateStore[this.currentStage - 1] = succCandidateSet;
	candidateStore[this.currentStage - 1].dataLoaded.compareAndSet(false, true);
	prevCandidateSet = succCandidateSet;
	currCandidateSet = new CandidateElementList();
	succCandidateSet = new CandidateElementList();
    }

    /*
     * Applies the Schema Object, generated by the PrecisSchemaProcessor on the
     * input data feed. The schema elements are added to the respective fields
     * in the field objects.
     */

    public void loadSchema(Schema sch) {
	// need to alter loadSchema in case of Metrics.
	this.noOfFields = sch.getNoOfFields();
	fieldObjects = new FieldObject[noOfFields];
	SchemaElement[] list = sch.getSchemaList()
		.toArray(new SchemaElement[0]);
	for (int i = 0; i < noOfFields; i++) {
	    fieldObjects[i] = new FieldObject();
	    fieldObjects[i].setSchemaElement(list[i]);
	}
    }

    /*
     * The actual input data needs to loaded as per the input feed
     * characteristics. The feed characteristics, delimiters, definitions,
     * serialization, deserialization, data formats, block data, sequence data,
     * encryption formats, field info needs to be taken care in the loadInput()
     * method by the Deriving Class.
     * 
     * So loadInputCharacteristics() method is a abstract method.
     */

    public abstract void loadInputCharacteristics(Object o) throws Exception;

    /*
     * A checker method to verify data initializations.
     */
    protected abstract boolean isInitialized();

    /*
     * A simple toString() method defined to provide the current state of the
     * Precis Execution and its relevent objects contained in the input object.
     */

    public String toString() {
	return "=====================================================\n"
		+ "\nNo of Records :: " + this.getNoOfLines() + "\n\n"
		+ "isCountPrecis :: " + this.isCountPrecis() + "\n\n"
		+ "Field Objects :: " + Arrays.toString(fieldObjects) + "\n\n"
		+ "Stages Run :: " + this.currentStage + "\n\n"
		+ "DimVal Indexes :: " + DimValIndex.dumpIndexes() + "\n"
		+ "=====================================================\n";
    }
}
