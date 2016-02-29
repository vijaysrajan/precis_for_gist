package com.precis.bitSetEvaluation;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;

public class CanditateGeneratorStageKGT2 {

    private int stage = 0;
    private int cardinality = 0;
    private HashMap<BitSet, ArrayList<BitSet>> candidatePartition;
    private HashSet<BitSet> candidateSet;

    public CanditateGeneratorStageKGT2(int stage, HashSet<BitSet> candidateSet) {
	// TODO Auto-generated constructor stub
	/*
	 * if (stage <= 2) { System.out.println("Incorrect Stage...."); } else {
	 */this.stage = stage;
	cardinality = (stage - 1) * 2;
	candidatePartition = new HashMap<BitSet, ArrayList<BitSet>>();
	this.candidateSet = candidateSet;
	// }
	addSet(candidateSet);
    }

    /**
     * Function to add
     * 
     * @return
     */
    public void add(BitSet bitSet) {

	BitSet intermediateBS = (BitSet) bitSet.clone();
	ArrayList<BitSet> newBS = null;
	int dimIndex = 0, metIndex = 0;

	int nextBit = -1;

	for (int i = 0; i < cardinality; i++) {
	    nextBit = bitSet.nextSetBit(nextBit + 1);
	    if (i == (cardinality / 2) - 1) {
		dimIndex = nextBit;
	    } else if (i == cardinality - 1) {
		metIndex = nextBit;
	    }
	}

	intermediateBS.clear(dimIndex);
	intermediateBS.clear(metIndex);

	if (candidatePartition.containsKey(intermediateBS)) {
	    newBS = candidatePartition.get(intermediateBS);
	    newBS.add(bitSet);
	} else {
	    newBS = new ArrayList<BitSet>();
	    newBS.add(bitSet);
	    candidatePartition.put(intermediateBS, newBS);
	}
    }

    public static void main(String args[]) {
	BitSet bits1 = new BitSet(6);
	bits1.set(0);
	bits1.set(1);
	bits1.set(2);
	bits1.set(7);
	bits1.set(13);
	bits1.set(17);

	BitSet bits2 = new BitSet(6);
	bits2.set(0);
	bits2.set(1);
	bits2.set(2);
	bits2.set(7);
	bits2.set(13);
	bits2.set(17);
	CanditateGeneratorStageKGT2 obj = new CanditateGeneratorStageKGT2(4,
		null);
	System.out.println(bits1.toString());
	obj.add(bits1);
	obj.add(bits2);

    }

    public void addSet(HashSet<BitSet> candidateSet) {
	for (BitSet bitSet : candidateSet) {
	    add(bitSet);
	}
    }

    public HashMap<BitSet, ArrayList<BitSet>> getCandidatePartition() {
	return candidatePartition;
    }

    public boolean isPresent1(BitSet bs) {
	boolean result = false;
	int dimIndex = -1, metIndex = 0, j = 0;
	// System.out.println("Size::"+candidateSet.size());
	for (int i = 0; i < stage; i++) {
	    dimIndex = bs.nextSetBit(dimIndex + 1);
	    if (metIndex == 0) {
		for (j = 0; j < stage; j++) {
		    metIndex = bs.nextSetBit(metIndex + 1);
		}
		metIndex = bs.nextSetBit(metIndex + 1);
	    } else {
		metIndex = bs.nextSetBit(metIndex + 1);
	    }

	    bs.clear(dimIndex);
	    bs.clear(metIndex);

	    System.out.println("BS to check:" + bs.toString());
	    if (candidateSet.contains(bs)) {
		System.out.println("BS present:" + bs.toString());
		result = true;
		bs.set(dimIndex);
		bs.set(metIndex);
	    } else {
		result = false;
		System.out.println("dimIndex=" + dimIndex + "; metIndex="
			+ metIndex);
		System.out.println("BS Not present::" + bs.toString());
		bs.set(dimIndex);
		bs.set(metIndex);
		break;
	    }

	}
	return result;
    }

    public boolean isPresent(BitSet bs1) {

	// get the stage_num_const
	// check the first set bit
	// loop through stage_num_const
	// clone the bitset
	//
	boolean result = true;
	int valIndex = -1;
	for (int i = 0; i <= stage; i++) {
	    valIndex = bs1.nextSetBit(valIndex + 1);
	}

	int dimIndex = -1;
	for (int i = 0; i < stage; i++) {
	    dimIndex = bs1.nextSetBit(dimIndex + 1);
	    if (i != 0) {
		valIndex = bs1.nextSetBit(valIndex + 1);
	    }
	    bs1.clear(dimIndex);
	    bs1.clear(valIndex);

	    if (candidateSet.contains(bs1)) {
		System.out.println("BS present:" + bs1.toString());
		bs1.set(dimIndex);
		bs1.set(valIndex);
	    } else {
		result = false;
		System.out.println("dimIndex=" + dimIndex + "; metIndex="
			+ valIndex);
		System.out.println("BS Not present::" + bs1.toString());
		bs1.set(dimIndex);
		bs1.set(valIndex);
		break;
	    }
	}
	return result;
    }

}
