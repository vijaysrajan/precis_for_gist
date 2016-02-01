package com.fratics.precis.sanitation.rules;

import com.fratics.precis.base.PrecisProcessor;
import com.fratics.precis.base.ValueObject;

public class SanitationRuleProcessor extends PrecisProcessor {
    SanitationRuleBase[] srb = { new SameValueRule(), new UniqueValueRule() };

    public boolean process(ValueObject vo) throws Exception {
	vo.resultObject.init(vo.inputObject.getFieldObjects().length);
	for (int i = 0; i < srb.length; i++) {
	    srb[i].applyRule(vo);
	}
	return true;
    }
}