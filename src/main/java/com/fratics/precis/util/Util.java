package com.fratics.precis.util;

import java.io.File;
import java.io.PrintWriter;
import java.util.BitSet;
import java.util.Iterator;
import java.util.Map;

import com.fratics.precis.base.MutableInt;
import com.fratics.precis.base.ValueObject;
import com.fratics.precis.candidategeneration.BaseCandidateElement;
import com.fratics.precis.dimval.DimValIndex;

public class Util {
    
    public static String generateRandomId() {
	return Integer.toString((int) (Math.random() * 100000));
    }
    
    private static String formatCandidates(int stage, Map<BitSet, BaseCandidateElement> map){
	String ret = "";
	int startIndex = 0;
	int val = 0;
	int index = stage;
	Iterator<BaseCandidateElement> it = map.values().iterator();
	while(it.hasNext()){
	    BaseCandidateElement bce = it.next();
	    BitSet b = bce.getBitSet();
	    index = stage;
	    val = 0;
	    startIndex = DimValIndex.dimMap.size();
	    while(index > 0){
		val = b.nextSetBit(startIndex);
		ret = ret + DimValIndex.revDimValMap.get(new MutableInt(val));
		startIndex = val + 1;
		index--;
		if(index > 0) ret = ret + DimValIndex.dimDelimiter;
	    }
	    ret = ret +  DimValIndex.dimDelimiter + bce.getMetric() + "\n";
	}
	return ret;
    }
    
    public static void dump(int currStage, ValueObject o) {
 	try {
 	    PrintWriter pw = new PrintWriter(new File("./data/stage" + currStage + "_candidates.txt"));
 	    //pw.append(new PrettyPrintingMap<BitSet, BaseCandidateElement>(o.inputObject.getCurrentCandidateMap()).toString());
 	    pw.append(formatCandidates(currStage, o.inputObject.getCurrentCandidateMap()));
 	    pw.flush();
 	    pw.close();
	    pw = new PrintWriter(new File("./data/stage" + currStage + "_raw_candidates.txt"));
 	    pw.append(new PrettyPrintingMap<BitSet, BaseCandidateElement>(o.inputObject.getCurrentCandidateMap()).toString());
 	    //pw.append(formatCandidates(currStage, o.inputObject.getCurrentCandidateMap()));
 	    pw.flush();
 	    pw.close();
 	} catch (Exception e) {
 	    e.printStackTrace();
 	}
   }
}
