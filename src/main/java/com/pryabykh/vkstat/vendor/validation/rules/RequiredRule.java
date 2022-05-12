package com.pryabykh.vkstat.vendor.validation.rules;


import com.pryabykh.vkstat.vendor.validation.ValidationRuleResult;

public class RequiredRule implements ValidationRule {
    @Override
    public ValidationRuleResult check(Object target) {
        ValidationRuleResult validationRuleResult = new ValidationRuleResultImpl();
        if (target == null) {
            validationRuleResult.setError("Field cannot be null");
        }
        if (target instanceof String) {
            if (((String) target).isBlank() || ((String) target).isEmpty()) {
                validationRuleResult.setError("Field cannot be empty");
            }
        }
        return validationRuleResult;
    }
}
