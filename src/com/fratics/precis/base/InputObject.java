package com.fratics.precis.base;

import java.io.Serializable;
import java.util.ArrayList;

import com.fratics.precis.base.Schema.SchemaElement;

public abstract class InputObject implements Serializable {

    private static final long serialVersionUID = -6326248205037370805L;
    protected int noOfFields = 0;
    private long noOfValues = 0;
    protected FieldObject[] fieldObjects = null;
    private boolean countPrecis = true;

    public boolean isCountPrecis() {
	return countPrecis;
    }

    public void setCountPrecis(boolean countPrecis) {
	this.countPrecis = countPrecis;
    }

    private int metricIndex;

    public int getMetricIndex() {
	return metricIndex;
    }

    public void setMetricIndex(int metricIndex) {
	this.metricIndex = metricIndex;
    }

    public int getNoOfFields() {
	return noOfFields;
    }

    public void setNoOfFields(int noOfFields) {
	this.noOfFields = noOfFields;
    }

    public FieldObject[] getFieldObjects() {
	return fieldObjects;
    }

    public void setNoOfValues(long noOfValues) {
	this.noOfValues = noOfValues;
    }

    public long getNoOfValues() {
	return this.noOfValues;
    }

    public void incNoOfValues(long inc) {
	noOfValues = noOfValues + inc;
    }

    public void loadSchema(Schema sch) {
	// need to alter loadSchema in case of Metrics.
	this.noOfFields = sch.getNoOfFields();
	fieldObjects = new FieldObject[noOfFields];
	ArrayList<SchemaElement> list = sch.getSchemaList();
	for (int i = 0; i < noOfFields; i++) {
	    fieldObjects[i] = new FieldObject();
	    fieldObjects[i].setSchemaElement(list.get(i));
	}
    }

    public abstract void loadInput(Object o) throws Exception;

    protected abstract boolean isInitialized();

    public abstract String toString();
}
