package com.fratics.precis.candidategeneration;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;

import com.fratics.precis.base.PrecisProcessor;
import com.fratics.precis.base.ValueObject;
import com.fratics.precis.dimval.DimValIndex;
import com.fratics.precis.fis.feed.BaseFeedPartitioner;

public class NewCandidateGeneratorSTGTK2 extends PrecisProcessor {
    private int currStage = 2;
    private ValueObject o;
    private HashMap<BitSet, ArrayList<BitSet>> cdParttion; 
    
    public boolean isPresent(BitSet bs1) {

 	// get the stage_num_const
 	// check the first set bit
 	// loop through stage_num_const
 	// clone the bitset
 	//
 	boolean result = true;
 	int valIndex = -1;
 	for (int i = 0; i <= currStage; i++) {
 	    valIndex = bs1.nextSetBit(valIndex + 1);
 	}

 	int dimIndex = -1;
 	for (int i = 0; i < currStage; i++) {
 	    dimIndex = bs1.nextSetBit(dimIndex + 1);
 	    if (i != 0) {
 		valIndex = bs1.nextSetBit(valIndex + 1);
 	    }
 	    bs1.clear(dimIndex);
 	    bs1.clear(valIndex);

 	    if (cdParttion.containsKey(bs1)) {
 		System.out.println("--->BS present:" + bs1.toString());
 		bs1.set(dimIndex);
 		bs1.set(valIndex);
 	    } else {
 		result = false;
 		System.out.println("dimIndex=" + dimIndex + "; metIndex="
 			+ valIndex);
 		System.out.println("BS Not present::" + bs1.toString());
 		bs1.set(dimIndex);
 		bs1.set(valIndex);
 		break;
 	    }
 	}
 	return result;
     }
    
    private void crossProduct(){
	int size = DimValIndex.dimMap.size() + DimValIndex.dimValMap.size();
	BitSet allOneBS1 = new BitSet(size);
	allOneBS1.flip(0, size-1);

	BitSet allOneBS2 = new BitSet(size);
	allOneBS2.flip(0, size-1);

	for (ArrayList<BitSet> bsList : cdParttion.values()) {
	    for (int i = 0; i < bsList.size() - 1; i++) {
		allOneBS1.and(bsList.get(i));
		for (int j = i + 1; j < bsList.size(); j++) {
		    allOneBS1.xor(bsList.get(j));
		    if (allOneBS1.cardinality() == 4) {
			BitSet newCand = (BitSet) bsList.get(i).clone();
			System.out.println("2 Sets --> ::" + bsList.get(i) + " " + bsList.get(j));
			System.out.println("New Candidate::"
				+ newCand.toString());
			newCand.or(bsList.get(j));
			System.out.println("New Candidate to test::"
				+ newCand.toString());
			if (isPresent(newCand)) {
			    o.inputObject.addCandidate(newCand);
			}

		    }
		}
	    }
	}
    }
    
    public NewCandidateGeneratorSTGTK2(int stage){
	this.currStage = stage;
    }
    
    public boolean process(ValueObject o) throws Exception {
	this.o = o;
	cdParttion = o.inputObject.prevCandidateSet;
	o.inputObject.currentStage = currStage;
	o.inputObject.moveToNextStage();
	BaseFeedPartitioner bp = o.inputObject.getPartitioner();
	BaseCandidateElement [] it = (BaseCandidateElement []) o.inputObject.getPrevCandidateMap().values().toArray(new BaseCandidateElement[0]);
	System.err.println("Current Stage ::" + this.currStage);
	System.err.println("Lenght of Prev Candidates ::" + o.inputObject.prevCandidateSet.size());
	bp.initReader(this.currStage);
	crossProduct();
	System.err.println("No of Candidates ::" + o.inputObject.currCandidateSet.size());
	o.inputObject.moveTo();
	
//	BaseFeedPartitionerReader bpr = bp.getReader();
//	while(bpr.hasNext()){
//	    BaseFeedElement be = bpr.getNext();
//	    //System.err.println(be);
//	    crossProduct(be, it);
//	}
//	//System.err.println("Size Before pruning ::" + o.inputObject.getCurrentCandidateMap().size());
//	//System.err.println(o.inputObject.getCurrentCandidateMap());
//	o.inputObject.selectSuccessfulCandidates();
//	bp.closeReader();
//	Util.dump(currStage,o);
	return true;
    }
}
