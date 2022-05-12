package com.pryabykh.vkstat.core.vk.ds;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "vk")
public class AuthData {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String oauthHost;
}
