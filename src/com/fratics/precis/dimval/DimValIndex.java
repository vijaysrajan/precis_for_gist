package com.fratics.precis.dimval;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.fratics.precis.base.FieldObject;
import com.fratics.precis.base.MutableInt;
import com.fratics.precis.base.PrecisProcessor;
import com.fratics.precis.base.ValueObject;

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
 * Creating DimValIndex completes the first stage of Precis.
 * 
 */

public class DimValIndex extends PrecisProcessor {

    public static int threshold = 500;
    public static String dimValDelimiter = Character.toString('\002'); // ^B
    public static Map<String, MutableInt> dimMap = new TreeMap<String, MutableInt>();
    public static Map<MutableInt, String> revDimMap = new TreeMap<MutableInt, String>();
    public static Map<String, MutableInt> dimValMap = new TreeMap<String, MutableInt>();
    public static Map<MutableInt, String> revDimValMap = new TreeMap<MutableInt, String>();

    public static long getDimValBitSetLength() {
	return dimMap.size() + dimValMap.size();
    }

    public static String dumpIndexes() {
	return dimMap + "\n" + dimValMap + "\n" + revDimMap + "\n"
		+ revDimValMap + "\n";
    }

    @Override
    public boolean process(ValueObject o) throws Exception {
	int valIndex = 0;
	int dimIndex = 0;
	FieldObject[] fi = o.inputObject.getFieldObjects();
	for (int i = 0; i < fi.length; i++) {
	    Map<String, MutableInt> map = fi[i].getMap();
	    Iterator<String> it = map.keySet().iterator();
	    while (it.hasNext()) {
		String key = it.next();
		MutableInt val = map.get(key);
		if (val.get() >= threshold) {
		    if (!dimMap.containsKey(fi[i].getFieldName())) {
			dimMap.put(fi[i].getFieldName(), new MutableInt(
				dimIndex));
			revDimMap.put(new MutableInt(dimIndex),
				fi[i].getFieldName());
			dimIndex++;
		    }
		    if (!dimValMap.containsKey(fi[i].getFieldName()
			    + dimValDelimiter + key)) {
			dimValMap.put(fi[i].getFieldName() + dimValDelimiter
				+ key, new MutableInt(valIndex));
			valIndex++;
		    }
		}
	    }
	}
	// inc dimValMap by dim_size.
	int inc = dimMap.size();
	Iterator<String> it = dimValMap.keySet().iterator();
	while (it.hasNext()) {
	    String key = it.next();
	    MutableInt m = dimValMap.get(key);
	    m.incBy(inc);
	    dimValMap.put(key, m);
	    revDimValMap.put(m, key);
	}
	return true;
    }
}
