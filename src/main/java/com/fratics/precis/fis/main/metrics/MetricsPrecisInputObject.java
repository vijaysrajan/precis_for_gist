package com.fratics.precis.fis.main.metrics;

import com.fratics.precis.exception.PrecisException;
import com.fratics.precis.fis.base.InputObject;
import com.fratics.precis.fis.base.Schema.FieldType;

public class MetricsPrecisInputObject extends InputObject {

    private static final long serialVersionUID = 6369672872079922497L;
    protected static final String DEF_VALUE = "";

    public MetricsPrecisInputObject() {
	this.countPrecis = false;
    }

    protected boolean isInitialized() {
	return (noOfFields > 0);
    }

    public void loadInputCharacteristics(Object o) throws Exception {
	int index = 0;
	String[] str = (String[]) o;
	// Check if Schema is loaded
	if (!this.isInitialized())
	    throw new PrecisException("Schema Not Loaded");
	// Schema is loaded, check if metrics index is set.
	if (this.getMetricIndex() < 0)
	    throw new PrecisException("No Metrics Field in Schema");
	for (int i = 0; i < fieldObjects.length; i++) {
	    if (fieldObjects[i].getSchemaElement().fieldType == FieldType.METRIC)
		continue;
	    index = fieldObjects[i].getSchemaElement().fieldIndex;
	    if (str[index] == null) {
		fieldObjects[i].addFieldValueBy(DEF_VALUE,
			Double.parseDouble(str[this.metricIndex]));
	    } else {
		fieldObjects[i].addFieldValueBy(str[index].trim(),
			Double.parseDouble(str[this.metricIndex]));
	    }
	}
    }

}
