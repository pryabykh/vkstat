package com.pryabykh.vkstat.vendor.validation.rules;

import com.pryabykh.vkstat.core.validation.exceptions.ValidationErrorException;
import com.pryabykh.vkstat.vendor.validation.ValidationRuleResult;

public interface ValidationRule {
    ValidationRuleResult check(Object target) throws ValidationErrorException;
}
