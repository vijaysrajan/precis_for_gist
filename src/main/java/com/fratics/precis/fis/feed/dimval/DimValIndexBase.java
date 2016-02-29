package com.fratics.precis.fis.feed.dimval;

import java.util.Map;
import java.util.TreeMap;

import com.fratics.precis.fis.base.PrecisProcessor;

/*
 * Indexes are created for all the dims and values that cross the threshold in stage 1.
 * 
 * We have 4 maps totally, they are
 * 	1) dim map
 * 	2) dim_val map
 * 	3) reverse dim map
 * 	4) reverse dim_val map
 * 
 * The format we used for dim_val map element is "dim^Bvalue" -> Index.
 * 
 * Creating MetricDimValIndex completes the first stage of Precis.
 * 
 */

public abstract class DimValIndexBase extends PrecisProcessor {
    public static Map<String, Integer> dimMap = new TreeMap<String, Integer>();
    public static Map<Integer, String> revDimMap = new TreeMap<Integer, String>();
    public static Map<String, Integer> dimValMap = new TreeMap<String, Integer>();
    public static Map<Integer, String> revDimValMap = new TreeMap<Integer, String>();

    public static long getDimValBitSetLength() {
	return dimMap.size() + dimValMap.size();
    }

    public static String dumpIndexes() {
	return dimMap + "\n" + dimValMap + "\n" + revDimMap + "\n"
		+ revDimValMap + "\n";
    }

}
