package com.yunhalee.walkerholic.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaypalClient {

    private String clientId;
    private String clientSecret;

    public PaypalClient(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }
}
