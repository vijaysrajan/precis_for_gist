package com.fratics.precis.base;

import java.io.Serializable;
import com.fratics.precis.sanitation.FieldObject;

public abstract class ValueObject implements Serializable {
    
    private static final long serialVersionUID = 1L;
    protected static final String DEF_VALUE = "";
    protected int noOfFields = 0;
    private long noOfValues = 0;
    protected FieldObject[] fieldObjects = null;

    public FieldObject[] getFieldObjects() { return fieldObjects; }
    protected abstract void initVO(int noOfFields);
    public abstract String toString();
    public void setNoOfValues(long noOfValues) { this.noOfValues = noOfValues; }
    public long getNoOfValues() { return this.noOfValues; }
    public void incNoOfValues(long inc) { noOfValues = noOfValues + inc; }
    public abstract void loadDataObjects(String[] str) throws Exception;
    protected boolean isInitialized() { return (noOfFields > 0); }
}
