package com.fratics.precis.candidategeneration;

import java.util.BitSet;

import com.fratics.precis.base.PrecisProcessor;
import com.fratics.precis.base.ValueObject;
import com.fratics.precis.fis.feed.BaseFeedElement;
import com.fratics.precis.fis.feed.BaseFeedPartitioner;
import com.fratics.precis.fis.feed.BaseFeedPartitioner.BaseFeedPartitionerReader;
import com.fratics.precis.util.Util;

public class CandidateGenerator extends PrecisProcessor {
    private int currStage = 2;
    private ValueObject o;
     
    private boolean compareEquals(BitSet b1, BitSet b2){
	if(this.currStage == 2) return true;
	//Else we need to compare currStage - 2 dimBits.
	int numToCompare = this.currStage - 2;
	int index1 = 0;
	int index2 = 0;
	for(int i = 0; i < numToCompare; i++){
	    index1 = b1.nextSetBit(index1);
	    index2 = b2.nextSetBit(index2);
	    if( index1 != index2 ) return false;
	    index1++;
	    index2++;
	}
	return true;
    }
    
    private void crossProduct(BaseCandidateElement [] it){
	for(int i = 0; i < it.length; i++){
	    for(int j = i + 1; j < it.length; j++){
		if(compareEquals(it[i].getBitSet(), it[j].getBitSet())) {
		    if(it[i].xor(it[j]).cardinality() == 4){
			BitSet b = it[i].or(it[j]);
        		o.inputObject.addNextCandidateElement(new BaseCandidateElement(b, 0.0));
		    }
		}else{
		    break;
		}
	    }
	}
    }
    
    public CandidateGenerator(int stage){
	this.currStage = stage;
    }
    
    public boolean process(ValueObject o) throws Exception {
	this.o = o;
	o.inputObject.currentStage = currStage;
	BaseFeedPartitioner bp = o.inputObject.getPartitioner();
	BaseCandidateElement [] it = (BaseCandidateElement []) o.inputObject.getPrevCandidateMap().values().toArray(new BaseCandidateElement[0]);
	System.err.println("Current Stage ::" + this.currStage);
	System.err.println("Lenght of Prev Candidates ::" + it.length);
	bp.initReader(this.currStage);
	crossProduct(it);
	BaseFeedPartitionerReader bpr = bp.getReader();
	boolean countPrecis = o.inputObject.isCountPrecis();
	while(bpr.hasNext()){
	    BaseFeedElement be = bpr.getNext();
	    //System.err.println(be);
	    for(BaseCandidateElement bce : o.inputObject.getCurrentCandidateMap().values()){
		if(bce.and(be.getBitSet()).equals(bce.getBitSet())){
		    if(countPrecis)
			bce.incrMetric();
		    else
			bce.incrMetricBy(be.getMetric());
		}
	    }
	}
	bp.closeReader();
	System.err.println("Size Before Candidates Generation ::" + o.inputObject.getCurrentCandidateMap().size());
	o.inputObject.selectSuccessfulCandidates();
	System.err.println("Size After Candidates Generation ::" + o.inputObject.getCurrentCandidateMap().size());
	Util.dump(currStage,o);
	o.inputObject.moveToNextStage();
	return true;
    }
}
