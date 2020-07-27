package com.techminia.collection.api.service;

import com.techminia.collection.api.domain.PasswordToken;
import com.techminia.collection.api.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    PasswordToken savePasswordToken(PasswordToken passwordToken);
    List<PasswordToken> getPasswordTokens(PasswordToken passwordToken);

    UserDetails loadUserByUsername(String username);
    Page<User> getUsers(User user, String fullName, PageRequest pageRequest);

}
