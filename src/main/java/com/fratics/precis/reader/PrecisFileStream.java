package com.fratics.precis.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.fratics.precis.exception.PrecisException;
import com.fratics.precis.fis.base.PrecisStream;
import com.fratics.precis.fis.util.PrecisConfigProperties;

public class PrecisFileStream extends PrecisStream {

    private BufferedReader br = null;

    public PrecisFileStream(String fileName) {
	super(fileName);
    }

    public boolean initialize() throws Exception {
	File file = new File(this.getStreamName());
	if (!(file.exists() && file.canRead()))
	    throw new PrecisException("Error Reading file:: "
		    + this.getStreamName());
	try {
	    this.br = new BufferedReader(new FileReader(file));
	} catch (Exception e) {
	    // log error
	    throw e;
	}
	return true;
    }

    public boolean unInitialize() throws Exception {
	try {
	    this.br.close();
	    return true;
	} catch (Exception e) {
	    // log error
	    throw e;
	}
    }

    public String[] readStream() throws Exception {
	String[] str = read();
	if (str != null)
	    this.incLines();
	return str;
    }

    private String[] read() throws Exception {
	try {
	    String thisLine = br.readLine();
	    //Handle Comments
	    while(thisLine != null && thisLine.startsWith("#")) thisLine = br.readLine();
	    if (thisLine != null) {
		return thisLine.split(PrecisConfigProperties.INPUT_RECORD_SEPERATOR);
	    } else {
		return null;
	    }
	} catch (IOException e) {
	    // log error
	    throw e;

	} catch (Exception ex) {
	    // log error
	    throw new PrecisException(ex.getMessage());
	}
    }
}
