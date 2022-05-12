package com.pryabykh.vkstat.vendor.validation;

import com.pryabykh.vkstat.core.validation.exceptions.ValidationErrorException;
import com.pryabykh.vkstat.vendor.validation.rules.ValidationRule;

public interface RulesSimpleFactory {
    ValidationRule getRule(String criteria) throws ValidationErrorException;
}
