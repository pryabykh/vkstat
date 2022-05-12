package com.pryabykh.vkstat.app.validation;

import com.pryabykh.vkstat.vendor.validation.ValidationUtil;
import com.pryabykh.vkstat.vendor.validation.ValidationUtilImpl;
import com.pryabykh.vkstat.core.validation.exceptions.NoSuchFieldValidationException;
import com.pryabykh.vkstat.core.validation.exceptions.ValidationErrorException;
import com.pryabykh.vkstat.core.validation.ValidationResult;
import com.pryabykh.vkstat.core.validation.ValidationStrategy;

import java.util.HashMap;
import java.util.Map;

public enum RegistrationDataValidationStrategy implements ValidationStrategy {
    INSTANCE;

    @Override
    public Map<String, String> getRules() throws ValidationErrorException, NoSuchFieldValidationException {
        Map<String, String> rules = new HashMap<>();
        rules.put("codeToGetToken", "required|max:255");
        return rules;
    }
}
