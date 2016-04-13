package com.fratics.precis.fis.util;

import java.util.BitSet;
import java.util.Comparator;

public class BitSetComparator implements Comparator<BitSet> {
    public int compare(BitSet o1, BitSet o2) {
	return Integer.valueOf(o1.cardinality()).compareTo(o2.cardinality());
    }
}