package com.fratics.precis.reader;

import java.util.ArrayList;
import java.util.List;

import com.fratics.precis.base.PrecisProcessor;
import com.fratics.precis.base.PrecisStream;
import com.fratics.precis.base.ValueObject;

public class PrecisSchemaProcessor extends PrecisProcessor {
    private PrecisStream ps = null;

    public PrecisSchemaProcessor(PrecisStream ps) {
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
	List<String> s = new ArrayList<String>();
	while ((str = ps.readStream()) != null) {
	    s.add(str[0]);
	}
	str = new String[s.size()];
	str = (String[]) s.toArray(str);
	o.inputObject.loadSchema(str);
	return false;
    }
}
