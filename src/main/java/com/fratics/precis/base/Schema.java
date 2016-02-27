package com.fratics.precis.base;

import java.util.TreeSet;

public class Schema {

    public static enum FieldType {
	DIMENSION, METRIC, INVALID
    };

    public class SchemaElement implements Comparable<SchemaElement> {
	public SchemaElement(String fieldName, int fieldIndex,
		FieldType fieldType) {
	    this.fieldName = fieldName;
	    this.fieldIndex = fieldIndex;
	    this.fieldType = fieldType;
	}

	public String fieldName;
	public int fieldIndex;
	public FieldType fieldType;
	
	public int compareTo(SchemaElement o) {
	    return this.fieldName.compareTo(o.fieldName);
	}
    };

    private TreeSet<SchemaElement> list = new TreeSet<SchemaElement>();

    public int getNoOfFields() {
	return list.size();
    }

    public void addSchemaElement(String fieldName, int fieldIndex,
	    FieldType fieldType) {
	addSchemaElement(new SchemaElement(fieldName, fieldIndex, fieldType));
    }

    public void addSchemaElement(SchemaElement e) {
	list.add(e);
    }

    public TreeSet<SchemaElement> getSchemaList() {
	return list;
    }
}
