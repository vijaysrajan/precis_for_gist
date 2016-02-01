package com.fratics.precis.sanitation.rules;

import com.fratics.precis.base.FieldObject;
import com.fratics.precis.base.ValueObject;

public class UniqueValueRule extends SanitationRuleBase {
    public void applyRule(ValueObject vo) throws Exception {
	this.setRuleName("UniqueValuesRule");
	long numOfLines = vo.inputObject.getNoOfValues();
	FieldObject[] fo = vo.inputObject.getFieldObjects();
	for (int i = 0; i < fo.length; i++) {
	    if (fo[i].getNumberOfUniques() == numOfLines) {
		vo.resultObject.loadResult(new SingleRuleResult(ruleName, true,
			i));
	    } else {
		vo.resultObject.loadResult(new SingleRuleResult(ruleName,
			false, i));
	    }
	}
    }
}
