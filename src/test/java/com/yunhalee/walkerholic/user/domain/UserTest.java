package com.yunhalee.walkerholic.user.domain;

public class UserTest {

    public static final User USER = User.builder()
        .id(1)
        .firstname("testFirstName")
        .lastname("TestLastName")
        .email("test@example.com")
        .password("12345678")
        .role(Role.USER).build();

    public static final User SELLER = User.builder()
        .id(2)
        .firstname("sellerFirstName")
        .lastname("sellerLastName")
        .email("sellerTestExample@example.com")
        .password("12345678")
        .role(Role.SELLER).build();

}
