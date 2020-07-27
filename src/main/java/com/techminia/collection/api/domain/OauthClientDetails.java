package com.techminia.collection.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "oauth_client_details")
public class OauthClientDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id")
    @JsonProperty("client_id")
    private String clientId;

    @Column(name = "resource_ids")
    @JsonProperty("resource_ids")
    private String resourceIds;

    @Column(name = "client_secret")
    @JsonProperty("client_secret")
    private String clientSecret;

    @Column(name = "scope")
    private String scope;

    @Column(name = "authorized_grant_types")
    @JsonProperty("authorized_grant_types")
    private String authorizedGrantTypes;

    @Column(name = "web_server_redirect_uri")
    @JsonProperty("web_server_redirect_uri")
    private String webServerRedirectUri;

    @Column(name = "authorities")
    private String authorities;

    @Column(name = "access_token_validity")
    @JsonProperty("access_token_validity")
    private long accessTokenValidity;

    @Column(name = "refresh_token_validity")
    @JsonProperty("refresh_token_validity")
    private long refreshTokenValidity;

    @Column(name = "additional_information")
    @JsonProperty("additional_information")
    private String additionalInformation;

    @Column(name = "autoapprove")
    private boolean autoapprove = false;

    @Column(name = "app_name")
    @JsonProperty("app_name")
    private String appName;


    public OauthClientDetails(String clientId, String resourceIds, String clientSecret, String scope, String authorizedGrantTypes, String webServerRedirectUri, String authorities, long accessTokenValidity, long refreshTokenValidity, String additionalInformation, boolean autoapprove, String appName) {
        this.clientId = clientId;
        this.resourceIds = resourceIds;
        this.clientSecret = clientSecret;
        this.scope = scope;
        this.authorizedGrantTypes = authorizedGrantTypes;
        this.webServerRedirectUri = webServerRedirectUri;
        this.authorities = authorities;
        this.accessTokenValidity = accessTokenValidity;
        this.refreshTokenValidity = refreshTokenValidity;
        this.additionalInformation = additionalInformation;
        this.autoapprove = autoapprove;
        this.appName = appName;
    }

    public OauthClientDetails() {
    }

}
