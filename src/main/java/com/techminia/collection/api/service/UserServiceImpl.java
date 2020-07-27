package com.techminia.collection.api.service;

import com.techminia.collection.api.domain.PasswordToken;
import com.techminia.collection.api.domain.User;
import com.techminia.collection.api.specification.PasswordTokenPredicate;
import com.techminia.collection.api.specification.UserPredicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.techminia.collection.api.validation.phonenumber.ValidatePhone.formartedPhoneNumber;
import static com.techminia.collection.api.validation.phonenumber.ValidatePhone.validPhoneNumber;
import static com.techminia.collection.global.GlobalRepository.passwordTokenRepository;
import static com.techminia.collection.global.GlobalRepository.userRepository;
import static com.techminia.collection.global.GlobalService.userService;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {
    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public PasswordToken savePasswordToken(PasswordToken passwordToken) {
        return passwordTokenRepository.save(passwordToken);
    }

    @Override
    public List<PasswordToken> getPasswordTokens(PasswordToken passwordToken) {
        PasswordTokenPredicate predicate = new PasswordTokenPredicate(passwordToken);
        return passwordTokenRepository.findAll(predicate);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        List<User> users = userService.getUsers(new User(validPhoneNumber(username) ? null : username,
                validPhoneNumber(username) ? formartedPhoneNumber(username) : null), null, PageRequest.of(0, 1, Sort.Direction.DESC, "id")).getContent();
        if (users.isEmpty()) {
            throw new UsernameNotFoundException("Invalid user credentials");
        }
        return users.get(0);
    }

    @Override
    public Page<User> getUsers(User user, String fullName, PageRequest pageRequest) {
        UserPredicate predicate = new UserPredicate(user, fullName);
        return userRepository.findAll(predicate, pageRequest);
    }
}
