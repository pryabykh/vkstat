package com.pryabykh.vkstat.core.vk.ds;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
public class GetUserResult {
    private VkError error;
    private List<VkUser> response;
}
