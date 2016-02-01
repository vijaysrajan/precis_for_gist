package com.fratics.precis.sanitation;

import com.fratics.precis.base.PrecisProcessor;
import com.fratics.precis.base.ValueObject;
import com.fratics.precis.reader.PrecisFileStream;
import com.fratics.precis.reader.PrecisFileStreamProcessor;

public class SanitationMain extends PrecisProcessor {

    private PrecisProcessor[] ps = null;

    public SanitationMain(String streamName) {
	ps = new PrecisProcessor[2];
	ps[0] = new PrecisFileStreamProcessor(new PrecisFileStream(streamName));
	ps[1] = new SanitationRuleProcessor();
    }

    public boolean initialize() throws Exception {
	for (int i = 0; i < ps.length; i++)
	    ps[i].initialize();
	return true;
    }

    public boolean unInitialize() throws Exception {
	for (int i = 0; i < ps.length; i++)
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
	    vo.resultObject = new SanitationResultObject();
	    SanitationMain sm = new SanitationMain(args[0]);
	    sm.initialize();
	    sm.process(vo);
	    sm.unInitialize();
	    System.err.println(vo);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
