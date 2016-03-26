package com.fratics.precis.fis.util;

import java.io.File;
import java.io.PrintWriter;
import java.util.BitSet;
import java.util.HashSet;

import com.fratics.precis.fis.base.BaseCandidateElement;
import com.fratics.precis.fis.base.ValueObject;
import com.fratics.precis.fis.feed.dimval.DimValIndex;

public class Util {

    public static String generateRandomId() {
	return Integer.toString((int) (Math.random() * 100000));
    }

    private static String convertToDims(int stage, BaseCandidateElement bce) {
	String ret = "";
	int startIndex = 0;
	int val = 0;
	int index = stage;
	// System.err.println(bce);
	BitSet b = bce.getBitSet();
	index = stage;
	val = 0;
	ret = "";
	startIndex = DimValIndex.dimMap.size();
	while (index > 0) {
	    val = b.nextSetBit(startIndex);
	    ret = ret + DimValIndex.revDimValMap.get(val);
	    startIndex = val + 1;
	    index--;
	    if (index > 0)
		ret = ret + PrecisConfigProperties.OUTPUT_RECORD_SEPERATOR;
	}
	ret = ret + PrecisConfigProperties.OUTPUT_RECORD_SEPERATOR
		+ bce.getMetric() + "\n";
	return ret;

    }

    private static void writeCandidates(int stage, PrintWriter pw, ValueObject o)
	    throws Exception {
	String ret = "";
	if (stage == 1) {
	    for (BaseCandidateElement bce : o.inputObject.firstStageCandidates
		    .values()) {
		ret = convertToDims(stage, bce);
		pw.write(ret);
	    }

	} else {
	    HashSet<BaseCandidateElement> al = o.inputObject.candidateStore[stage - 1];
	    for (BaseCandidateElement bce : al) {
		ret = convertToDims(stage, bce);
		pw.write(ret);
	    }
	}
    }

    public static void dump(int currStage, ValueObject o) throws Exception {
	try {
	    String outputDir = PrecisConfigProperties.OUTPUT_DIR + "/";
	    String fileName = PrecisConfigProperties.OUPUT_CANDIDATE_FILE_PATTERN
		    .replace("${stage_number}", "" + currStage);
	    PrintWriter pw = new PrintWriter(new File(outputDir + fileName));
	    writeCandidates(currStage, pw, o);
	    pw.flush();
	    pw.close();

	    if (PrecisConfigProperties.GENERATE_RAW_CANDIDATE_FILE) {
		fileName = PrecisConfigProperties.OUPUT_RAW_CANDIDATE_FILE_PATTERN
			.replace("${stage_number}", "" + currStage);
		pw = new PrintWriter(new File(outputDir + fileName));
		if (currStage == 1)
		    for (BitSet b : o.inputObject.firstStageCandidates.keySet())
			pw.write(b.toString() + "\n");
		else
		    for (BaseCandidateElement bce : o.inputObject.candidateStore[currStage - 1])
			pw.write(bce.getBitSet().toString() + "\n");
		pw.flush();
		pw.close();
	    }

	} catch (Exception e) {
	    throw e;
	}
    }
}
