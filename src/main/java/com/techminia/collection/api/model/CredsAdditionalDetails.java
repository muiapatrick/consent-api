package com.techminia.collection.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CredsAdditionalDetails {
    @JsonProperty("clientType")
    private Integer clientType;

    @JsonProperty("source")
    private Integer source;

    @JsonIgnore
    private String client_id;

    public CredsAdditionalDetails() {
    }
}
