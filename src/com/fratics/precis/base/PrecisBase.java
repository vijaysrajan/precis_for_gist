package com.fratics.precis.base;

import com.fratics.precis.util.Util;

public abstract class PrecisBase {
    public static String id = Util.generateRandomId();
    public boolean initialize() throws Exception { return true; }
    public boolean unInitialize() throws Exception { return true; }
    public boolean reInitialize() throws Exception {
	if (unInitialize())
	    return initialize();
	return false;
    }
}
