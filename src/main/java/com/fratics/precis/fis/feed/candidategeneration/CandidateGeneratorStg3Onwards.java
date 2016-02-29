package com.fratics.precis.fis.feed.candidategeneration;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;

import com.fratics.precis.fis.base.BaseCandidateElement;
import com.fratics.precis.fis.base.BaseFeedElement;
import com.fratics.precis.fis.base.PrecisProcessor;
import com.fratics.precis.fis.base.ValueObject;
import com.fratics.precis.fis.feed.BaseFeedPartitioner;
import com.fratics.precis.fis.feed.BaseFeedPartitioner.BaseFeedPartitionerReader;
import com.fratics.precis.fis.feed.dimval.DimValIndex;
import com.fratics.precis.fis.util.Util;

public class CandidateGeneratorStg3Onwards extends PrecisProcessor {
    private int currStage = 2;
    private ValueObject o;
    private HashMap<BitSet, ArrayList<BaseCandidateElement>> cdParttion;
    private HashSet<BitSet> cdSet;

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

	    if (cdSet.contains(bs1)) {

		// System.out.println("--->BS present:" + bs1.toString());
		bs1.set(dimIndex);
		bs1.set(valIndex);
	    } else {
		result = false;
		// System.out.println("dimIndex=" + dimIndex + "; metIndex="+
		// valIndex);
		// System.out.println("BS Not present::" + bs1.toString());
		bs1.set(dimIndex);
		bs1.set(valIndex);
		break;
	    }
	}
	return result;
    }

    private void crossProduct() {
	int size = DimValIndex.dimMap.size() + DimValIndex.dimValMap.size();
	BitSet allOneBS1 = new BitSet(size);
	allOneBS1.flip(0, size);

	BitSet allOneBS2 = new BitSet(size);
	allOneBS2.flip(0, size);

	for (ArrayList<BaseCandidateElement> bsList : cdParttion.values()) {
	    for (int i = 0; i < bsList.size() - 1; i++) {
		allOneBS1.and(bsList.get(i).getBitSet());
		for (int j = i + 1; j < bsList.size(); j++) {
		    allOneBS1.xor(bsList.get(j).getBitSet());
		    if (allOneBS1.cardinality() == 4) {
			BitSet newCand = (BitSet) bsList.get(i).getBitSet()
				.clone();
			newCand.or(bsList.get(j).getBitSet());
			if (isPresent(newCand)) {
			    o.inputObject.addCandidate(newCand);
			}
		    }
		    allOneBS1.or(allOneBS2);
		    allOneBS1.and(bsList.get(i).getBitSet());
		}
		allOneBS1.or(allOneBS2);
	    }
	}
    }

    public CandidateGeneratorStg3Onwards(int stage) {
	this.currStage = stage;
    }

    public boolean process(ValueObject o) throws Exception {
	this.o = o;
	cdParttion = o.inputObject.prevCandidatePart;
	cdSet = o.inputObject.prevCandidateSet;
	o.inputObject.currentStage = currStage;
	BaseFeedPartitioner bp = o.inputObject.getPartitioner();
	System.err.println("Current Stage ::" + this.currStage);
	System.err.println("No of Candidates from Previous Stage ::"
		+ o.inputObject.prevCandidateSet.size());
	bp.initReader(this.currStage);
	crossProduct();
	System.err.println("No of Candidates Before Applying Threshold::"
		+ o.inputObject.currCandidateSet.size());
	// System.err.println("No of Candidates Before Applying Threshold values:: "
	// + o.inputObject.currCandidatePart);
	BaseFeedPartitionerReader bpr = bp.getReader();
	boolean countPrecis = o.inputObject.isCountPrecis();
	while (bpr.hasNext()) {
	    BaseFeedElement be = bpr.getNext();
	    // System.err.println(be);
	    for (ArrayList<BaseCandidateElement> al : o.inputObject.currCandidatePart
		    .values()) {
		for (BaseCandidateElement bce : al) {
		    if (bce.and(be.getBitSet()).equals(bce.getBitSet())) {
			if (countPrecis)
			    bce.incrMetric();
			else
			    bce.incrMetricBy(be.getMetric());
		    }
		}
	    }
	}
	bp.closeReader();
	// System.err.println("No of Candidates Before Applying Threshold values:: "
	// + o.inputObject.currCandidatePart);
	o.inputObject.applyThreshold();
	System.err.println("No of Candidates After Applying Threshold::"
		+ o.inputObject.currCandidateSet.size());
	Util.dump(this.currStage, o);
	o.inputObject.moveToNextStage();
	return true;
    }
}
