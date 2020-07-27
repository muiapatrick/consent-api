package com.techminia.collection.api.service;

import com.techminia.collection.api.domain.OauthClientDetails;
import com.techminia.collection.api.domain.User;
import com.techminia.collection.api.model.CredsAdditionalDetails;
import org.springframework.security.core.Authentication;

public interface ISecurityService {
    Authentication getAuthentication();
    OauthClientDetails addOauthClientDetails(OauthClientDetails oauthClientDetails);
    void revokeAccessTokenM(String clientId, String username, boolean revokeRefreshToken);

    Object getUserAuthenticated();
    User getAuthenticatedUser();

    CredsAdditionalDetails getAdditionalDetails(String clientId);
}
