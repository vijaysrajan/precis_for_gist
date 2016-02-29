package com.fratics.precis.fis.feed.dimval;

import java.util.Iterator;
import java.util.Map;

import com.fratics.precis.fis.base.FieldObject;
import com.fratics.precis.fis.base.MutableDouble;
import com.fratics.precis.fis.base.ValueObject;

public class DimValIndex extends DimValIndexBase {

    private double threshold = 0.0;

    public DimValIndex(double threshold) {
	this.threshold = threshold;
    }

    public boolean process(ValueObject o) throws Exception {
	int valIndex = 0;
	int dimIndex = 0;
	o.inputObject.setThreshold(this.threshold);
	FieldObject[] fi = o.inputObject.getFieldObjects();
	for (int i = 0; i < fi.length; i++) {
	    Map<String, MutableDouble> map = fi[i].getMap();
	    Iterator<String> it = map.keySet().iterator();
	    while (it.hasNext()) {
		String key = it.next();
		MutableDouble val = map.get(key);
		if (val.get() >= threshold) {
		    if (!dimMap.containsKey(fi[i].getSchemaElement().fieldName)) {
			dimMap.put(fi[i].getSchemaElement().fieldName, dimIndex);
			revDimMap.put(dimIndex,
				fi[i].getSchemaElement().fieldName);
			dimIndex++;
		    }
		    if (!dimValMap
			    .containsKey(fi[i].getSchemaElement().fieldName
				    + dimValDelimiter + key)) {
			dimValMap.put(fi[i].getSchemaElement().fieldName
				+ dimValDelimiter + key, valIndex);
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
	    Integer m = dimValMap.get(key);
	    m += inc;
	    dimValMap.put(key, m);
	    revDimValMap.put(m, key);
	}
	return true;
    }
}
