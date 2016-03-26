package com.fratics.precis.fis.base;

import java.util.BitSet;

/*
 * Candidates Generated from Precis execution are captured in this holder class.
 * The candidate element class is a simple bit representation of a candidate, along with 
 * metric and threshold passed flag.
 * 
 * This class inherits from the Base Feed Element class, which is the actual bit
 * representation of a record in the input feed. 
 * 
 */

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

    public int hashCode() {
	return this.b.hashCode();
    }

    public boolean equals(Object b) {
	if (b instanceof BaseCandidateElement) {
	    BaseCandidateElement e = (BaseCandidateElement) b;
	    return this.b.equals(e.getBitSet());
	}
	return false;
    }

    public String toString() {
	return this.b.toString() + "," + metric;
    }
}
