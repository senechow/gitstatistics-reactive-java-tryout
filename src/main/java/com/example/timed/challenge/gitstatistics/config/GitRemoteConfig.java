package com.example.timed.challenge.gitstatistics.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.remote.git")
public class GitRemoteConfig {

    private String baseUrl;

    private String acceptVersionValue;
    private String userAgentHeader;

    private GitEndpointConfig endpoints;

    private String accessToken;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getAcceptVersionValue() {
        return acceptVersionValue;
    }

    public void setAcceptVersionValue(String acceptVersionValue) {
        this.acceptVersionValue = acceptVersionValue;
    }

    public String getUserAgentHeader() {
        return userAgentHeader;
    }

    public void setUserAgentHeader(String userAgentHeader) {
        this.userAgentHeader = userAgentHeader;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public GitEndpointConfig getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(GitEndpointConfig endpoints) {
        this.endpoints = endpoints;
    }
}
