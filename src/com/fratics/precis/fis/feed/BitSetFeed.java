package com.fratics.precis.fis.feed;

import java.io.File;
import java.io.PrintWriter;

import com.fratics.precis.base.FieldObject;
import com.fratics.precis.base.PrecisProcessor;
import com.fratics.precis.base.PrecisStream;
import com.fratics.precis.base.ValueObject;
import com.fratics.precis.dimval.DimValIndex;
import com.fratics.precis.dimval.DimValIndexBase;

public class BitSetFeed extends PrecisProcessor {

    public BaseFeedPartitioner partitioner = new BaseFeedPartitioner();

    private PrecisStream ps = null;

    public BitSetFeed(PrecisStream ps) {
	this.ps = ps;
    }

    public boolean initialize() throws Exception {
	return this.ps.initialize();
    }

    public boolean unInitialize() throws Exception {
	return this.ps.unInitialize();
    }

    public void dump() {
	try {
	    PrintWriter pw = new PrintWriter(new File("./data/out.txt"));
	    pw.append(partitioner.toString());
	    pw.flush();
	    pw.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public boolean process(ValueObject o) throws Exception {
	String[] str = null;
	FieldObject[] fi = o.inputObject.getFieldObjects();
	boolean metricPrecis = !o.inputObject.isCountPrecis();
	boolean elementAddedflag = false;
	while ((str = ps.readStream()) != null) {
	    BaseFeedElement e = new BaseFeedElement(
		    (int) DimValIndexBase.getDimValBitSetLength());
	    elementAddedflag = false;
	    for (int i = 0; i < str.length; i++) {
		String tmpDim = fi[i].getSchemaElement().fieldName;
		String tmpDimVal = fi[i].getSchemaElement().fieldName
			+ DimValIndexBase.dimValDelimiter + str[i];
		if (DimValIndex.dimMap.containsKey(tmpDim)) {
		    if (DimValIndex.dimValMap.containsKey(tmpDimVal)) {
			int index1 = DimValIndex.dimMap.get(tmpDim).get();
			int index2 = DimValIndex.dimValMap.get(tmpDimVal).get();
			e.addElement(index1);
			e.addElement(index2);
			if (metricPrecis)
			    e.setMetric(Double.parseDouble(str[o.inputObject
				    .getMetricIndex()]));
			elementAddedflag = true;
		    }
		}
	    }
	    // e.setMetric(0.0); //for now, we need to change this.
	    if (elementAddedflag)
		partitioner.addElement(e.getNumberofDimVals(), e);
	}
	dump();
	return true;
    }
}