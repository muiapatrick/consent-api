package com.techminia.collection.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.techminia.collection.api.validation.granttype.ValidGrantType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class GrantType {

    @JsonProperty("grant_type")
    @NotBlank(message = "Grant Type required")
    @ValidGrantType
    private String grantType;

    public GrantType() {
    }
}
