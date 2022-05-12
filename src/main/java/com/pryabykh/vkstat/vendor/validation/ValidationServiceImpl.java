package com.pryabykh.vkstat.vendor.validation;

import com.pryabykh.vkstat.core.validation.ValidationService;
import com.pryabykh.vkstat.core.validation.exceptions.NoSuchFieldValidationException;
import com.pryabykh.vkstat.core.validation.exceptions.ValidationErrorException;
import com.pryabykh.vkstat.core.validation.ValidationResult;
import com.pryabykh.vkstat.core.validation.ValidationStrategy;
import org.springframework.stereotype.Service;

@Service
public class ValidationServiceImpl implements ValidationService {
    private final ValidationUtil validationUtil = ValidationUtilImpl.INSTANCE;

    @Override
    public ValidationResult validate(Object target, ValidationStrategy validationStrategy) throws ValidationErrorException, NoSuchFieldValidationException {
        return validationUtil.check(target, validationStrategy.getRules());
    }
}
