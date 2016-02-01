package com.fratics.precis.sanitation.rules;

public class SingleRuleResult {
    public String ruleName;
    public boolean result;
    public long fieldIndex;

    public SingleRuleResult(String ruleName, boolean result, long fieldIndex) {
	this.ruleName = ruleName;
	this.result = result;
	this.fieldIndex = fieldIndex;
    }

}
