package com.techminia.collection.global;

import com.techminia.collection.api.service.ISecurityService;
import com.techminia.collection.api.service.NotificationService;
import com.techminia.collection.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.stereotype.Component;

@Component
public class GlobalService {
    public static UserService userService;
    public static ConsumerTokenServices tokenServices;
    public static ISecurityService securityService;
    public static NotificationService notificationService;

    @Autowired
    public void setNotificationService(NotificationService notificationService) {
        GlobalService.notificationService = notificationService;
    }

    @Autowired
    public void setTokenServices(ConsumerTokenServices tokenServices) {
        GlobalService.tokenServices = tokenServices;
    }

    @Autowired
    public void setUserService(UserService userService) {
        GlobalService.userService = userService;
    }

    @Autowired
    public void setSecurityService(ISecurityService securityService) {
        GlobalService.securityService = securityService;
    }
}
