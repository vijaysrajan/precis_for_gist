package com.fratics.precis.base;

import java.io.Serializable;

public abstract class InputObject implements Serializable {
    private static final long serialVersionUID = -6326248205037370805L;
    protected int noOfFields = 0;
    private long noOfValues = 0;
    protected FieldObject[] fieldObjects = null;

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

    public abstract void loadSchema(String[] str);

    public abstract void loadInput(Object o) throws Exception;

    protected abstract boolean isInitialized();

    public abstract String toString();
}
