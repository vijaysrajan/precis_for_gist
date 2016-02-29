package com.fratics.precis.fis.base;

import java.io.Serializable;

public class ValueObject implements Serializable {
    private static final long serialVersionUID = 1590842569304698182L;
    public InputObject inputObject = null;
    public OutputObject resultObject = null;

    public String toString() {
	return inputObject.toString() + "\n" + resultObject.toString();
    }
}
