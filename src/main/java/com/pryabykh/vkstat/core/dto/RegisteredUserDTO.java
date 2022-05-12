package com.pryabykh.vkstat.core.dto;

import lombok.Data;

@Data
public class RegisteredUserDTO {
    private Long id;
    private int vkUserId;
    private String vkToken;
}
