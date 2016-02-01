package com.fratics.precis.sanitation;

import com.fratics.precis.base.FieldObject;
import com.fratics.precis.base.ValueObject;
import com.fratics.precis.sanitation.rules.SanitationRuleBase;
import com.fratics.precis.sanitation.rules.SingleRuleResult;

public class SameValueRule extends SanitationRuleBase {
    public void applyRule(ValueObject vo) throws Exception {
	this.setRuleName("SameValueRule");
	FieldObject[] fo = vo.inputObject.getFieldObjects();
	for (int i = 0; i < fo.length; i++) {
	    if (fo[i].getNumberOfUniques() == 1) {
		vo.resultObject.loadResult(new SingleRuleResult(ruleName, true,
			i));
	    } else {
		vo.resultObject.loadResult(new SingleRuleResult(ruleName,
			false, i));
	    }
	}
    }
}
