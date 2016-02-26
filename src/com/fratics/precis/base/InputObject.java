package com.fratics.precis.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;

import com.fratics.precis.base.Schema.SchemaElement;
import com.fratics.precis.candidategeneration.BaseCandidateElement;
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
    protected HashMap<BitSet,BaseCandidateElement> currCandidateMap = new HashMap<BitSet,BaseCandidateElement>();
    protected HashMap<BitSet,BaseCandidateElement> prevCandidateMap = new HashMap<BitSet,BaseCandidateElement>();
    public int currentStage = -1;
    
    
    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public HashMap<BitSet,BaseCandidateElement> getCurrentCandidateMap(){
	return currCandidateMap;
    }
    
    public HashMap<BitSet,BaseCandidateElement> getPrevCandidateMap(){
	return prevCandidateMap;
    }
    
    public void addNextCandidateElement(BaseCandidateElement b){
	if(currCandidateMap.containsKey(b.getBitSet())){
	    if(currCandidateMap.get(b.getBitSet()).isPassedThreshold()) return;
	    currCandidateMap.get(b.getBitSet()).incrMetric(b.getMetric());
	    if(currCandidateMap.get(b.getBitSet()).getMetric() >= this.threshold){
		currCandidateMap.get(b.getBitSet()).setPassedThreshold(true);
	    }
	}else{
	    currCandidateMap.put(b.getBitSet(), b);
	}
    }
    
    public void moveToNextStage(){
	prevCandidateMap = currCandidateMap;
	Iterator<BaseCandidateElement> it = prevCandidateMap.values().iterator();
	ArrayList<BitSet> al = new ArrayList<BitSet>();
	while(it.hasNext()){
	    BaseCandidateElement bce = it.next();
	    if(!bce.isPassedThreshold()) al.add(bce.getBitSet());
	}
	//remove them from candidates
	for(int i = 0; i < al.size(); i++){
	    prevCandidateMap.remove(al.get(i));
	}
	currCandidateMap = new HashMap<BitSet,BaseCandidateElement>();
    }
    
    public BaseFeedPartitioner getPartitioner() {
        return partitioner;
    }

    public void setPartitioner(BaseFeedPartitioner partitioner) {
        this.partitioner = partitioner;
    }
    
    public boolean isCountPrecis(){
	return this.countPrecis;
    }
    
    public int getMetricIndex(){
	return this.metricIndex;
    }

    public void setMetricIndex(int metricIndex){
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
	ArrayList<SchemaElement> list = sch.getSchemaList();
	for (int i = 0; i < noOfFields; i++) {
	    fieldObjects[i] = new FieldObject();
	    fieldObjects[i].setSchemaElement(list.get(i));
	}
    }

    public abstract void loadInput(Object o) throws Exception;

    protected abstract boolean isInitialized();

    public abstract String toString();
}
