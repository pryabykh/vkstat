package com.pryabykh.vkstat.vendor.validation;

public interface ValidationRuleResult {
    void setError(String error);
    String getError();
    boolean hasErrors();
}
