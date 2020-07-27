package com.techminia.collection.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "users")
@Getter @Setter
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="created_at", insertable=false, updatable=false)
    @JsonIgnore
    private Date createdAt;

    @JsonIgnore
    @Column(name = "is_deleted")
    private boolean deleted;

    @JsonIgnore
    @Column(name = "deleted_at")
    private Date dateDeleted;

    @JsonProperty("reset_password")
    @Column(name = "reset_password")
    private boolean resetPassword;

    @JsonProperty("enabled")
    @Column(name = "enabled")
    private boolean enabled;

    @JsonProperty("user_number")
    @Column(name = "user_number")
    private String userNumber;

    @JsonProperty("username")
    @Column(name = "username")
    private String username;

    @JsonProperty("email_address")
    @Column(name = "email_address")
    private String emailAddress;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @JsonProperty(value = "first_name")
    @Column(name = "first_name")
    private String firstName;

    @JsonProperty(value = "last_name")
    @Column(name = "last_name")
    private String lastName;

    @JsonProperty("other_name")
    @Column(name = "other_name")
    private String otherName;

    @JsonProperty("phone_number")
    @Column(name = "phone_number")
    private String phoneNumber;

    @Transient
    @JsonIgnore
    Collection<GrantedAuthority> authorities;

    public User() {
    }

    public User(Integer id) {
        this.id = id;
    }

    public User(String username, String phoneNumber) {
        this.username = username;
        this.phoneNumber = phoneNumber;
    }

    public User(Integer id, String userNumber, String emailAddress, String phoneNumber) {
        this.id = id;
        this.userNumber = userNumber;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
    }

    public User(boolean resetPassword, boolean enabled, String userNumber, String username, String emailAddress, String password, String firstName, String lastName, String otherName, String phoneNumber) {
        this.resetPassword = resetPassword;
        this.enabled = enabled;
        this.userNumber = userNumber;
        this.username = username;
        this.emailAddress = emailAddress;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.otherName = otherName;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

}
