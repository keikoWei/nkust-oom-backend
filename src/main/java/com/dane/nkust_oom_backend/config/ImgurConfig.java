package com.dane.nkust_oom_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class ImgurConfig {
    @Value("${imgur.client.id}")
    private String clientId;

    @Value("${imgur.client.secret}")
    private String clientSecret;

    @Value("${imgur.callback.url}")
    private String callbackUrl;
    
    @Value("${imgur.refresh.token}")
    private String refreshToken;
    
    @Value("${imgur.access.token}")
    private String accessToken;

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }
    
    public String getRefreshToken() {
        return refreshToken;
    }
    
    public String getAccessToken() {
        return accessToken;
    }
} 