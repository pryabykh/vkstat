package com.pryabykh.vkstat.core.vk.ds;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VkUser {
    @JsonProperty("id")
    private int id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("can_access_closed")
    private boolean canAccessClosed;

    @JsonProperty("is_closed")
    private boolean isClosed;

    @JsonProperty("online")
    private Online online;
}
