package com.fratics.precis.base;

public abstract class PrecisProcessor extends PrecisBase {
    public abstract boolean process(ValueObject o) throws Exception;
}
