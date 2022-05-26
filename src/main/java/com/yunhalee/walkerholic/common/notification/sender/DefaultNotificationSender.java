package com.yunhalee.walkerholic.common.notification.sender;

import com.yunhalee.walkerholic.order.domain.Order;
import com.yunhalee.walkerholic.user.domain.User;

public class DefaultNotificationSender implements NotificationSender{

    @Override
    public void sendCreateOrderNotification(Order order, User user) {

    }

    @Override
    public void sendCancelOrderNotification(Order order, User user) {

    }
}
