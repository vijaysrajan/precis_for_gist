package com.fratics.precis.fis.feed.candidategeneration;

import java.util.BitSet;
import java.util.Iterator;

import com.fratics.precis.fis.base.BaseCandidateElement;
import com.fratics.precis.fis.base.BaseFeedElement;
import com.fratics.precis.fis.base.PrecisProcessor;
import com.fratics.precis.fis.base.ValueObject;
import com.fratics.precis.fis.feed.BaseFeedPartitioner;
import com.fratics.precis.fis.feed.BaseFeedPartitioner.BaseFeedPartitionerReader;

/*
 * This is the Second stage Candidate Generator. This is developed as a flow processor.
 * 
 * The Main functionalities are:-
 * 1) cross product on the first stage candidates to generate potential candidates.
 * 2) Applies the Base feed data on the potential candidates
 * 3) Applies the threshold handler to filter the successful candidates.
 * 
 */

public class CandidateGeneratorStage2 extends PrecisProcessor {
    private int currStage = 2;
    private ValueObject o;

    // Produces the cross product of the first stage candidates to
    // generate the 2nd Stage potential candidates.
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

    public CandidateGeneratorStage2() {
    }

    public boolean process(ValueObject o) throws Exception {
	this.o = o;
	o.inputObject.currentStage = currStage;
	BaseFeedPartitioner bp = o.inputObject.getPartitioner();
	System.err.println("Current Stage ::" + this.currStage);
	System.err.println("No of Candidates from Previous Stage ::"
		+ o.inputObject.firstStageCandidates.values().size());

	// Initialize Reader
	bp.initReader(this.currStage);

	// Generate Cross Product
	crossProduct();

	System.err.println("No of Candidates Before Applying Threshold::"
		+ o.inputObject.currCandidateSet.size());

	BaseFeedPartitionerReader bpr = bp.getReader();
	boolean countPrecis = o.inputObject.isCountPrecis();

	// Read the Input feed from Partitioner as bit set feed and apply the
	// metrics
	// to the candidates.
	while (bpr.hasNext()) {
	    BaseFeedElement be = bpr.getNext();
	    // System.err.println(be);
	    for (Iterator<BaseCandidateElement> i = o.inputObject.currCandidateSet
		    .iterator(); i.hasNext();) {
		BaseCandidateElement bce = i.next();
		if (bce.and(be.getBitSet()).equals(bce.getBitSet())) {
		    if (countPrecis)
			bce.incrMetric();
		    else
			bce.incrMetricBy(be.getMetric());
		}
		// apply threshold
		if (bce.getMetric() >= o.inputObject.getThreshold()) {
		    o.inputObject.addSuccessFulCandidate(bce.getBitSet());
		    i.remove();
		}
	    }
	}
	bp.closeReader();

	// Apply the threshold handler.
	boolean ret = (o.inputObject.succCandidateSet.size() > 0);
	System.err.println("No of Candidates After Applying Threshold::"
		+ o.inputObject.succCandidateSet.size());

	// Move the precis context to next stage - 3
	o.inputObject.moveToNextStage();
	return ret;
    }
}
