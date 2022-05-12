package com.pryabykh.vkstat.core.validation;

import com.pryabykh.vkstat.core.validation.exceptions.NoSuchFieldValidationException;
import com.pryabykh.vkstat.core.validation.exceptions.ValidationErrorException;

import java.util.Map;

public interface ValidationStrategy {
    Map<String, String> getRules() throws ValidationErrorException, NoSuchFieldValidationException;
}
