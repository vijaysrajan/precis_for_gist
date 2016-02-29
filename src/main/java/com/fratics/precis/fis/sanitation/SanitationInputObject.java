package com.fratics.precis.fis.sanitation;

import com.fratics.precis.exception.PrecisException;
import com.fratics.precis.fis.base.InputObject;

public class SanitationInputObject extends InputObject {
    private static final long serialVersionUID = 6369672872079922497L;
    protected static final String DEF_VALUE = "";

    protected boolean isInitialized() {
	return (noOfFields > 0);
    }

    public String toString() {
	return "SanitationInputObject";
    }

    public void loadInput(Object o) throws Exception {
	int index = 0;
	String[] str = (String[]) o;
	if (!this.isInitialized())
	    throw new PrecisException("Schema Not Loaded");
	for (int i = 0; i < fieldObjects.length; i++) {
	    index = fieldObjects[i].getSchemaElement().fieldIndex;
	    if (str[index] == null) {
		fieldObjects[i].addFieldValue(DEF_VALUE);
	    } else {
		fieldObjects[i].addFieldValue(str[index].trim());
	    }
	}
    }

}
