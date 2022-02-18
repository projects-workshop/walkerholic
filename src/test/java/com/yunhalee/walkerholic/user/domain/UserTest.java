package com.yunhalee.walkerholic.user.domain;

public class UserTest {

    public static final User USER = new User(1,
        "testFirstName",
        "TestLastName",
        "test@example.com",
        "12345678",
        Role.USER);

    public static final User SELLER = new User(2,
        "sellerFirstName",
        "sellerLastName",
        "sellerTestExample@example.com",
        "12345678",
        Role.SELLER);

}
