package com.fratics.precis.fis.main;

import com.fratics.precis.fis.sanitation.SanitationMain;


import com.fratics.precis.fis.util.PrecisConfigProperties;
import com.fratics.precis.util.ConfigObject;

//Main Driver for Precis Engine.

public class Main {

    private static void printUsage() {
	System.out.println();
	System.out.println();
	System.out
		.print("Usage :: java com.fratics.precis.fis.main.Main [${fileName}]");
	System.out
		.println("Optional Argument - Configuration File Name, If Configuration file is not Specified");
	System.out
		.println("Default Configuration file in location ./conf/precisconfig.properties will be loaded");
	System.out.println();
	System.out.println();
    }

    public static void main(String[] args) {
	try {
	    if (args.length > 0) {
		ConfigObject.setConfigFile(args[0]);
	    }
	    ConfigObject c = new ConfigObject();
	    c.initialize();
	    PrecisConfigProperties.loadConfig(c);
	    if (PrecisConfigProperties.IS_COUNT_PRECIS) {
		SanitationMain.run(args);
	    } else {
		PrecisMain.run(args);
	    }
	} catch (Exception e) {
	    System.err.println("Exception Raised ::" + e.toString());
	    printUsage();
	    e.printStackTrace();
	}
    }

}
