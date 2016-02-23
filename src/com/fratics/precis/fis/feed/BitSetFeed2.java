package com.fratics.precis.fis.feed;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

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

public class BitSetFeed2 {

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
	BitSetFeed2 bsf = new BitSetFeed2();
	try {
	    bsf.populateFeed();
	    bsf.dump();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
