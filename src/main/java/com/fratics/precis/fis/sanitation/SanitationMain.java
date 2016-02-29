package com.fratics.precis.fis.sanitation;

import com.fratics.precis.fis.base.PrecisProcessor;
import com.fratics.precis.fis.base.ValueObject;
import com.fratics.precis.fis.feed.BitSetFeed;
import com.fratics.precis.fis.feed.candidategeneration.CandidateGeneratorStage2;
import com.fratics.precis.fis.feed.candidategeneration.CandidateGeneratorStg3Onwards;
import com.fratics.precis.fis.feed.dimval.DimValIndex;
import com.fratics.precis.fis.schema.PrecisSchemaProcessor;
import com.fratics.precis.reader.PrecisFileStream;
import com.fratics.precis.reader.PrecisFileStreamProcessor;

public class SanitationMain extends PrecisProcessor {

    private PrecisProcessor[] ps = null;

    public SanitationMain(String streamName) {
	ps = new PrecisProcessor[8];
	ps[0] = new PrecisSchemaProcessor(new PrecisFileStream(
		"./data/schemaFile"));
	ps[1] = new PrecisFileStreamProcessor(new PrecisFileStream(streamName));
	ps[2] = new SanitationRuleProcessor();
	ps[3] = new DimValIndex(2500.0);
	ps[4] = new BitSetFeed(new PrecisFileStream(streamName));
	ps[5] = new CandidateGeneratorStage2(2);
	ps[6] = new CandidateGeneratorStg3Onwards(3);
	ps[7] = new CandidateGeneratorStg3Onwards(4);

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
	for (int i = 0; i < ps.length; i++)
	    ps[i].process(o);
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
	    vo.inputObject = new SanitationInputObject();
	    vo.resultObject = new SanitationOutputObject();
	    SanitationMain sm = new SanitationMain(args[0]);
	    sm.initialize();
	    sm.process(vo);
	    sm.unInitialize();
	    System.err.println(vo);
	    System.err.println();

	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
