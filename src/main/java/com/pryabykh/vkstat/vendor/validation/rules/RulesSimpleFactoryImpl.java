package com.pryabykh.vkstat.vendor.validation.rules;

import com.pryabykh.vkstat.vendor.validation.RulesSimpleFactory;
import com.pryabykh.vkstat.core.validation.exceptions.ValidationErrorException;

import static java.lang.String.format;

public class RulesSimpleFactoryImpl implements RulesSimpleFactory {

    @Override
    public ValidationRule getRule(String criteria) throws ValidationErrorException {
        if (criteria == null) {
            throw new ValidationErrorException("Rule cannot be null");
        }
        String[] ruleNameAndCriteria = criteria.split(":");
        String ruleName = ruleNameAndCriteria[0];
        String ruleValue = null;
        if (ruleNameAndCriteria.length > 1) {
            ruleValue = ruleNameAndCriteria[1];
        }
        switch (ruleName) {
            case "required" -> {
                return new RequiredRule();
            }
            case "max" -> {
                MaxRule maxRule = new MaxRule();
                maxRule.setMaxLength(ruleValue);
                return maxRule;
            }
            case "min" -> {
                MinRule minRule = new MinRule();
                minRule.setMinLength(ruleValue);
                return minRule;
            }
            default -> throw new ValidationErrorException(format("Cannot find rule for name %s", ruleName));
        }
    }
}
