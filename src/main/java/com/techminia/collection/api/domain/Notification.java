package com.techminia.collection.api.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "notifications")
@Getter @Setter
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="created_at", insertable=false, updatable=false)
    private Date createdAt;

    @Column(name="send_at")
    private Date sendAt;

    @Column(name = "is_send")
    private boolean send;

    @Column(name = "send_error")
    private boolean sendingError;

    @Column(name = "notification_type")
    private Integer notificationType;

    @Column(name = "notification_category")
    private Integer notificationCategory;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "name")
    private String name;

    @Column(name = "message")
    private String message;

    @Column(name = "f_token")
    private String fToken;

    @Column(name = "send_comment")
    private String sendComment;

    public Notification() {
    }

    public Notification(boolean send, boolean sendingError, Integer notificationType, Integer notificationCategory) {
        this.send = send;
        this.sendingError = sendingError;
        this.notificationType = notificationType;
        this.notificationCategory = notificationCategory;
    }

    public Notification(Integer notificationType, Integer notificationCategory, String message) {
        this.notificationType = notificationType;
        this.notificationCategory = notificationCategory;
        this.message = message;
    }

    public Notification(Integer notificationType, Integer notificationCategory, String message, String fToken) {
        this.notificationType = notificationType;
        this.notificationCategory = notificationCategory;
        this.message = message;
        this.fToken = fToken;
    }

    public Notification(Integer notificationType, Integer notificationCategory, String mobileNumber, String name, String message) {
        this.notificationType = notificationType;
        this.notificationCategory = notificationCategory;
        this.mobileNumber = mobileNumber;
        this.name = name;
        this.message = message;
    }

    public Notification(Integer notificationType, Integer notificationCategory, String mobileNumber, String emailAddress, String name, String message) {
        this.notificationType = notificationType;
        this.notificationCategory = notificationCategory;
        this.mobileNumber = mobileNumber;
        this.emailAddress = emailAddress;
        this.name = name;
        this.message = message;
    }
}
