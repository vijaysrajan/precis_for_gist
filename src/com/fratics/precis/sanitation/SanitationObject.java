package com.fratics.precis.sanitation;

import java.util.Arrays;

import com.fratics.precis.base.ValueObject;

public class SanitationObject extends ValueObject {

    private static final long serialVersionUID = -8200835187272019899L;
    public String toString() { return "No of Records :: " + this.getNoOfValues() + "\n" + Arrays.toString(fieldObjects) + "\n"; }

    protected void initVO(int noOfFields) {
	this.noOfFields = noOfFields;
	fieldObjects = new FieldObject[noOfFields];
	for (int i = 0; i < noOfFields; i++) {
	    fieldObjects[i] = new FieldObject();
	    fieldObjects[i].setFieldIndex(i);
	    fieldObjects[i].setFieldName("" + i); // Change Later Accordingly
	}
    }
 
    public void loadDataObjects(String[] str) throws Exception {
	if (!this.isInitialized()) this.initVO(str.length);
	//System.err.println("String Array ==> " + Arrays.toString(str));
	//Load Values to the Value Object.
	for (int i = 0; i < str.length; i++) {
	    if (str[i] == null) {
		fieldObjects[i].addFieldValue(DEF_VALUE);
	    } else {
		fieldObjects[i].addFieldValue(str[i].trim());
	    }
	}
    }

}
