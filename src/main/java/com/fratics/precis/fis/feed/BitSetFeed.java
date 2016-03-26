package com.fratics.precis.fis.feed;

import com.fratics.precis.fis.base.BaseCandidateElement;
import com.fratics.precis.fis.base.BaseFeedElement;
import com.fratics.precis.fis.base.FieldObject;
import com.fratics.precis.fis.base.PrecisProcessor;
import com.fratics.precis.fis.base.PrecisStream;
import com.fratics.precis.fis.base.ValueObject;
import com.fratics.precis.fis.feed.dimval.DimValIndex;
import com.fratics.precis.fis.feed.dimval.DimValIndexBase;
import com.fratics.precis.fis.util.PrecisConfigProperties;

/*
 * The BitSetFeed class converts the Input data Feed values to bits.
 * It also loads these bit set objects to the partition class.
 * 
 * It uses the DimValIndex class to generate the indexes for values 
 * and sets them to the resulting bit set object.
 * 
 * It is designed as a flow processor object, so it works on the input object
 * and changes the alters the state of the input object by creating & loading
 * the partitioner to the input object.
 * 
 * So any processor which comes later in the sequence will have the base feed partitioner
 * as the representative of the input feed to work with.
 * 
 * This also generates the First Stage Candidates from the values set in DimValIndex has already crossed threshold.
 * 
 */

public class BitSetFeed extends PrecisProcessor {

    private PrecisStream ps = null;

    // Takes a PrecisStream object to read a file stream (or) data stream
    // of the input data feed.
    public BitSetFeed(PrecisStream ps) {
	this.ps = ps;
    }

    public boolean initialize() throws Exception {
	return this.ps.initialize();
    }

    public boolean unInitialize() throws Exception {
	return this.ps.unInitialize();
    }

    // Process methods holds the logic of this flow processor.
    // In this case, the processor reads the input feed values in sequence.
    // verifies if they are present in DimValIndex Object.
    // If present returns the index for the incoming value from the input feed.
    // The returned index is set in a new BitSet() object and added to the
    // partitioner.
    public boolean process(ValueObject o) throws Exception {
	o.inputObject.currentStage = 1;
	String[] str = null;
	FieldObject[] fi = o.inputObject.getFieldObjects();
	BaseFeedPartitioner partitioner = new BaseFeedPartitioner(fi.length);
	boolean metricPrecis = !o.inputObject.isCountPrecis();
	boolean elementAddedflag = false;
	boolean metricGenerated = false;
	double metric = 0.0;

	// read the input stream object
	while ((str = ps.readStream()) != null) {
	    BaseFeedElement e = new BaseFeedElement(
		    DimValIndexBase.getPrecisBitSetLength());
	    elementAddedflag = false;
	    metricGenerated = false;
	    metric = 0.0;
	    for (int i = 0; i < fi.length; i++) {

		// Create the keys from input feed to check in DimValIndex.
		String tmpDim = fi[i].getSchemaElement().fieldName;
		String tmpDimVal = fi[i].getSchemaElement().fieldName
			+ PrecisConfigProperties.OUTPUT_DIMVAL_SEPERATOR
			+ str[fi[i].getSchemaElement().fieldIndex];

		// Checks if the value is present in DimValindex.
		if (DimValIndex.dimMap.containsKey(tmpDim)) {
		    if (DimValIndex.dimValMap.containsKey(tmpDimVal)) {
			// If present, add them to a bit set object.
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
			// Add the Value to the Fist Stage Candidates.
			BaseCandidateElement bce = new BaseCandidateElement(
				DimValIndexBase.getPrecisBitSetLength());
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
	    // add the element to the respective partition.
	    if (elementAddedflag) {
		// System.err.println(e);
		partitioner.addElement(e.getNumberofDimVals() - 1, e);
	    }
	}
	// Set the partitioner to the input object.
	o.inputObject.setPartitioner(partitioner);
	// Dump the contents of partitioner as a file.
	if (PrecisConfigProperties.DUMP_BITSET_FEED)
	    partitioner.dump();

	// Move the context of Precis execution to next stage (i.e) stage 2.
	o.inputObject.moveToNextStage();
	return true;
    }
}