package com.fratics.precis.fis.app;

import com.fratics.precis.base.InputObject;
import com.fratics.precis.base.Schema.FieldType;
import com.fratics.precis.exception.PrecisException;

public class PrecisInputObject extends InputObject {

    private static final long serialVersionUID = 6369672872079922497L;
    protected static final String DEF_VALUE = "";

    protected boolean isInitialized() {
	return (noOfFields > 0);
    }

    public String toString() {
	return "PrecisInputObject";
    }

    public void loadInput(Object o) throws Exception {
	int index = 0;
	String[] str = (String[]) o;
	if (!this.isInitialized())
	    throw new PrecisException("Schema Not Loaded");
	this.countPrecis = false;
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
