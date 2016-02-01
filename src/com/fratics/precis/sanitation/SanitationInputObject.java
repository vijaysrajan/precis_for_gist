package com.fratics.precis.sanitation;

import java.util.Arrays;

import com.fratics.precis.base.FieldObject;
import com.fratics.precis.base.InputObject;

public class SanitationInputObject extends InputObject {
    private static final long serialVersionUID = 6369672872079922497L;
    protected static final String DEF_VALUE = "";

    protected boolean isInitialized() {
	return (noOfFields > 0);
    }

    public String toString() {
	return "No of Records :: " + this.getNoOfValues() + "\n"
		+ Arrays.toString(fieldObjects) + "\n";
    }

    public void init(int noOfFields) {
	this.noOfFields = noOfFields;
	fieldObjects = new FieldObject[noOfFields];
	for (int i = 0; i < noOfFields; i++) {
	    fieldObjects[i] = new FieldObject();
	    fieldObjects[i].setFieldIndex(i);
	    fieldObjects[i].setFieldName("" + i); // Change Later Accordingly
	}
    }

    public void loadInput(Object o) throws Exception {
	String[] str = (String[]) o;
	if (!this.isInitialized())
	    this.init(str.length);
	// System.err.println("String Array ==> " + Arrays.toString(str));
	// Load Values to the Value Object.
	for (int i = 0; i < str.length; i++) {
	    if (str[i] == null) {
		fieldObjects[i].addFieldValue(DEF_VALUE);
	    } else {
		fieldObjects[i].addFieldValue(str[i].trim());
	    }
	}
    }

}
