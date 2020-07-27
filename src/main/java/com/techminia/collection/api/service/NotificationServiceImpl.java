package com.techminia.collection.api.service;

import com.techminia.collection.api.domain.Notification;
import com.techminia.collection.api.specification.NotificationPredicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.techminia.collection.global.GlobalRepository.notificationRepository;

@Service("notificationService")
@Transactional
public class NotificationServiceImpl implements NotificationService {
    @Override
    public Page<Notification> getNotifications(Notification notification, boolean checkSend, boolean checkSendError, PageRequest pageRequest) {
        NotificationPredicate predicate = new NotificationPredicate(notification, checkSend, checkSendError);
        return notificationRepository.findAll(predicate, pageRequest);
    }

    @Override
    public Notification saveNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> saveNotifications(List<Notification> notifications) {
        return notificationRepository.saveAll(notifications);
    }
}
