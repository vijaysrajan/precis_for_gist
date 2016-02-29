package com.fratics.precis.fis.base;

public abstract class PrecisStream extends PrecisBase {
    private long noOfLines = 0;
    private String streamName = null;

    public PrecisStream(String streamName) {
	this.streamName = streamName;
    }

    public String getStreamName() {
	return streamName;
    }

    public void setStreamName(String streamName) {
	this.streamName = streamName;
    }

    public abstract String[] readStream() throws Exception;

    protected void incLines() {
	++noOfLines;
    }

    public long getNoOfLines() {
	return this.noOfLines;
    }
}
