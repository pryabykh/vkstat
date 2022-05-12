package com.pryabykh.vkstat.vendor.validation.rules;

import com.pryabykh.vkstat.core.validation.exceptions.ValidationErrorException;
import com.pryabykh.vkstat.vendor.validation.ValidationRuleResult;

import static java.lang.String.format;

public class MinRule implements ValidationRule {
    private String minLength;

    @Override
    public ValidationRuleResult check(Object target) throws ValidationErrorException {
        if (minLength == null || minLength.isEmpty() || !isValidNumberValue(minLength)) {
            throw new ValidationErrorException("Criteria \"min\" should be valid number to specify min length of value");
        }
        ValidationRuleResult validationRuleResult = new ValidationRuleResultImpl();

        String targetString = String.valueOf(target);
        if (targetString.length() < Integer.parseInt(minLength)) {
            validationRuleResult.setError(format("Field cannot be less than %s chars", minLength));
        }
        return validationRuleResult;
    }

    private boolean isValidNumberValue(String number) {
        try {
            Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public void setMinLength(String minLength) {
        this.minLength = minLength;
    }
}
