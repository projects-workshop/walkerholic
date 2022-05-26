package com.yunhalee.walkerholic.common.notification.mapper;

import com.yunhalee.walkerholic.common.notification.sender.NotificationSender;
import com.yunhalee.walkerholic.user.domain.NotificationType;

public class NotificationMapper {

    private static final NotificationMaps maps = new NotificationMaps();

    public static NotificationSender of(NotificationType type) {
        return maps.getNotificationSender(type);
    }

}
