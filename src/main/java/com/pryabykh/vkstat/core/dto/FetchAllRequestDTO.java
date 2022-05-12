package com.pryabykh.vkstat.core.dto;

import lombok.Data;

@Data
public class FetchAllRequestDTO {
    private String vkUserId;
    private int ownerVkId;
    private int page;
    private int size;
}
