package com.pryabykh.vkstat.core.vk;

import com.pryabykh.vkstat.core.vk.ds.AuthData;
import com.pryabykh.vkstat.core.vk.ds.AuthResult;
import com.pryabykh.vkstat.core.vk.ds.VkUser;
import com.pryabykh.vkstat.core.vk.exceptions.RequestToVkApiFailedException;
import com.pryabykh.vkstat.core.vk.exceptions.VkAuthErrorException;

import java.util.Optional;

public interface VkService {
    AuthResult auth(String codeToGetToken) throws VkAuthErrorException;
    Optional<VkUser> getUser(String vkUserId, String accessToken) throws RequestToVkApiFailedException;
}
