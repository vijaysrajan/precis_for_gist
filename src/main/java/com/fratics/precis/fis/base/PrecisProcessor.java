package com.fratics.precis.fis.base;

/*
 * An Abstract Flow Processor Object. 
 * 
 * Each Flow Processor Object applies the logic/functionality on the 
 * Value Object.
 * 
 * The State change due to this application is captured in the Value Object.
 * 
 * The Value Object is a holder composite object of both
 * 	1) Input Object
 * 	2) Output Object
 * 
 */

public abstract class PrecisProcessor extends PrecisBase {

    // The functionality provided is defined by the overridden process() method
    // in the derived class.
    public abstract boolean process(ValueObject o) throws Exception;
}
