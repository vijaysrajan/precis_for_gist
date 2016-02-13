package com.fractics.precis.dimval;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.fratics.precis.base.FieldObject;
import com.fratics.precis.base.MutableInt;
import com.fratics.precis.base.ValueObject;

public class DimValIndex {
    
    public static int threshold = 500;
    private Map<String, MutableInt> dimMap = new HashMap<String, MutableInt>();
    private Map<MutableInt, String> revDimMap = new HashMap<MutableInt, String>();
    private Map<String, MutableInt> dimValMap = new HashMap<String, MutableInt>();
    private Map<MutableInt, String> revDimValMap = new HashMap<MutableInt, String>();

    public DimValIndex() {
    }

    public void applyThresholds(ValueObject o) {
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
		    dimValMap.put(
			    fi[i].getFieldName() + Character.toString('\002')
				    + val.get(), new MutableInt(valIndex));
		    revDimValMap.put(new MutableInt(valIndex),
			    fi[i].getFieldName() + Character.toString('\002')
				    + val.get());
		    valIndex++;
		}
	    }
	}
    }

    public String toString() {
	// System.err.println(dimValMap.size());
	// System.err.println(revDimValMap.size());
	return dimMap + "\n" + dimValMap + "\n" + revDimMap + "\n"
		+ revDimValMap + "\n";

    }
}
