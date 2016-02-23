package com.fratics.precis.fis.feed;

import java.util.BitSet;

public class BaseFeedElement {

    private BitSet b;
    private double metric;

    public BaseFeedElement() {
	b = new BitSet();
    }

    public BaseFeedElement(int noOfBits) {
	b = new BitSet(noOfBits);
    }

    public void and(BitSet b) {
	this.b.and(b);
    }

    public void or(BitSet b) {
	this.b.or(b);
    }

    public void xor(BitSet b) {
	this.b.xor(b);
    }

    public void addElement(int e) {
	this.b.set(e);
    }

    public void setMetric(double metric) {
	this.metric = metric;
    }

    public double getMetric() {
	return metric;
    }

    public int getCardinality() {
	return this.b.cardinality();
    }

    public int getNumberofDimVals() {
	return this.b.cardinality() / 2;
    }

    public String toString() {
	return this.b.toString() + "," + metric;
    }
}
