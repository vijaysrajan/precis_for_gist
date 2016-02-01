package com.fratics.precis.base;

import java.util.HashMap;
import java.util.Map;

class MutableInt {
    int value = 1;

    public void inc() {
	++value;
    }

    public int get() {
	return value;
    }

    public String toString() {
	return "" + value;
    }
}

public class FieldObject {

    private int fieldIndex;
    private String fieldName;
    private Map<String, MutableInt> map = new HashMap<String, MutableInt>();

    public int getFieldIndex() {
	return fieldIndex;
    }

    public void setFieldIndex(int fieldIndex) {
	this.fieldIndex = fieldIndex;
    }

    public String getFieldName() {
	return fieldName;
    }

    public void setFieldName(String fieldName) {
	this.fieldName = fieldName;
    }

    public int getNumberOfUniques() {
	return map.keySet().size();
    }

    public void addFieldValue(String key) {
	MutableInt value = map.get(key);
	if (value == null) {
	    value = new MutableInt();
	    map.put(key, value);
	} else {
	    value.inc();
	}
    }

    public String toString() {
	String str = "fieldName :: " + this.fieldName + " fieldIndex :: "
		+ this.fieldIndex + " Map Values ==>{ " + this.map.toString()
		+ " } \n";
	return str;
    }

}