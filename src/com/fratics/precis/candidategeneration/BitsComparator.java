package com.fratics.precis.candidategeneration;

import java.util.BitSet;
import java.util.Comparator;

public class BitsComparator implements Comparator<BitSet> {
    public int compare (BitSet b1, BitSet b2) {
	BitSet b3 = (BitSet) b1.clone();
	b3.xor(b2);
	return b3.nextSetBit(0) - b1.nextSetBit(0);
    }
}