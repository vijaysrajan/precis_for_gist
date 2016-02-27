package com.fratics.precis.util;

import java.io.File;
import java.io.PrintWriter;
import java.util.BitSet;
import java.util.Iterator;
import java.util.Map;

import com.fratics.precis.base.ValueObject;
import com.fratics.precis.candidategeneration.BaseCandidateElement;
import com.fratics.precis.dimval.DimValIndex;

public class Util {
    
    public static String generateRandomId() {
	return Integer.toString((int) (Math.random() * 100000));
    }
    
    private static void writeCandidates(int stage, PrintWriter pw, Map<BitSet, BaseCandidateElement> map) throws Exception {
	String ret = "";
	int startIndex = 0;
	int val = 0;
	int index = stage;
	Iterator<BaseCandidateElement> it = map.values().iterator();
	while(it.hasNext()){
	    BaseCandidateElement bce = it.next();
	    //System.err.println(bce);
	    BitSet b = bce.getBitSet();
	    index = stage;
	    val = 0;
	    ret = "";
	    startIndex = DimValIndex.dimMap.size();
	    while(index > 0){
		val = b.nextSetBit(startIndex);
		ret = ret + DimValIndex.revDimValMap.get(val);
		startIndex = val + 1;
		index--;
		if(index > 0) ret = ret + DimValIndex.dimDelimiter;
	    }
	    ret = ret +  DimValIndex.dimDelimiter + bce.getMetric() + "\n";
	    pw.write(ret);
	}
    }
    
    public static void dump(int currStage, ValueObject o) {
 	try {
 	    PrintWriter pw = new PrintWriter(new File("./data/stage" + currStage + "_candidates.txt"));
 	    writeCandidates(currStage, pw, o.inputObject.getCurrentCandidateMap());
 	    pw.flush();
 	    pw.close();
 	    /*
	    pw = new PrintWriter(new File("./data/stage" + currStage + "_raw_candidates.txt"));
 	    pw.append(new PrettyPrintingMap<BitSet, BaseCandidateElement>(o.inputObject.getCurrentCandidateMap()).toString());
 	    pw.flush();
 	    pw.close();
 	    */
 	} catch (Exception e) {
 	    e.printStackTrace();
 	}
   }
}
