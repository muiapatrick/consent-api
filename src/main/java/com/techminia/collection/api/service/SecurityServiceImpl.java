package com.techminia.collection.api.service;

import com.techminia.collection.api.domain.OauthClientDetails;
import com.techminia.collection.api.domain.User;
import com.techminia.collection.api.model.CredsAdditionalDetails;
import com.techminia.collection.global.GlobalValues;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static com.techminia.collection.global.GlobalRepository.clientDetailsRepository;
import static com.techminia.collection.global.GlobalService.tokenServices;
import static com.techminia.collection.global.GlobalValues.tokenStore;

@Service("securityService")
public class SecurityServiceImpl implements ISecurityService {

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public OauthClientDetails addOauthClientDetails(OauthClientDetails oauthClientDetails) {
        oauthClientDetails.setClientSecret(GlobalValues.passwordEncoder.encode(oauthClientDetails.getClientSecret()));
        return clientDetailsRepository.save(oauthClientDetails);
    }

    @Override
    public void revokeAccessTokenM(String clientId, String username, boolean revokeRefreshToken) {
        Collection<OAuth2AccessToken> tokens = tokenStore.findTokensByClientIdAndUserName(clientId, username);
        if (tokens != null) {
            for (OAuth2AccessToken token : tokens) {
                if (revokeRefreshToken) {
                    OAuth2RefreshToken refreshToken = token.getRefreshToken();
                    System.out.println("================================== : REFRESH TOKEN REVOKE IS NULL==================== " + (refreshToken == null));
                    if (refreshToken != null) {
                        tokenStore.removeRefreshToken(refreshToken);
                    }
                }
                tokenStore.removeAccessToken(token);
                tokenServices.revokeToken(token.getValue());
            }
        }
    }

    @Override
    public Object getUserAuthenticated() {
        try {
            Authentication authentication = getAuthentication();
            return authentication.getPrincipal();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User getAuthenticatedUser() {
        User user = null;
        try {
            Authentication authentication = getAuthentication();

            if (authentication.getPrincipal() instanceof User) {
                user = (User) authentication.getPrincipal();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public CredsAdditionalDetails getAdditionalDetails(String clientId) {
        CredsAdditionalDetails details = new CredsAdditionalDetails();
        try {
            OauthClientDetails oauthClientDetails = clientDetailsRepository.findByClientId(clientId);

            if (oauthClientDetails != null) {
                ObjectMapper mapper = new ObjectMapper();
                details = mapper.readValue(oauthClientDetails.getAdditionalInformation(), CredsAdditionalDetails.class);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return details;
    }
}
