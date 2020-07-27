package com.techminia.collection.api.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "password_token")
@Getter @Setter
public class PasswordToken {
    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "expiry_date")
    private Date expiryDate;

    @Column(name = "token")
    private String token;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "is_used")
    private boolean tokenUsed;

    public PasswordToken() {
    }

    public PasswordToken(String token, String userEmail, boolean tokenUsed) {
        this.token = token;
        this.userEmail = userEmail;
        this.tokenUsed = tokenUsed;
    }

    public PasswordToken(Date createdAt, Date expiryDate, String token, String userEmail, boolean tokenUsed) {
        this.createdAt = createdAt;
        this.expiryDate = expiryDate;
        this.token = token;
        this.userEmail = userEmail;
        this.tokenUsed = tokenUsed;
    }
}
