package com.pryabykh.vkstat.vendor.validation.rules;

import com.pryabykh.vkstat.vendor.validation.ValidationRuleResult;
import com.pryabykh.vkstat.core.validation.exceptions.ValidationErrorException;

import static java.lang.String.format;

public class MaxRule implements ValidationRule {
    private String maxLength;

    @Override
    public ValidationRuleResult check(Object target) throws ValidationErrorException {
        if (maxLength == null || maxLength.isEmpty() || !isValidNumberValue(maxLength)) {
            throw new ValidationErrorException("Criteria \"max\" should be valid number to specify max length of value");
        }
        ValidationRuleResult validationRuleResult = new ValidationRuleResultImpl();

        String targetString = String.valueOf(target);
        if (targetString.length() > Integer.parseInt(maxLength)) {
            validationRuleResult.setError(format("Field cannot be more than %s chars", maxLength));
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

    public void setMaxLength(String maxLength) {
        this.maxLength = maxLength;
    }
}
