package com.fratics.precis.candidategeneration;

import java.util.BitSet;

import com.fratics.precis.fis.feed.BaseFeedElement;

public class BaseCandidateElement extends BaseFeedElement {

    private boolean passedThreshold = false;
    
    public BaseCandidateElement(){}
    
    public BaseCandidateElement(int size){
	this.b = new BitSet(size);
    }
    
    public BaseCandidateElement(BaseFeedElement e){
	this.b = (BitSet) e.getBitSet().clone();
	this.metric = e.getMetric();
    }

    public BaseCandidateElement(BitSet b, double metric) {
	this.b = b;
	this.metric = metric;
    }

    public boolean isPassedThreshold() {
        return passedThreshold;
    }

    public void setPassedThreshold(boolean passedThreshold) {
        this.passedThreshold = passedThreshold;
    }

    
    public void incrMetric(double metric){
	this.metric += metric;
    }

    public boolean equals(BaseCandidateElement b){
	return this.b == b.getBitSet();
    }
    
    public String toString() {
	return this.b.toString() + "," + metric + "," + this.passedThreshold;
    }
}
