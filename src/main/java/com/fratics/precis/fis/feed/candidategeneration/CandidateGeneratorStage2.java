package com.fratics.precis.fis.feed.candidategeneration;

import java.util.ArrayList;
import java.util.BitSet;

import com.fratics.precis.fis.base.BaseCandidateElement;
import com.fratics.precis.fis.base.BaseFeedElement;
import com.fratics.precis.fis.base.PrecisProcessor;
import com.fratics.precis.fis.base.ValueObject;
import com.fratics.precis.fis.feed.BaseFeedPartitioner;
import com.fratics.precis.fis.feed.BaseFeedPartitioner.BaseFeedPartitionerReader;
import com.fratics.precis.fis.util.Util;

public class CandidateGeneratorStage2 extends PrecisProcessor {
    private int currStage = 2;
    private ValueObject o;

    private void crossProduct() {
	BaseCandidateElement[] it = o.inputObject.firstStageCandidates.values()
		.toArray(new BaseCandidateElement[0]);
	for (int i = 0; i < it.length; i++) {
	    for (int j = i + 1; j < it.length; j++) {
		if (it[i].xor(it[j]).cardinality() == 4) {
		    BitSet b = it[i].or(it[j]);
		    o.inputObject.addCandidate(b);
		}
	    }
	}
    }

    public CandidateGeneratorStage2(int stage) {
	this.currStage = stage;
    }

    public boolean process(ValueObject o) throws Exception {
	this.o = o;
	o.inputObject.currentStage = currStage;
	BaseFeedPartitioner bp = o.inputObject.getPartitioner();
	System.err.println("Current Stage ::" + this.currStage);
	System.err.println("No of Candidates from Previous Stage ::"
		+ o.inputObject.firstStageCandidates.values().size());
	bp.initReader(this.currStage);
	crossProduct();
	System.err.println("No of Candidates Before Applying Threshold::"
		+ o.inputObject.currCandidateSet.size());
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
	boolean ret = o.inputObject.applyThreshold();
	System.err.println("No of Candidates After Applying Threshold::"
		+ o.inputObject.currCandidateSet.size());
	if(ret) Util.dump(this.currStage, o);
	o.inputObject.moveToNextStage();
	return ret;
    }
}
