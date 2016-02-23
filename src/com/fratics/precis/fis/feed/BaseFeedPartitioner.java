package com.fratics.precis.fis.feed;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BaseFeedPartitioner {
    public Map<Integer, ArrayList<BaseFeedElement>> partitionMap = new HashMap<Integer, ArrayList<BaseFeedElement>>();

    public void addElement(int index, BaseFeedElement e) {
	if (partitionMap.containsKey(index)) {
	    partitionMap.get(index).add(e);
	    return;
	}
	ArrayList<BaseFeedElement> al = new ArrayList<BaseFeedElement>();
	al.add(e);
	partitionMap.put(index, al);
    }

    public String toString() {
	String str = "";
	Iterator<Integer> i = partitionMap.keySet().iterator();
	while (i.hasNext()) {
	    Integer l = i.next();
	    ArrayList<BaseFeedElement> al = partitionMap.get(l);
	    str = str + "PN ==> " + l + ", NS ==> " + al.size() + ", ==> {"
		    + al.toString() + "}\n";
	}
	return str;
    }
}
