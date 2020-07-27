package com.techminia.collection.api.service;

import com.techminia.collection.api.domain.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface NotificationService {
    Page<Notification> getNotifications(Notification notification, boolean checkSend, boolean checkSendError, PageRequest pageRequest);
    Notification saveNotification(Notification notification);
    List<Notification> saveNotifications(List<Notification> notifications);
}
