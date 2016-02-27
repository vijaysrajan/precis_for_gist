package com.fratics.precis.reader;

import com.fratics.precis.base.PrecisProcessor;
import com.fratics.precis.base.PrecisStream;
import com.fratics.precis.base.ValueObject;

public class PrecisFileStreamProcessor extends PrecisProcessor {

    private PrecisStream ps = null;

    public PrecisFileStreamProcessor(PrecisStream ps) {
	this.ps = ps;
    }

    public boolean initialize() throws Exception {
	return this.ps.initialize();
    }

    public boolean unInitialize() throws Exception {
	return this.ps.unInitialize();
    }

    public boolean process(ValueObject o) throws Exception {
	String[] str = null;
	while ((str = ps.readStream()) != null) {
	    o.inputObject.loadInput(str);
	}
	// Set the Number of Lines as Well.
	o.inputObject.setNoOfValues(ps.getNoOfLines());
	return true;
    }
}
