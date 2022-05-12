package com.pryabykh.vkstat.vendor.validation.rules;

import com.pryabykh.vkstat.vendor.validation.ValidationRuleResult;

public class ValidationRuleResultImpl implements ValidationRuleResult {
    @Override
    public String getError() {
        return error;
    }

    private String error;

    @Override
    public void setError(String error) {
        this.error = error;
    }

    @Override
    public boolean hasErrors() {
        return error != null;
    }
}
