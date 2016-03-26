package com.fratics.precis.fis.feed.candidategeneration;

import java.util.HashSet;

import com.fratics.precis.fis.base.BaseCandidateElement;
import com.fratics.precis.fis.base.BaseFeedElement;
import com.fratics.precis.fis.base.PrecisProcessor;
import com.fratics.precis.fis.base.ValueObject;
import com.fratics.precis.fis.feed.BaseFeedPartitioner;
import com.fratics.precis.fis.feed.BaseFeedPartitioner.BaseFeedPartitionerReader;
import com.fratics.precis.fis.util.PrecisConfigProperties;
import com.fratics.precis.fis.util.Util;

/*
 * Generates the Results from by applying the Base Feeds to all the Candidates Generated
 * 
 */

public class GenResultsThread extends PrecisProcessor implements Runnable {

    public Thread thread;
    private ValueObject o;
    private int currIndex;
    private BaseFeedPartitioner bp;

    public GenResultsThread() {
	currIndex = 1;
    }

    private boolean doProcess(int index) throws Exception {

	// Start Dumping From 2nd Stage.
	int currStage = index + 1;

	if (index == 1) {
	    System.err.println("Writing Candidates for Stage :: 1");
	    // Dump the Stage 1 also.
	    Util.dump(1, o);
	}
	System.err.println("Writing Candidates for Stage :: " + currStage);

	// Initialize Reader
	bp.initReader(currStage);

	BaseFeedPartitionerReader bpr = bp.getReader();
	boolean countPrecis = o.inputObject.isCountPrecis();

	// Read the Input feed from Partitioner as bit set feed and apply the
	// metrics to the candidates.
	//int i = 0;
	while (bpr.hasNext()) {
	    //i++;
	    BaseFeedElement be = bpr.getNext();
	    //if(currStage == 6) System.err.println("1--> "+ be);
	    HashSet<BaseCandidateElement> al = o.inputObject.candidateStore[index];
	    for (BaseCandidateElement bce : al) {
		//if(currStage == 6) System.err.println("2--> "+ bce);
		if (bce.and(be.getBitSet()).equals(bce.getBitSet())) {
		    if (countPrecis)
			bce.incrMetric();
		    else
			bce.incrMetricBy(be.getMetric());
		    //if(currStage == 6) System.err.println("Line Number ::" + i + " Value Generated--> "+ bce);
		}
	    }
	}
	Util.dump(currStage, o);

	return true;
    }

    public boolean process(ValueObject o) throws Exception {
	this.o = o;
	bp = (BaseFeedPartitioner)o.inputObject.getPartitioner().deepClone();
	thread = new Thread(this);
	thread.start();
	o.inputObject.threadHandle = thread;
	return true;
    }

    public void run() {
	while (this.currIndex < PrecisConfigProperties.NO_OF_STAGES) {
	    try {
		if (o.inputObject.processingCompleted && this.currIndex >= o.inputObject.exitedStage) break;
		while (!o.inputObject.candidateStore[this.currIndex].dataLoaded.get()) Thread.sleep(10);
		doProcess(this.currIndex);
		if (this.currIndex >= 2 && !o.inputObject.candidateStore[this.currIndex - 1].isEmpty()){
		    o.inputObject.candidateStore[this.currIndex - 1].clear();
		    System.err.println("Purged Candidates from Memory for Stage ::" + (this.currIndex - 1));
		}
		this.currIndex++;

	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
    }
}
