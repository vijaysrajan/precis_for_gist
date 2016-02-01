package com.fratics.precis.sanitation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fratics.precis.base.ResultObject;
import com.fratics.precis.sanitation.rules.SingleRuleResult;

class RuleResult {
    public RuleResult(String ruleName, boolean result) {
	this.result = result;
	this.ruleName = ruleName;
    }

    public String ruleName;
    public boolean result;

    public String toString() {
	return "Rule :: " + ruleName + " Result :: " + result;
    }
}

class FieldResult {
    public long fieldIndex;

    public FieldResult(int i) {
	fieldIndex = i;
    }

    public List<RuleResult> rulesResult = new ArrayList<RuleResult>();

    public String toString() {
	return "Field Index :: " + fieldIndex + " Applied Rules ==> { "
		+ rulesResult.toString() + " }";
    }
}

public class SanitationResultObject extends ResultObject {
    private static final long serialVersionUID = 8259727611192043540L;
    private int numOfFields = 0;
    private FieldResult[] fr = null;

    protected boolean isInitialized() {
	return (numOfFields > 0);
    }

    public void init(int numOfFields) {
	this.numOfFields = numOfFields;
	fr = new FieldResult[this.numOfFields];
	for (int i = 0; i < this.numOfFields; i++) {
	    fr[i] = new FieldResult(i);
	}
    }

    private void loadResult(String ruleName, boolean result, int fieldIndex) {
	fr[fieldIndex].rulesResult.add(new RuleResult(ruleName, result));
    }

    public String toString() {
	return "Rule Result ::" + Arrays.toString(fr) + "\n";
    }

    public void loadResult(Object o) throws Exception {
	SingleRuleResult srr = (SingleRuleResult) o;
	loadResult(srr.ruleName, srr.result, srr.fieldIndex);
    }

}
