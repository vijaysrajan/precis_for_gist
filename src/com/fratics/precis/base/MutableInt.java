package com.fratics.precis.base;

public class MutableInt implements Comparable<MutableInt> {
    int value = 1;

    public MutableInt() {
    }

    public MutableInt(int val) {
	value = val;
    }

    public void inc() {
	++value;
    }

    public void incBy(int inc) {
	value += inc;
    }

    public int get() {
	return value;
    }

    public String toString() {
	return "" + value;
    }

    @Override
    public int compareTo(MutableInt o) {
	return this.value - o.value;
    }
}