package com.fratics.precis.candidategeneration;

import java.util.Arrays;
import java.util.BitSet;

import com.fratics.precis.base.PrecisProcessor;
import com.fratics.precis.base.ValueObject;
import com.fratics.precis.fis.feed.BaseFeedPartitioner;

public class NewCandidateGeneratorStage2 extends PrecisProcessor {
    private int currStage = 2;
    private ValueObject o;
    
    private void crossProduct(BaseCandidateElement [] it){
	double metric = 0.0;
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
	System.err.println("Lenght of Prev Candidates ::" + it.length + " values ::" + Arrays.toString(it));
	bp.initReader(this.currStage);
	crossProduct(it);
	System.err.println("No of Candidates ::" + o.inputObject.currCandidateSet.size() + " values:: " + o.inputObject.currCandidateSet);
	o.inputObject.moveTo();
//	BaseFeedPartitionerReader bpr = bp.getReader();
//	while(bpr.hasNext()){
//	    BaseFeedElement be = bpr.getNext();
//	    Iterator<ArrayList<BitSet>> itbs = o.inputObject.currCandidateSet.values().iterator();
//	}
//	
//	//System.err.println("Size Before pruning ::" + o.inputObject.getCurrentCandidateMap().size());
//	//System.err.println(o.inputObject.getCurrentCandidateMap());
//	//o.inputObject.selectSuccessfulCandidates();
//	bp.closeReader();
//	Util.dump(currStage,o);
	return true;
    }
}
