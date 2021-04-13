package com.example.sms_server.service.shiro;

import lombok.Data;

import java.io.Serializable;

@Data
public class ShiroUser implements Serializable {
    private String userName;
    private String id;
    private String authCacheKey;
    private String password;

    public String toString() {
        return this.authCacheKey;
    }
}
