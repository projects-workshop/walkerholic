package com.yunhalee.walkerholic.common.service;

import com.yunhalee.walkerholic.order.domain.Order;
import com.yunhalee.walkerholic.user.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Value("${base-url}")
    private String baseUrl;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendCancelOrderMail(Order order, User user){
        sendMail(user.getEmail(),
            user.getFullname() + " : Cancel Order " + order.getId(),
            "Hello" + user.getFirstname() + "! Your order has been canceled successfully. " +
                "\n\nOrder Id :  " + order.getId() +
                "\nTotal Amount : " + order.getTotalAmount() +
                "\nCanceled At : " + order.getUpdatedAt() +
                "\n\nFor more Details visit " + baseUrl + "/order/" + order.getId());
//        message.setTo(user.getEmail());
//        message.setFrom(sender);
//        message.setSubject(user.getFullname() + " : Cancel Order " + order.getId());
//        message.setText(
//            "Hello" + user.getFirstname() + "! Your order has been canceled successfully. " +
//                "\n\nOrder Id :  " + order.getId() +
//                "\nTotal Amount : " + order.getTotalAmount() +
//                "\nCanceled At : " + order.getUpdatedAt() +
//                "\n\nFor more Details visit " + baseUrl + "/order/" + order.getId());
//        mailSender.send(message);
    }

    private void sendMail(String to, String subject, String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom(sender);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }



}
