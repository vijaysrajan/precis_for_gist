package com.fratics.precis.base;

import java.util.ArrayList;

public class Schema {

    public static enum FieldType {
	DIMENSION, METRIC, INVALID
    };

    public class SchemaElement {
	public SchemaElement(String fieldName, int fieldIndex,
		FieldType fieldType) {
	    this.fieldName = fieldName;
	    this.fieldIndex = fieldIndex;
	    this.fieldType = fieldType;
	}

	public String fieldName;
	public int fieldIndex;
	public FieldType fieldType;
    };

    private ArrayList<SchemaElement> list = new ArrayList<SchemaElement>();

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

    public ArrayList<SchemaElement> getSchemaList() {
	return list;
    }
}
