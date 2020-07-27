package com.techminia.collection.global;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;

@Component
public class GlobalValues {
    public static TokenStore tokenStore;
    public static PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        GlobalValues.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setTokenStore(TokenStore tokenStore) {
        GlobalValues.tokenStore = tokenStore;
    }
}
