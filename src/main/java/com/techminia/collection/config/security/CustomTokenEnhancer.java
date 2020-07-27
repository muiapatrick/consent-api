package com.techminia.collection.config.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

public class CustomTokenEnhancer implements TokenEnhancer {
    private static final Logger logger = LogManager.getLogger(CustomTokenEnhancer.class);

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
        try {
            final Map<String, Object> additionalInfo = new HashMap<>();

            if (oAuth2Authentication.getOAuth2Request().getGrantType() == null || oAuth2Authentication.getOAuth2Request().getGrantType().equals("password")) {
                additionalInfo.put("user", oAuth2Authentication.getPrincipal());
            }

            ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(additionalInfo);

            oAuth2Authentication.getDetails();
        }catch (Exception e){
            e.printStackTrace();
        }
        return oAuth2AccessToken;
    }
}
