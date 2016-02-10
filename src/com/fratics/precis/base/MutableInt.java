package com.fratics.precis.base;

public class MutableInt {
    int value = 1;

    public void inc() {
	++value;
    }

    public int get() {
	return value;
    }

    public String toString() {
	return "" + value;
    }
}