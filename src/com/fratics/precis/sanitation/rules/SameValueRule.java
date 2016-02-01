package com.fratics.precis.sanitation.rules;

import com.fratics.precis.base.SanitationRuleBase;
import com.fratics.precis.base.ValueObject;
import com.fratics.precis.sanitation.FieldObject;

public class SameValueRule extends SanitationRuleBase {
    public void applyRule(ValueObject vo) throws Exception {
	FieldObject [] fo = vo.getFieldObjects();
	for (int i = 0; i < fo.length; i++){
	    if(fo[i].getNumberOfUniques() == 1) return;
	    //add the result
	}
    }
}
