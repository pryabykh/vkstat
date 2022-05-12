package com.pryabykh.vkstat.core.validation;

import java.util.List;

public interface ValidationResult {
    boolean addError(String error);
    List<String> getErrors();
    boolean hasErrors();
}
