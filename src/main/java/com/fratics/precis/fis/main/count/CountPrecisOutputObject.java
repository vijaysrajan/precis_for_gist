package com.fratics.precis.fis.main.count;

import com.fratics.precis.fis.base.OutputObject;

public class CountPrecisOutputObject extends OutputObject {
    private static final long serialVersionUID = 8259727611192034540L;

    public String toString() {
	return "MetricsPrecisOutputObject = {}\n";
    }

    public void loadResult(Object o) throws Exception {
    }

    public void init(int noOfFields) {
    }

    protected boolean isInitialized() {
	return false;
    }

}
