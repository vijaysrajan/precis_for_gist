package com.fratics.precis.sanitation.rules;

import com.fratics.precis.base.SanitationRuleBase;
import com.fratics.precis.base.ValueObject;
import com.fratics.precis.sanitation.FieldObject;

public class UniqueValueRule extends SanitationRuleBase {
    public void applyRule(ValueObject vo) throws Exception {
	long numOfLines = vo.getNoOfValues();
	FieldObject [] fo = vo.getFieldObjects();
	for (int i = 0; i < fo.length; i++){
	    if(fo[i].getNumberOfUniques() == numOfLines) return;
	    //add the result
	}
    }
}
