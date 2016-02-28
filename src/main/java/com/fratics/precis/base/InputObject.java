package com.fratics.precis.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;

import com.fratics.precis.base.Schema.SchemaElement;
import com.fratics.precis.candidategeneration.BaseCandidateElement;
import com.fratics.precis.dimval.DimValIndex;
import com.fratics.precis.fis.feed.BaseFeedPartitioner;

public abstract class InputObject implements Serializable {

    private static final long serialVersionUID = -6326248205037370805L;
    protected int noOfFields = 0;
    private long noOfValues = 0;
    protected FieldObject[] fieldObjects = null;
    protected boolean countPrecis = true;
    protected BaseFeedPartitioner partitioner = null;
    protected int metricIndex;
    protected double threshold;
    public HashMap<BitSet, ArrayList<BaseCandidateElement>> currCandidatePart = new HashMap<BitSet, ArrayList<BaseCandidateElement>>();
    public HashMap<BitSet, ArrayList<BaseCandidateElement>> prevCandidatePart = new HashMap<BitSet, ArrayList<BaseCandidateElement>>();
    public HashSet<BitSet> currCandidateSet = new HashSet<BitSet>();
    public HashSet<BitSet> prevCandidateSet = new HashSet<BitSet>();
    public HashMap<BitSet, BaseCandidateElement> firstStageCandidates = new HashMap<BitSet,BaseCandidateElement>();

    public int currentStage = -1;

    public double getThreshold() {
	return threshold;
    }

    public void setThreshold(double threshold) {
	this.threshold = threshold;
    }

    public void addCandidate(BitSet b) {
	int dimSetBit = b.previousSetBit(DimValIndex.dimMap.size() - 1);
	int valSetBit = b.previousSetBit(DimValIndex.dimMap.size()
		+ DimValIndex.dimValMap.size() - 1);
	BitSet tmp = (BitSet) b.clone();
	tmp.clear(dimSetBit);
	tmp.clear(valSetBit);
	// System.err.println("Dim Set Map Size :: " +
	// (DimValIndex.dimMap.size() - 1) + " Value Set Map Size ::" +
	// (DimValIndex.dimMap.size() + DimValIndex.dimValMap.size() - 1));
	// System.err.println("Dim Bit Set to Clear :: " + dimSetBit +
	// " Value Set Bit to Clear ::" + valSetBit);
	// System.err.println("Original Set" + b + " Candidate ::" + tmp);
	if (currCandidatePart.containsKey(tmp)) {
	    currCandidatePart.get(tmp).add(new BaseCandidateElement(b, 0.0));
	} else {
	    ArrayList<BaseCandidateElement> al = new ArrayList<BaseCandidateElement>();
	    al.add(new BaseCandidateElement(b, 0.0));
	    currCandidatePart.put(tmp, al);
	}
	currCandidateSet.add(b);
    }

    public void addFirstStageCandidateElement(BaseCandidateElement b) {
	if(this.firstStageCandidates.containsKey(b.getBitSet())){
	    this.firstStageCandidates.get(b.getBitSet()).incrMetricBy(b.getMetric());
	}else{
	    this.firstStageCandidates.put(b.getBitSet(), b);
	}
    }

    public void moveToNextStage() {
	prevCandidatePart = currCandidatePart;
	currCandidatePart = new HashMap<BitSet, ArrayList<BaseCandidateElement>>();
	prevCandidateSet = currCandidateSet;
	currCandidateSet = new HashSet<BitSet>();
    }

    public void applyThreshold() {
	for (ArrayList<BaseCandidateElement> al : this.currCandidatePart
		.values()) {
	    ArrayList<BaseCandidateElement> removeList = new ArrayList<BaseCandidateElement>();
	    for (BaseCandidateElement bce : al) {
		if (bce.getMetric() < this.threshold)
		    removeList.add(bce);
	    }
	    for (BaseCandidateElement bce : removeList) {
		al.remove(bce);
		this.currCandidateSet.remove(bce.getBitSet());
	    }
	}
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

    public void setNoOfValues(long noOfValues) {
	this.noOfValues = noOfValues;
    }

    public long getNoOfValues() {
	return this.noOfValues;
    }

    public void incNoOfValues(long inc) {
	noOfValues = noOfValues + inc;
    }

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

    public abstract void loadInput(Object o) throws Exception;

    protected abstract boolean isInitialized();

    public abstract String toString();
}
