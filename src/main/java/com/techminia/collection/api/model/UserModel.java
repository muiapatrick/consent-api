package com.techminia.collection.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.techminia.collection.api.validation.email.ValidEmail;
import com.techminia.collection.api.validation.phonenumber.ValidPhone;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class UserModel {
    @JsonProperty("email_address")
    @ValidEmail
    @NotBlank(message = "Email address is required")
    private String emailAddress;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("other_name")
    private String otherName;

    @JsonProperty("phone_number")
    @ValidPhone
    private String phoneNumber;

    @JsonProperty("enabled")
    private boolean enabled;

    public UserModel() {
    }
}
