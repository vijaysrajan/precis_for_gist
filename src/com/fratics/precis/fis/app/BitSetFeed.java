package com.fratics.precis.fis.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/*
 class ElementA{
 public long dim_val;
 public String toString(){ return Long.toString(dim_val); }
 }

 class ElementList{
 private List<ElementA> elementList = new ArrayList<ElementA>();
 private long metric;
 public void addElement(ElementA e) { this.elementList.add(e); }
 public void setMetric(long metric) { this.metric = metric; }
 public long getMetric() { return metric; }
 public int getNumberofElements() {  return elementList.size(); }
 public String toString() { return elementList.toString() + "," + metric; }
 }
 */

class Element {
    private BitSet b = new BitSet();
    private double metric;

    public void addElement(int e) {
	this.b.set(e);
    }

    public void setMetric(double metric) {
	this.metric = metric;
    }

    public double getMetric() {
	return metric;
    }

    public int getNumberofDimVals() {
	return this.b.cardinality() / 2;
    }

    public String toString() {
	return this.b.toString() + "," + metric;
    }
}

final class BaseFeedPartitioner {
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

public class BitSetFeed {

    public BaseFeedPartitioner partitioner = new BaseFeedPartitioner();

    private void populateLine(String str) {
	// parseLine
	Element el = new Element();
	int beginIndex = str.indexOf('{') + 1;
	int endIndex = str.indexOf('}');
	String[] strArray = str.substring(beginIndex, endIndex).split(",");
	for (int i = 0; i < strArray.length; i++) {
	    el.addElement(Integer.parseInt(strArray[i].trim()));
	}
	el.setMetric(Double.parseDouble(str.substring(endIndex + 1).trim()));
	partitioner.addElement(el.getNumberofDimVals(), el);
    }

    public void populateFeed() throws Exception {
	try {
	    BufferedReader br = new BufferedReader(new FileReader(
		    "./data/bitsetDatafile"));
	    String thisLine = null;
	    while ((thisLine = br.readLine()) != null) {
		populateLine(thisLine);
	    }
	    br.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public void dump() {
	try {
	    PrintWriter pw = new PrintWriter(new File("./data/out.txt"));
	    pw.append(partitioner.toString());
	    pw.flush();
	    pw.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public static void main(String[] args) {
	BitSetFeed bsf = new BitSetFeed();
	try {
	    bsf.populateFeed();
	    bsf.dump();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
