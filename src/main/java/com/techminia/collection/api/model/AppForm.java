package com.techminia.collection.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.techminia.collection.api.validation.clienttype.ValidClientType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Setter @Getter
@Validated
public class AppForm {
    @NotBlank(message="App name required")
    @JsonProperty(value="app_name", required=true)
    private String appName;

    @JsonProperty("grant_types")
    @Valid
    private List<GrantType> grantTypes;

    @JsonProperty("access_token_validity")
    private Integer accessTokenValidity;

    @JsonProperty("refresh_token_validity")
    private Integer refreshTokenValidity;

    @JsonProperty("client_type_id")
    @NotNull(message = "Client Type Id required")
    @ValidClientType(message = "A valid client type id required")
    private String clientType;

    @JsonProperty("source")
    @NotNull(message = "Source required")
    private Integer source;

    public AppForm() {
    }
}

