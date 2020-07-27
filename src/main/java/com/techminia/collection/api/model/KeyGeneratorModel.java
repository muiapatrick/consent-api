package com.techminia.collection.api.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class KeyGeneratorModel {
    private String public_key;
    private String private_key;
    private String app;

    public KeyGeneratorModel() {
    }

    public KeyGeneratorModel(String public_key, String private_key, String app) {
        this.public_key = public_key;
        this.private_key = private_key;
        this.app = app;
    }

}

