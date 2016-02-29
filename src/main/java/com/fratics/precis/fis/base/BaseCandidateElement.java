package com.fratics.precis.fis.base;

import java.util.BitSet;

public class BaseCandidateElement extends BaseFeedElement {

    public BaseCandidateElement() {
    }

    public BaseCandidateElement(int size) {
	this.b = new BitSet(size);
    }

    public BaseCandidateElement(BaseFeedElement e) {
	this.b = (BitSet) e.getBitSet().clone();
	this.metric = e.getMetric();
    }

    public BaseCandidateElement(BitSet b, double metric) {
	this.b = b;
	this.metric = metric;
    }

    public void incrMetric() {
	this.metric++;
    }

    public void incrMetricBy(double metric) {
	this.metric += metric;
    }

    public boolean equals(BaseCandidateElement b) {
	return this.b == b.getBitSet();
    }

    public String toString() {
	return this.b.toString() + "," + metric;
    }
}
