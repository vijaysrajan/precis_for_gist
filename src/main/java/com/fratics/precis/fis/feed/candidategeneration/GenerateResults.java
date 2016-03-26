package com.fratics.precis.fis.feed.candidategeneration;

import java.util.HashSet;

import com.fratics.precis.fis.base.BaseCandidateElement;
import com.fratics.precis.fis.base.BaseFeedElement;
import com.fratics.precis.fis.base.PrecisProcessor;
import com.fratics.precis.fis.base.ValueObject;
import com.fratics.precis.fis.feed.BaseFeedPartitioner;
import com.fratics.precis.fis.feed.BaseFeedPartitioner.BaseFeedPartitionerReader;
import com.fratics.precis.fis.util.Util;

/*
 * Generates the Results from by applying the Base Feeds to all the Candidates Generated
 * 
 * OBSOLETE NOW, USING THE THREAD CANDIDATES WRITER.
 * 
 */

public class GenerateResults extends PrecisProcessor {

    public GenerateResults() {
    }

    public boolean process(ValueObject o) throws Exception {

	// Start Dumping From 2nd Stage.
	int stageProcessed = 2;
	BaseFeedPartitioner bp = o.inputObject.getPartitioner();

	// Initialize Reader
	bp.initReader(stageProcessed);

	BaseFeedPartitionerReader bpr = bp.getReader();
	boolean countPrecis = o.inputObject.isCountPrecis();

	// Read the Input feed from Partitioner as bit set feed and apply the
	// metrics to the candidates.
	while (bpr.hasNext()) {
	    BaseFeedElement be = bpr.getNext();
	    for (int i = stageProcessed - 1; i < o.inputObject.candidateStore.length; i++) {
		// System.err.println("Cand Store :;" +
		// o.inputObject.candidateStore[i] );
		if (o.inputObject.candidateStore[i].isEmpty())
		    break;
		if (bpr.getCurrentReadPartition() >= (i + 1)) {
		    HashSet<BaseCandidateElement> al = o.inputObject.candidateStore[i];
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
	}
	bp.closeReader();

	System.err.println("Generating Precis Candidates Files");

	// Write the files.
	Util.dump(1, o);
	for (int i = 1; i < o.inputObject.candidateStore.length; i++) {
	    if (o.inputObject.candidateStore[i].isEmpty())
		break;
	    Util.dump(i + 1, o);
	}
	return true;
    }
}
