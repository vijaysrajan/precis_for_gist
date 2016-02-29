package com.fratics.precis.fis.feed;

import com.fratics.precis.fis.base.BaseCandidateElement;
import com.fratics.precis.fis.base.BaseFeedElement;
import com.fratics.precis.fis.base.FieldObject;
import com.fratics.precis.fis.base.PrecisProcessor;
import com.fratics.precis.fis.base.PrecisStream;
import com.fratics.precis.fis.base.ValueObject;
import com.fratics.precis.fis.feed.dimval.DimValIndex;
import com.fratics.precis.fis.feed.dimval.DimValIndexBase;
import com.fratics.precis.fis.util.Util;

public class BitSetFeed extends PrecisProcessor {

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

    public boolean process(ValueObject o) throws Exception {
	o.inputObject.currentStage = 1;
	String[] str = null;
	FieldObject[] fi = o.inputObject.getFieldObjects();
	BaseFeedPartitioner partitioner = new BaseFeedPartitioner(fi.length);
	boolean metricPrecis = !o.inputObject.isCountPrecis();
	boolean elementAddedflag = false;
	boolean metricGenerated = false;
	double metric = 0.0;
	while ((str = ps.readStream()) != null) {
	    BaseFeedElement e = new BaseFeedElement(
		    (int) DimValIndexBase.getDimValBitSetLength());
	    elementAddedflag = false;
	    metricGenerated = false;
	    metric = 0.0;
	    for (int i = 0; i < str.length; i++) {
		String tmpDim = fi[i].getSchemaElement().fieldName;
		String tmpDimVal = fi[i].getSchemaElement().fieldName
			+ DimValIndexBase.dimValDelimiter
			+ str[fi[i].getSchemaElement().fieldIndex];
		if (DimValIndex.dimMap.containsKey(tmpDim)) {
		    if (DimValIndex.dimValMap.containsKey(tmpDimVal)) {
			int index1 = DimValIndex.dimMap.get(tmpDim);
			int index2 = DimValIndex.dimValMap.get(tmpDimVal);
			e.setBit(index1);
			e.setBit(index2);
			elementAddedflag = true;
			if (metricPrecis && !metricGenerated) {
			    metricGenerated = true;
			    metric = Double.parseDouble(str[o.inputObject
				    .getMetricIndex()]);
			    e.setMetric(metric);
			}
			// add stage 1 candidates.
			BaseCandidateElement bce = new BaseCandidateElement(
				(int) DimValIndexBase.getDimValBitSetLength());
			bce.setBit(index1);
			bce.setBit(index2);
			if (metricPrecis)
			    bce.setMetric(metric);
			else
			    bce.setMetric(1.0);
			o.inputObject.addFirstStageCandidateElement(bce);
		    }
		}
	    }
	    // e.setMetric(0.0); //for now, we need to change this.
	    if (elementAddedflag) {
		// System.err.println(e);
		partitioner.addElement(e.getNumberofDimVals() - 1, e);
	    }
	}
	o.inputObject.setPartitioner(partitioner);
	partitioner.dump();
	Util.dump(1, o);
	o.inputObject.moveToNextStage();
	return true;
    }
}