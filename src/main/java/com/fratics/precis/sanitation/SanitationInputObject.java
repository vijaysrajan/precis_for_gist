package com.fratics.precis.sanitation;

import java.util.Arrays;

import com.fratics.precis.base.InputObject;
import com.fratics.precis.dimval.DimValIndex;
import com.fratics.precis.exception.PrecisException;

public class SanitationInputObject extends InputObject {
    private static final long serialVersionUID = 6369672872079922497L;
    protected static final String DEF_VALUE = "";

    protected boolean isInitialized() {
	return (noOfFields > 0);
    }

    public String toString() {
	return "No of Records :: " + this.getNoOfValues() + "\n" +
		"Field Objects :: " + Arrays.toString(fieldObjects) + "\n\n" +
		"Current Candidate Stage :: " + this.currentStage + "\n" + 
		"Prev Candidates Generated :: " + this.prevCandidateMap.values() + "\n" +
		"Candidates Generated :: " + this.currCandidateMap.values()  + "\n" + 
		"DimVal Indexes :: " +  DimValIndex.dumpIndexes() +  "\n";
    }

    public void loadInput(Object o) throws Exception {
	int index = 0;
	String[] str = (String[]) o;
	if (!this.isInitialized())
	    throw new PrecisException("Schema Not Loaded");
	// System.err.println("String Array ==> " + Arrays.toString(str));
	// Load Values to the Value Object.
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
