package com.fratics.precis.fis.feed;

import java.io.File;
import java.io.PrintWriter;
import java.util.BitSet;

import com.fratics.precis.base.FieldObject;
import com.fratics.precis.base.PrecisProcessor;
import com.fratics.precis.base.PrecisStream;
import com.fratics.precis.base.ValueObject;
import com.fratics.precis.candidategeneration.BaseCandidateElement;
import com.fratics.precis.dimval.DimValIndex;
import com.fratics.precis.dimval.DimValIndexBase;
import com.fratics.precis.util.PrettyPrintingMap;

public class BitSetFeed extends PrecisProcessor {

    private PrecisStream ps = null;
    private ValueObject o;

    public BitSetFeed(PrecisStream ps) {
	this.ps = ps;
    }

    public boolean initialize() throws Exception {
	return this.ps.initialize();
    }

    public boolean unInitialize() throws Exception {
	return this.ps.unInitialize();
    }

    private void dump(BaseFeedPartitioner partitioner) {
	try {
	    PrintWriter pw = new PrintWriter(new File("./data/bitSetFeed.txt"));
	    pw.append(partitioner.toString());
	    pw.flush();
	    pw.close();
	    pw = new PrintWriter(new File("./data/stage1_candidates.txt"));
	    pw.append(new PrettyPrintingMap<BitSet, BaseCandidateElement>(o.inputObject.getCurrentCandidateMap()).toString());
	    pw.flush();
	    pw.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public boolean process(ValueObject o) throws Exception {
	this.o = o;
	o.inputObject.currentStage = 1;
	String[] str = null;
	FieldObject[] fi = o.inputObject.getFieldObjects();
	BaseFeedPartitioner partitioner = new BaseFeedPartitioner(fi.length);
	boolean metricPrecis = !o.inputObject.isCountPrecis();
	boolean elementAddedflag = false;
	boolean metricGenerated = false;
	double metric = 0.0;
	while ((str = ps.readStream()) != null) {
	    BaseFeedElement e = new BaseFeedElement((int) DimValIndexBase.getDimValBitSetLength());
	    elementAddedflag = false;
	    metricGenerated = false;
	    metric = 0.0;
	    for (int i = 0; i < str.length; i++) {
		String tmpDim = fi[i].getSchemaElement().fieldName;
		String tmpDimVal = fi[i].getSchemaElement().fieldName
			+ DimValIndexBase.dimValDelimiter + str[i];
		if (DimValIndex.dimMap.containsKey(tmpDim)) {
		    if (DimValIndex.dimValMap.containsKey(tmpDimVal)) {
			int index1 = DimValIndex.dimMap.get(tmpDim).get();
			int index2 = DimValIndex.dimValMap.get(tmpDimVal).get();
			e.setBit(index1);
			e.setBit(index2);
			elementAddedflag = true;
			if (metricPrecis && !metricGenerated){
			    metricGenerated = true;
			    metric = Double.parseDouble(str[o.inputObject.getMetricIndex()]);
			    e.setMetric(metric);
			}
			//add stage 1 candidates.
			BaseCandidateElement bce = new BaseCandidateElement((int) DimValIndexBase.getDimValBitSetLength());
			bce.setBit(index1);
			bce.setBit(index2);
			if(metricPrecis) 
			    bce.setMetric(metric);
			else
			    bce.setMetric(1.0);
			o.inputObject.addNextCandidateElement(bce);
		    }
		}
	    }
	    // e.setMetric(0.0); //for now, we need to change this.
	    if (elementAddedflag){
		//System.err.println(e);
		partitioner.addElement(e.getNumberofDimVals() - 1, e);
	    }
	}
	o.inputObject.setPartitioner(partitioner);
	dump(partitioner);
	//System.err.println(o);
	return true;
    }
}