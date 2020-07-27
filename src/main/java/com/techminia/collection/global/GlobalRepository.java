package com.techminia.collection.global;

import com.techminia.collection.api.repository.ClientDetailsRepository;
import com.techminia.collection.api.repository.NotificationRepository;
import com.techminia.collection.api.repository.PasswordTokenRepository;
import com.techminia.collection.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GlobalRepository {
    public static ClientDetailsRepository clientDetailsRepository;
    public static UserRepository userRepository;
    public static PasswordTokenRepository passwordTokenRepository;
    public static NotificationRepository notificationRepository;

    @Autowired
    public void setNotificationRepository(NotificationRepository notificationRepository) {
        GlobalRepository.notificationRepository = notificationRepository;
    }

    @Autowired
    public void setPasswordTokenRepository(PasswordTokenRepository passwordTokenRepository) {
        GlobalRepository.passwordTokenRepository = passwordTokenRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        GlobalRepository.userRepository = userRepository;
    }

    @Autowired
    public void setClientDetailsRepository(ClientDetailsRepository clientDetailsRepository) {
        GlobalRepository.clientDetailsRepository = clientDetailsRepository;
    }
}
