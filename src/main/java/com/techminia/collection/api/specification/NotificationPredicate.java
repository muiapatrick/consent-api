package com.techminia.collection.api.specification;

import com.techminia.collection.api.domain.Notification;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class NotificationPredicate implements Specification<Notification> {
    private Notification model;
    private boolean checkSend, checkSendingError;

    public NotificationPredicate(Notification model, boolean checkSend, boolean checkSendingError) {
        this.model = model;
        this.checkSend = checkSend;
        this.checkSendingError = checkSendingError;
    }


    @Override
    public Predicate toPredicate(Root<Notification> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        Predicate p = cb.and();

        if (model != null) {
            if (model.getId() != null) {
                p.getExpressions().add(cb.equal(root.get("id"), model.getId()));
            }

            if (model.getNotificationType() != null) {
                p.getExpressions().add(cb.equal(root.get("notificationType"), model.getNotificationType()));
            }

            if (model.getNotificationCategory() != null) {
                p.getExpressions().add(cb.equal(root.get("notificationCategory"), model.getNotificationCategory()));
            }

        }

        if (checkSend) {
            p.getExpressions().add(cb.equal(root.get("send"), model.isSend()));
        }

        if (checkSendingError) {
            p.getExpressions().add(cb.equal(root.get("sendingError"), model.isSendingError()));
        }

        return p;
    }
}
