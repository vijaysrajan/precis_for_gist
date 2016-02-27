package com.fratics.precis.fis.feed;

import java.util.BitSet;

public class BaseFeedElement {

    protected BitSet b;
    protected double metric;

    public BaseFeedElement() {
	b = new BitSet();
    }

    public BaseFeedElement(int noOfBits) {
	b = new BitSet(noOfBits);
    }

    public BitSet getBitSet() {
        return b;
    }

    public void setBitSet(BitSet b) {
        this.b = b;
    }

    public BitSet and(BaseFeedElement b) {
	BitSet bs = (BitSet) this.b.clone();
	bs.and(b.getBitSet());
	return bs;
    }

    public BitSet or(BaseFeedElement b) {
	BitSet bs = (BitSet) this.b.clone();
	bs.or(b.getBitSet());
	return bs;   
    }

    public BitSet xor(BaseFeedElement b) {
	BitSet bs = (BitSet) this.b.clone();
	bs.xor(b.getBitSet());
	return bs;
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

    
    public void setBit(int e) {
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
