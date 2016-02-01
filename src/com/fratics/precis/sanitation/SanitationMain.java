package com.fratics.precis.sanitation;

import com.fratics.precis.base.PrecisProcessor;
import com.fratics.precis.base.ValueObject;
import com.fratics.precis.reader.PrecisFileStream;
import com.fratics.precis.reader.PrecisFileStreamProcessor;

public class SanitationMain extends PrecisProcessor {

    private PrecisProcessor ps = null;
    private String precisStreamName = null;

    public SanitationMain(String st) {
	this.precisStreamName = st;
    }

    public boolean initialize() throws Exception {
	ps = new PrecisFileStreamProcessor(new PrecisFileStream(
		this.precisStreamName));
	ps.initialize();
	return true;
    }

    public boolean unInitialize() throws Exception {
	ps.unInitialize();
	return true;

    }

    public boolean process(ValueObject o) throws Exception {
	//Initialize various Processors for Populating the Value Object 
	//and run the sequence to the processors for the same.
	ps.process(o);
	//
	
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
	    SanitationObject so = new SanitationObject();
	    SanitationMain sm = new SanitationMain(args[0]);
	    sm.initialize();
	    sm.process(so);
	    sm.unInitialize();
	    System.err.println(so);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
