package com.yunhalee.walkerholic.common.controller;

import com.yunhalee.walkerholic.common.dto.PaypalClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    @Value("${paypal.client.id}")
    private String PAYPAL_CLIENT_ID;

    @Value("${paypal.client.secret}")
    private String PAYPAL_CLIENT_SECRET;

    @GetMapping("/paypal")
    public PaypalClient getPAYPAL_CLIENT_ID() {
        return new PaypalClient(PAYPAL_CLIENT_ID, PAYPAL_CLIENT_SECRET);
    }
}
