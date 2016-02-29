package com.fratics.precis.fis.base;

import java.util.HashMap;
import java.util.Map;

import com.fratics.precis.fis.base.Schema.SchemaElement;

public class FieldObject {

    private SchemaElement schElement;

    private Map<String, MutableDouble> map = new HashMap<String, MutableDouble>();

    public SchemaElement getSchemaElement() {
	return schElement;
    }

    public Map<String, MutableDouble> getMap() {
	return map;
    }

    public void setSchemaElement(SchemaElement schElement) {
	this.schElement = schElement;
    }

    public int getNumberOfUniques() {
	return map.keySet().size();
    }

    public void addFieldValue(String key) {
	MutableDouble value = map.get(key);
	if (value == null) {
	    value = new MutableDouble();
	    map.put(key, value);
	} else {
	    value.inc();
	}
    }

    public void addFieldValueBy(String key, double metric) {
	MutableDouble value = map.get(key);
	if (value == null) {
	    value = new MutableDouble(metric);
	    map.put(key, value);
	} else {
	    value.incBy(metric);
	}
    }

    public String toString() {
	String str = "fieldName :: " + this.schElement.fieldName
		+ " fieldIndex :: " + this.schElement.fieldIndex
		+ " Map Values ==>{ " + this.map.toString() + " } \n";
	return str;
    }

}
