package com.fratics.precis.base;

import java.io.Serializable;

public abstract class ResultObject implements Serializable {
    private static final long serialVersionUID = -6326248205032370705L;

    public abstract void init(int noOfFields);

    public abstract void loadResult(Object o) throws Exception;

    protected abstract boolean isInitialized();

    public abstract String toString();
}
