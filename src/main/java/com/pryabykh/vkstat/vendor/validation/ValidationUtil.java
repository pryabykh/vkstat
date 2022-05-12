package com.pryabykh.vkstat.vendor.validation;

import com.pryabykh.vkstat.core.validation.exceptions.NoSuchFieldValidationException;
import com.pryabykh.vkstat.core.validation.exceptions.ValidationErrorException;
import com.pryabykh.vkstat.core.validation.ValidationResult;

import java.util.Map;

public interface ValidationUtil {
    ValidationResult check(Object target, Map<String, String> validationRules) throws ValidationErrorException, NoSuchFieldValidationException;
}
