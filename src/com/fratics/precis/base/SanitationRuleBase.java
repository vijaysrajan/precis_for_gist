package com.fratics.precis.base;

public abstract class SanitationRuleBase extends PrecisBase {
    public abstract void applyRule(ValueObject vo) throws Exception;
}
