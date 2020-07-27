package com.techminia.collection.config.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class CustomAccessTokEnhancer implements TokenEnhancer {
    private static final Logger logger = LogManager.getLogger(CustomAccessTokEnhancer.class);


    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication oAuth2Authentication) {
        DefaultOAuth2AccessToken result = new DefaultOAuth2AccessToken(accessToken);
        Map<String, Object> info = new LinkedHashMap(accessToken.getAdditionalInformation());
        if (info.containsKey("jti")) {
            info.remove("jti");
        }

        if (info.containsKey("user")) {
            info.remove("user");
        }
        result.setScope(null);
        result.setAdditionalInformation(info);
        return result;
    }
}
