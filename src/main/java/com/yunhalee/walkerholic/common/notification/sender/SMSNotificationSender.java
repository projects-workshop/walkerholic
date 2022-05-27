package com.yunhalee.walkerholic.common.notification.sender;

import com.yunhalee.walkerholic.common.exception.InvalidValueException;
import com.yunhalee.walkerholic.order.domain.Order;
import com.yunhalee.walkerholic.user.domain.User;
import java.util.HashMap;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;

@Async
public class SMSNotificationSender implements NotificationSender {

    @Value("COOL_SMS_API_KEY")
    private String apiKey;

    @Value("COOL_SMS_API_SECRET")
    private String apiSecret;

    @Value("COOL_SMS_PHONE_NUMBER")
    private String sender;

    @Value("${base-url}")
    private String baseUrl;

    @Override
    public void sendCreateOrderNotification(Order order, User user) {
        sendSMS(user.getPhoneNumber(),
            "Hello" + user.getFirstname() + "! Your order has been made successfully. " +
                "\n\nOrder Id :  " + order.getId() +
                "\nTotal Amount : " + order.getTotalAmount() +
                "\nPaid At : " + order.getPaidAt() +
                "\n\nFor more Details visit " + baseUrl + "/order/" + order.getId());
    }

    @Override
    public void sendCancelOrderNotification(Order order, User user) {
        sendSMS(user.getEmail(),
            "Hello" + user.getFirstname() + "! Your order has been canceled successfully. " +
                "\n\nOrder Id :  " + order.getId() +
                "\nTotal Amount : " + order.getTotalAmount() +
                "\nCanceled At : " + order.getUpdatedAt() +
                "\n\nFor more Details visit " + baseUrl + "/order/" + order.getId());
    }

    private void sendSMS(String to, String text) {
        Message sms = new Message(apiKey, apiSecret);
        HashMap<String, String> params = new HashMap<>();
        params.put("to", to);
        params.put("from", sender);
        params.put("type", "SMS");
        params.put("text", text);
        params.put("app_version", "test app 1.2");

        try {
            sms.send(params);
        } catch (CoolsmsException e) {
            throw new InvalidValueException(e.getCode() + " Cannot send sms :" + e.getMessage());
        }
    }

}
