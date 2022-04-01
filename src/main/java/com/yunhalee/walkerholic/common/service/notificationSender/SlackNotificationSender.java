package com.yunhalee.walkerholic.common.service.notificationSender;

import com.yunhalee.walkerholic.order.domain.Order;
import com.yunhalee.walkerholic.user.domain.User;

public class SlackNotificationSender implements NotificationSender {

    @Override
    public void sendCreateOrderNotification(Order order, User user) {

    }

    @Override
    public void sendCancelOrderNotification(Order order, User user) {

    }
}
