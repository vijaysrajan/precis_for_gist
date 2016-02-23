package com.fratics.precis.fis.app;

import com.fratics.precis.base.PrecisProcessor;
import com.fratics.precis.base.ValueObject;
import com.fratics.precis.dimval.DimValIndex;
import com.fratics.precis.fis.feed.BitSetFeed;
import com.fratics.precis.reader.PrecisFileStream;
import com.fratics.precis.reader.PrecisFileStreamProcessor;
import com.fratics.precis.schema.PrecisSchemaProcessor;

public class PrecisMain extends PrecisProcessor {

    private PrecisProcessor[] ps = null;

    public PrecisMain(String streamName) {
	ps = new PrecisProcessor[4];
	ps[0] = new PrecisSchemaProcessor(new PrecisFileStream(
		"./data/schemaFile"));
	ps[1] = new PrecisFileStreamProcessor(new PrecisFileStream(streamName));
	ps[2] = new DimValIndex(36000.0);
	ps[3] = new BitSetFeed(new PrecisFileStream(streamName));
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
	    vo.inputObject = new PrecisInputObject();
	    vo.resultObject = new PrecisOutputObject();
	    PrecisMain sm = new PrecisMain(args[0]);
	    sm.initialize();
	    sm.process(vo);
	    sm.unInitialize();
	    System.err.println(vo);
	    System.err.println();
	    System.err.println(DimValIndex.dumpIndexes());

	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}