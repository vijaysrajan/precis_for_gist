package com.fratics.precis.base;

public abstract class PrecisStream extends PrecisBase {
    private long noOfLines = 0;
    public abstract String[] readStream() throws Exception;
    protected void incLines() { ++noOfLines; }
    public long getNoOfLines() { return this.noOfLines; }
}
