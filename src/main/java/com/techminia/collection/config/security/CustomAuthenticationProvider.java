package com.techminia.collection.config.security;

import com.techminia.collection.global.GlobalService;
import com.techminia.collection.global.GlobalValues;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static com.techminia.collection.global.GlobalService.userService;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private static final Logger logger = LogManager.getLogger(CustomAuthenticationProvider.class);

    @Autowired
    private HttpServletRequest request;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication.getName()  == null || authentication.getName().trim().isEmpty()) {
            throw  new UnapprovedClientAuthenticationException("Invalid credentials :: Username missing for password grant type");
        }

        if (authentication.getCredentials() == null || authentication.getCredentials().toString().trim().isEmpty()) {
            throw new UnapprovedClientAuthenticationException("Invalid credentials :: Password missing for password grant type");
        }

        String username = authentication.getName().trim();
        String password = authentication.getCredentials().toString().trim();

        UserDetails userDetails = userService.loadUserByUsername(username);

        System.out.println("VALID : "+ GlobalValues.passwordEncoder.matches(password, userDetails.getPassword()));
        logger.info("USERNAME :: "+userDetails.getUsername());
        if (GlobalValues.passwordEncoder.matches(password, userDetails.getPassword())){
            Authentication authenticatedCreds = SecurityContextHolder.getContext().getAuthentication();
            GlobalService.securityService.revokeAccessTokenM(authenticatedCreds.getName(), userDetails.getUsername(), true);
            Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities() );
            return auth;
        }
        else {
//            GlobalService.userService.updateUserAttempts(userDetails, false);
            throw  new UnapprovedClientAuthenticationException("Invalid credentials");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
