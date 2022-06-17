package com.yunhalee.walkerholic.common.notification.sender;

import com.yunhalee.walkerholic.order.domain.Order;
import com.yunhalee.walkerholic.user.domain.User;
import org.springframework.scheduling.annotation.Async;

public class DefaultNotificationSender implements NotificationSender{

    @Override
    @Async
    public void sendCreateOrderNotification(Order order, User user) {
    }

    @Override
    @Async
    public void sendCancelOrderNotification(Order order, User user) {
    }

    @Override
    @Async
    public void sendForgotPasswordNotification(User user, String tempPassword) {
    }


}
