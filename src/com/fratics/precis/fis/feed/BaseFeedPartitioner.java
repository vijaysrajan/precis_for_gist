package com.fratics.precis.fis.feed;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BaseFeedPartitioner {
    public Map<Integer, ArrayList<Element>> partitionMap = new HashMap<Integer, ArrayList<Element>>();

    public void addElement(int index, Element e) {
	if (partitionMap.containsKey(index)) {
	    partitionMap.get(index).add(e);
	    return;
	}
	ArrayList<Element> al = new ArrayList<Element>();
	al.add(e);
	partitionMap.put(index, al);
    }

    public String toString() {
	String str = "";
	Iterator<Integer> i = partitionMap.keySet().iterator();
	while (i.hasNext()) {
	    Integer l = i.next();
	    ArrayList<Element> al = partitionMap.get(l);
	    str = str + "PN ==> " + l + ", NS ==> " + al.size() + ", ==> {"
		    + al.toString() + "}\n";
	}
	return str;
    }
}
