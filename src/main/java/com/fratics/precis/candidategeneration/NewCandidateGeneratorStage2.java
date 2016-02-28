package com.fratics.precis.candidategeneration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;

import com.fratics.precis.base.PrecisProcessor;
import com.fratics.precis.base.ValueObject;
import com.fratics.precis.fis.feed.BaseFeedElement;
import com.fratics.precis.fis.feed.BaseFeedPartitioner;
import com.fratics.precis.fis.feed.BaseFeedPartitioner.BaseFeedPartitionerReader;

public class NewCandidateGeneratorStage2 extends PrecisProcessor {
    private int currStage = 2;
    private ValueObject o;
    
    private void crossProduct(BaseCandidateElement [] it){
	for(int i = 0; i < it.length; i++){
	    for(int j = i + 1; j < it.length; j++){
		if(it[i].xor(it[j]).cardinality() == 4){
		    BitSet b = it[i].or(it[j]);
		    o.inputObject.addCandidate(b);
		}
	    }
	}
    }
    
    public NewCandidateGeneratorStage2(int stage){
	this.currStage = stage;
    }
    
    public boolean process(ValueObject o) throws Exception {
	this.o = o;
	o.inputObject.currentStage = currStage;
	BaseFeedPartitioner bp = o.inputObject.getPartitioner();
	BaseCandidateElement [] it = (BaseCandidateElement []) o.inputObject.getPrevCandidateMap().values().toArray(new BaseCandidateElement[0]);
	System.err.println("Current Stage ::" + this.currStage);
	System.err.println("No of Candidates from Previous Stage ::" + it.length);
	bp.initReader(this.currStage);
	crossProduct(it);
	System.err.println("No of Candidates Before Applying Threshold::" + o.inputObject.currCandidateSet.size());
	BaseFeedPartitionerReader bpr = bp.getReader();
	boolean countPrecis = o.inputObject.isCountPrecis();
	while(bpr.hasNext()){
	    BaseFeedElement be = bpr.getNext();
	    //System.err.println(be);
	    for(ArrayList<BaseCandidateElement> al : o.inputObject.currCandidatePart.values()){
		for(BaseCandidateElement bce : al){
		    if(bce.and(be.getBitSet()).equals(bce.getBitSet())){
			if(countPrecis)
			    bce.incrMetric();
        		else
        		    bce.incrMetricBy(be.getMetric());
		    }
		}
	    }
	}
	bp.closeReader();
	o.inputObject.applyThreshold();
	System.err.println("No of Candidates After Applying Threshold::" + o.inputObject.currCandidateSet.size());
	o.inputObject.moveTo();
	return true;
    }
}
