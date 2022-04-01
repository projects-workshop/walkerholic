package com.yunhalee.walkerholic.common.service;

import com.yunhalee.walkerholic.common.service.notificationSender.MailNotificationSender;
import com.yunhalee.walkerholic.common.service.notificationSender.NotificationSender;
import com.yunhalee.walkerholic.common.service.notificationSender.SlackNotificationSender;
import com.yunhalee.walkerholic.order.domain.Order;
import com.yunhalee.walkerholic.user.domain.NotificationType;
import com.yunhalee.walkerholic.user.domain.User;
import org.springframework.scheduling.annotation.Async;

public class NotificationService {

    private NotificationSender notificationSender;
    private MailNotificationSender mailNotificationSender;
    private SlackNotificationSender slackNotificationSender;

    public NotificationService() {
        this.mailNotificationSender = new MailNotificationSender();
        this.slackNotificationSender = new SlackNotificationSender();
    }

    public NotificationService(MailNotificationSender mailNotificationSender, SlackNotificationSender slackNotificationSender) {
        this.mailNotificationSender = mailNotificationSender;
        this.slackNotificationSender = slackNotificationSender;
    }

    @Async
    public void sendCreateOrderNotification(Order order, User user) {
        setNotificationSender(user.getNotificationType());
        notificationSender.sendCreateOrderNotification(order, user);
    }

    @Async
    public void sendCancelOrderNotification(Order order, User user) {
        setNotificationSender(user.getNotificationType());
        notificationSender.sendCreateOrderNotification(order, user);
    }

    private void setNotificationSender(NotificationType notificationType) {
        if (notificationType == NotificationType.SLACK) {
            this.notificationSender = slackNotificationSender;
            return;
        }
        this.notificationSender = mailNotificationSender;
    }
}
