package com.pryabykh.vkstat.vendor.validation;

import com.pryabykh.vkstat.core.validation.ValidationResult;

import java.util.ArrayList;
import java.util.List;

public class ValidationResultImpl implements ValidationResult {
    private final List<String> errors = new ArrayList<>();

    @Override
    public List<String> getErrors() {
        return errors;
    }

    @Override
    public boolean addError(String error) {
        return errors.add(error);
    }

    @Override
    public boolean hasErrors() {
        return !errors.isEmpty();
    }
}
