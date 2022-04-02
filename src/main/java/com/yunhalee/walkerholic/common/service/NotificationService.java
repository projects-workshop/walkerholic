package com.yunhalee.walkerholic.common.service;

import com.yunhalee.walkerholic.common.service.notificationSender.MailNotificationSender;
import com.yunhalee.walkerholic.common.service.notificationSender.NotificationSender;
import com.yunhalee.walkerholic.common.service.notificationSender.SMSNotificationSender;
import com.yunhalee.walkerholic.order.domain.Order;
import com.yunhalee.walkerholic.user.domain.NotificationType;
import com.yunhalee.walkerholic.user.domain.User;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class NotificationService {

    private NotificationSender notificationSender;
    private MailNotificationSender mailNotificationSender;
    private SMSNotificationSender SMSNotificationSender;

    public NotificationService(MailNotificationSender mailNotificationSender, SMSNotificationSender SMSNotificationSender) {
        this.mailNotificationSender = mailNotificationSender;
        this.SMSNotificationSender = SMSNotificationSender;
    }

    @Async
    public void sendCreateOrderNotification(Order order, User user) {
        setNotificationSender(user.getNotificationType());
        notificationSender.sendCreateOrderNotification(order, user);
    }

    @Async
    public void sendCancelOrderNotification(Order order, User user) {
        setNotificationSender(user.getNotificationType());
        notificationSender.sendCancelOrderNotification(order, user);
    }

    private void setNotificationSender(NotificationType notificationType) {
        if (notificationType == NotificationType.SLACK) {
            this.notificationSender = SMSNotificationSender;
            return;
        }
        this.notificationSender = mailNotificationSender;
    }
}
