package com.fratics.precis.sanitation.rules;

import com.fratics.precis.base.PrecisBase;
import com.fratics.precis.base.ValueObject;

public abstract class SanitationRuleBase extends PrecisBase {
    protected String ruleName = null;

    public void setRuleName(String str) {
	this.ruleName = str;
    }

    public String getRuleName() {
	return this.ruleName;
    }

    public abstract void applyRule(ValueObject vo) throws Exception;
}
