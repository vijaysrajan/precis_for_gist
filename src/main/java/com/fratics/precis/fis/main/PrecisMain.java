package com.fratics.precis.fis.main;

import com.fratics.precis.fis.base.PrecisProcessor;
import com.fratics.precis.fis.base.ValueObject;
import com.fratics.precis.fis.feed.BitSetFeed;
import com.fratics.precis.fis.feed.candidategeneration.CandidateGeneratorStage2;
import com.fratics.precis.fis.feed.candidategeneration.CandidateGeneratorStg3Onwards;
import com.fratics.precis.fis.feed.dimval.DimValIndex;
import com.fratics.precis.fis.schema.PrecisSchemaProcessor;
import com.fratics.precis.reader.PrecisFileStream;
import com.fratics.precis.reader.PrecisFileStreamProcessor;

public class PrecisMain extends PrecisProcessor {

    private PrecisProcessor[] ps = null;

    public PrecisMain(String streamName) {
	ps = new PrecisProcessor[23];
	ps[0] = new PrecisSchemaProcessor(new PrecisFileStream(
		"./data/schemaFile"));
	ps[1] = new PrecisFileStreamProcessor(new PrecisFileStream(streamName));
	ps[2] = new DimValIndex(10000.0);
	ps[3] = new BitSetFeed(new PrecisFileStream(streamName));
	ps[4] = new CandidateGeneratorStage2(2);
	ps[5] = new CandidateGeneratorStg3Onwards(3);
	ps[6] = new CandidateGeneratorStg3Onwards(4);
	ps[7] = new CandidateGeneratorStg3Onwards(5);
	ps[8] = new CandidateGeneratorStg3Onwards(6);
	ps[9] = new CandidateGeneratorStg3Onwards(7);
	ps[10] = new CandidateGeneratorStg3Onwards(8);
	ps[11] = new CandidateGeneratorStg3Onwards(9);
	ps[12] = new CandidateGeneratorStg3Onwards(10);
	ps[13] = new CandidateGeneratorStg3Onwards(11);
	ps[14] = new CandidateGeneratorStg3Onwards(12);
	ps[15] = new CandidateGeneratorStg3Onwards(13);
	ps[16] = new CandidateGeneratorStg3Onwards(14);
	ps[17] = new CandidateGeneratorStg3Onwards(15);
	ps[18] = new CandidateGeneratorStg3Onwards(16);
	ps[19] = new CandidateGeneratorStg3Onwards(17);
	ps[20] = new CandidateGeneratorStg3Onwards(18);
	ps[21] = new CandidateGeneratorStg3Onwards(19);
	ps[22] = new CandidateGeneratorStg3Onwards(20);

    }

    public boolean initialize() throws Exception {
	for (int i = 0; i < ps.length; i++)
	    ps[i].initialize();
	return true;
    }

    public boolean unInitialize() throws Exception {
	for (int i = ps.length - 1; i >= 0; i--)
	    ps[i].unInitialize();
	return true;

    }

    public boolean process(ValueObject o) throws Exception {
	// Initialize & run the various Processors in sequence on the Value
	// Object
	for (int i = 0; i < ps.length; i++) {
	    System.err.println("Executing Handler :: "
		    + ps[i].getClass().getName());
	    ps[i].process(o);
	}
	return true;
    }

    private static void printUsage() {
	System.out.println();
	System.out.println();
	System.out.print("Usage :: java SanitationMain ${fileName}");
	System.out
		.println("Atleast 1 arguement - File/Stream Name is Required");
	System.out.println();
	System.out.println();
    }

    public static void main(String[] args) {
	if (args.length < 1) {
	    printUsage();
	    System.exit(0);
	}
	try {
	    ValueObject vo = new ValueObject();
	    vo.inputObject = new PrecisInputObject();
	    vo.resultObject = new PrecisOutputObject();
	    PrecisMain sm = new PrecisMain(args[0]);
	    sm.initialize();
	    sm.process(vo);
	    sm.unInitialize();
	    // System.err.println(vo);
	    System.err.println();

	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
