package com.pryabykh.vkstat.core.vk.ds;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VkError {
    @JsonProperty("error_code")
    private int errorCode;

    @JsonProperty("error_msg")
    private String errorMessage;
}
