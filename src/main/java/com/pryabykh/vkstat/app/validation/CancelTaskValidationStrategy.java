package com.pryabykh.vkstat.app.validation;

import com.pryabykh.vkstat.core.validation.ValidationStrategy;
import com.pryabykh.vkstat.core.validation.exceptions.NoSuchFieldValidationException;
import com.pryabykh.vkstat.core.validation.exceptions.ValidationErrorException;

import java.util.HashMap;
import java.util.Map;

public enum CancelTaskValidationStrategy implements ValidationStrategy {
    INSTANCE;

    @Override
    public Map<String, String> getRules() throws ValidationErrorException, NoSuchFieldValidationException {
        Map<String, String> rules = new HashMap<>();
        rules.put("vkUserId", "required|max:255");
        rules.put("ownerVkId", "required|max:255");
        return rules;
    }
}
