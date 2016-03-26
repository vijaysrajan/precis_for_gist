package com.fratics.precis.fis.main.metrics;

import com.fratics.precis.fis.base.PrecisProcessor;
import com.fratics.precis.fis.base.ValueObject;
import com.fratics.precis.fis.feed.BitSetFeed;
import com.fratics.precis.fis.feed.candidategeneration.CandidateGeneratorStage2;
import com.fratics.precis.fis.feed.candidategeneration.CandidateGeneratorStg3Onwards;
import com.fratics.precis.fis.feed.candidategeneration.GenResultsThread;
import com.fratics.precis.fis.feed.dimval.DimValIndex;
import com.fratics.precis.fis.schema.PrecisSchemaProcessor;
import com.fratics.precis.fis.util.PrecisConfigProperties;
import com.fratics.precis.reader.PrecisFileStream;
import com.fratics.precis.reader.PrecisInputCharacteristicsProcessor;

public class MetricsPrecisMain extends PrecisProcessor {

    private PrecisProcessor[] ps = null;

    public MetricsPrecisMain() {
	// Atleast 2 stages will be run, even if the configuration is less.
	ps = new PrecisProcessor[PrecisConfigProperties.NO_OF_STAGES + 4];
	ps[0] = new PrecisSchemaProcessor(new PrecisFileStream(
		PrecisConfigProperties.INPUT_SCHEMA_FILE,
		PrecisConfigProperties.SCHEMA_RECORD_SEPERATOR));
	ps[1] = new PrecisInputCharacteristicsProcessor(new PrecisFileStream(
		PrecisConfigProperties.INPUT_DATA_FILE));
	ps[2] = new DimValIndex(PrecisConfigProperties.THRESHOLD);
	ps[3] = new BitSetFeed(new PrecisFileStream(
		PrecisConfigProperties.INPUT_DATA_FILE));
	ps[4] = new GenResultsThread();
	ps[5] = new CandidateGeneratorStage2();
	for (int i = 3; i <= PrecisConfigProperties.NO_OF_STAGES; i++) {
	    ps[i + 3] = new CandidateGeneratorStg3Onwards(i);
	}
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
	boolean successFlag = true;
	int i = 0;
	for (i = 0; i < ps.length && successFlag; i++) {
	    System.err.println("Executing Handler :: "
		    + ps[i].getClass().getName());
	    successFlag = ps[i].process(o);
	}
	o.inputObject.exitedStage = i - 4;
	o.inputObject.processingCompleted = true;
	if(o.inputObject.threadHandle != null) o.inputObject.threadHandle.join();
	return true;
    }

    public static void run(String[] args) {
	try {
	    ValueObject vo = new ValueObject();
	    vo.inputObject = new MetricsPrecisInputObject();
	    vo.resultObject = new MetricsPrecisOutputObject();
	    MetricsPrecisMain sm = new MetricsPrecisMain();
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
