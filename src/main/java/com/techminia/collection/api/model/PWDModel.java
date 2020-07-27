package com.techminia.collection.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class PWDModel {
    @JsonProperty("password")
    @NotBlank(message = "New Password required")
    private String password;

    public PWDModel() {
        this.password = password;
    }
}
