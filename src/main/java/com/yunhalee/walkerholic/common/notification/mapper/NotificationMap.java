package com.yunhalee.walkerholic.common.notification.mapper;

import com.yunhalee.walkerholic.common.notification.sender.NotificationSender;
import com.yunhalee.walkerholic.user.domain.NotificationType;

public class NotificationMap {

    private NotificationType type;
    private NotificationSender notificationSender;

    private NotificationMap(NotificationType type, NotificationSender notificationSender) {
        this.type = type;
        this.notificationSender = notificationSender;
    }

    public static NotificationMap of(NotificationType type, NotificationSender notificationSender) {
        return new NotificationMap(type, notificationSender);
    }

    public boolean isMatchType(NotificationType type) {
        return this.type == type;
    }

    public NotificationSender getNotificationSender() {
        return notificationSender;
    }

}
