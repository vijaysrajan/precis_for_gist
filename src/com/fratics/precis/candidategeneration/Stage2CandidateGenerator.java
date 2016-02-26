package com.fratics.precis.candidategeneration;

import java.io.File;
import java.io.PrintWriter;
import java.util.BitSet;

import com.fratics.precis.base.PrecisProcessor;
import com.fratics.precis.base.ValueObject;
import com.fratics.precis.fis.feed.BaseFeedElement;
import com.fratics.precis.fis.feed.BaseFeedPartitioner;
import com.fratics.precis.fis.feed.BaseFeedPartitioner.BaseFeedPartitionerReader;
import com.fratics.precis.util.PrettyPrintingMap;

public class Stage2CandidateGenerator extends PrecisProcessor {
    private static int currStage = 2;
    private ValueObject o;
    
    private void dump() {
	try {
	    PrintWriter pw = new PrintWriter(new File("./data/stage2_candidates.txt"));
	    pw.append(new PrettyPrintingMap<BitSet, BaseCandidateElement>(o.inputObject.getCurrentCandidateMap()).toString());
	    pw.flush();
	    pw.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void crossProduct(BaseFeedElement be, BaseCandidateElement [] it){
	double metric = 0.0;
	for(int i = 0; i < it.length; i++){
	    for(int j = i + 1; j < it.length; j++){
		if(it[i].xor(it[j]).cardinality() == 4){
		    BitSet b = it[i].or(it[j]);
		    //System.err.println("it 1 :: " +  it[i] + " it 2 ::"+ it[j] + " b :: " +  b);
		    if(be.and(b).equals(b)){
			metric = o.inputObject.isCountPrecis() ? 1 : be.getMetric();
			//System.err.println("it 1 :: " +  it[i] + " it 2 ::"+ it[j] + " b :: " +  b);
			o.inputObject.addNextCandidateElement(new BaseCandidateElement(b, metric));
		    }
		}
	    }
	}
    }
    
    public boolean process(ValueObject o) throws Exception {
	this.o = o;
	o.inputObject.currentStage = currStage;
	o.inputObject.moveToNextStage();
	BaseFeedPartitioner bp = o.inputObject.getPartitioner();
	BaseCandidateElement [] it = (BaseCandidateElement []) o.inputObject.getPrevCandidateMap().values().toArray(new BaseCandidateElement[0]);
	bp.initReader(currStage);
	BaseFeedPartitionerReader bpr = bp.getReader();
	while(bpr.hasNext()){
	    BaseFeedElement be = bpr.getNext();
	    //System.err.println(be);
	    crossProduct(be, it);
	}
	dump();
	return true;
    }
}
