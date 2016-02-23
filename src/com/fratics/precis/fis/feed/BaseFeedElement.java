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

    public BitSet and(BitSet b) {
	BitSet bs = (BitSet) this.b.clone();
	bs.and(b);
	return bs;
    }

    public BitSet or(BitSet b) {
	BitSet bs = (BitSet) this.b.clone();
	bs.or(b);
	return bs;   }

    public BitSet xor(BitSet b) {
	BitSet bs = (BitSet) this.b.clone();
	bs.xor(b);
	return bs;
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
