package com.yunhalee.walkerholic.common.notification.mapper;

import com.yunhalee.walkerholic.common.notification.sender.DefaultNotificationSender;
import com.yunhalee.walkerholic.common.notification.sender.MailNotificationSender;
import com.yunhalee.walkerholic.common.notification.sender.NotificationSender;
import com.yunhalee.walkerholic.common.notification.sender.SMSNotificationSender;
import com.yunhalee.walkerholic.common.notification.exception.InvalidNotificationTypeException;
import com.yunhalee.walkerholic.user.domain.NotificationType;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class NotificationMaps {

    private static final Set<NotificationMap> maps;

    static {
        maps = new LinkedHashSet<>(Arrays.asList(
            NotificationMap.of(NotificationType.MAIL, new MailNotificationSender()),
            NotificationMap.of(NotificationType.SMS, new SMSNotificationSender()),
            NotificationMap.of(NotificationType.NONE, new DefaultNotificationSender())
        ));
    }

    public NotificationMaps() {
    }

    public NotificationSender getNotificationSender(NotificationType type) {
        return this.maps.stream()
            .filter(map -> map.isMatchType(type))
            .findFirst()
            .orElseThrow(() -> new InvalidNotificationTypeException("This notification type does not exists : " + type))
            .getNotificationSender();
    }
}
