package com.yunhalee.walkerholic.common.notification.sender;

import com.yunhalee.walkerholic.order.domain.Order;
import com.yunhalee.walkerholic.user.domain.User;

public interface NotificationSender {

    void sendCreateOrderNotification(Order order, User user);

    void sendCancelOrderNotification(Order order, User user);

    void sendForgotPasswordNotification(User user, String tempPassword);
}
