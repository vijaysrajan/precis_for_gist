package com.fratics.precis.fis.feed;

import java.util.ArrayList;

public class BaseFeedPartitioner {
    
    public class BaseFeedElementList extends ArrayList<BaseFeedElement>{
	private static final long serialVersionUID = 1L;
    }

    public class BaseFeedPartitionerReader{
	private int currentStage = 2;
	private int currentIndex = 0;
	
	public BaseFeedPartitionerReader(int stage){
	    this.currentStage = stage;
	    this.currentIndex = 0;
	}
	
	public boolean hasNext(){
	    return ((this.currentStage < partitionMap.length) && (this.currentIndex < partitionMap[this.currentStage].size()));
	}
	
	public BaseFeedElement getNext(){
	    //System.err.println("Current Index ::" + this.currentIndex + " Current Stage ::" + this.currentStage);
	    BaseFeedElement e = partitionMap[this.currentStage].get(currentIndex);
	    this.currentIndex++;
	    if( currentIndex >= partitionMap[this.currentStage].size()){
		this.currentIndex = 0;
		this.currentStage++;
		while((this.currentStage < partitionMap.length) && (partitionMap[this.currentStage].size() <= 0)) this.currentStage++;
	    }
	    return e;
	}
	
    }
    
    private BaseFeedPartitionerReader bfpr = null;
    private BaseFeedElementList [] partitionMap = null;
    
    public BaseFeedPartitioner(int length){
	partitionMap =  new BaseFeedElementList[length];
	for(int i = 0; i < partitionMap.length; i++){
	    partitionMap[i] =  new BaseFeedElementList();
	}
    }
  
    public void addElement(int index, BaseFeedElement e) {
	partitionMap[index].add(e);
    }
    
    public BaseFeedPartitionerReader getReader() throws Exception {
	if(this.bfpr == null) throw new Exception("BaseFeedPartitionerReader not initialized");
	return this.bfpr;
    }
    
    public void initReader(int stage){
	this.bfpr = new BaseFeedPartitionerReader(stage);
    }
    
    public void closeReader(){
	bfpr = null;
    }

    public String toString() {
	String str = "";
	 for(int i = 0; i < partitionMap.length; i++) {
	    ArrayList<BaseFeedElement> al = partitionMap[i];
	    if(al.size() > 0 ) str = str + "PN ==> " + i + ", NS ==> " + al.size() + ", ==> {" + al.toString() + "}\n";
	}
	return str;
    }
}
