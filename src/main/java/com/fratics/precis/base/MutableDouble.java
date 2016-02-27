package com.fratics.precis.base;

public class MutableDouble implements Comparable<MutableDouble> {
    double value = 1.0;

    public MutableDouble() {
    }

    public MutableDouble(double val) {
	value = val;
    }

    public void inc() {
	++value;
    }

    public void incBy(double inc) {
	value += inc;
    }

    public double get() {
	return value;
    }

    public String toString() {
	return "" + value;
    }

    public int compareTo(MutableDouble o) {
	return (int) ((this.value - o.value) * 100);
    }
}