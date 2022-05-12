package com.pryabykh.vkstat.core.validation;

import com.pryabykh.vkstat.core.validation.exceptions.NoSuchFieldValidationException;
import com.pryabykh.vkstat.core.validation.exceptions.ValidationErrorException;
import com.pryabykh.vkstat.core.validation.ValidationResult;
import com.pryabykh.vkstat.core.validation.ValidationStrategy;

public interface ValidationService {
    ValidationResult validate(Object target, ValidationStrategy validationStrategy)
            throws ValidationErrorException, NoSuchFieldValidationException;
}
