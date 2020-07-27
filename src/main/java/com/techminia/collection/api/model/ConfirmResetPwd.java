package com.techminia.collection.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class ConfirmResetPwd {
    @JsonProperty("token")
    @NotBlank(message = "Token Required")
    private String token;

    @JsonProperty("password")
    @NotBlank(message = "Password Required")
    private String password;

    public ConfirmResetPwd() {
    }
}
