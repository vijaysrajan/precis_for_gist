package com.fratics.precis.fis.base;

import com.fratics.precis.fis.util.Util;

public abstract class PrecisBase {
    public static String id = Util.generateRandomId();

    public boolean initialize() throws Exception {
	return true;
    }

    public boolean unInitialize() throws Exception {
	return true;
    }

    public boolean reInitialize() throws Exception {
	if (this.unInitialize())
	    return this.initialize();
	return false;
    }
}
