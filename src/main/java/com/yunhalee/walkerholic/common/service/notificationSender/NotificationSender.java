package com.yunhalee.walkerholic.common.service.notificationSender;

import com.yunhalee.walkerholic.order.domain.Order;
import com.yunhalee.walkerholic.user.domain.User;

public interface NotificationSender {

    void sendCreateOrderNotification(Order order, User user);

    void sendCancelOrderNotification(Order order, User user);
}
