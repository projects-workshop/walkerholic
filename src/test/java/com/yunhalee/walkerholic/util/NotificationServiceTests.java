package com.yunhalee.walkerholic.util;

import com.yunhalee.walkerholic.MockBeans;
import com.yunhalee.walkerholic.common.service.NotificationService;
import com.yunhalee.walkerholic.order.domain.Order;
import com.yunhalee.walkerholic.order.domain.OrderTest;
import com.yunhalee.walkerholic.user.domain.NotificationType;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.user.domain.UserTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;


class NotificationServiceTests extends MockBeans {

    private Order order;
    private User user;

    @InjectMocks
    NotificationService notificationService = new NotificationService(
        mailNotificationSender,
        slackNotificationSender);

    @BeforeEach
    public void setUp() {
        order = OrderTest.ORDER;
        user = UserTest.USER;
        user.setNotificationType(NotificationType.MAIL);
    }


    @Test
    public void createOrderMailNotification() {
        notificationService.sendCreateOrderNotification(order, user);
        verify(mailNotificationSender).sendCreateOrderNotification(any(), any());
    }

}
