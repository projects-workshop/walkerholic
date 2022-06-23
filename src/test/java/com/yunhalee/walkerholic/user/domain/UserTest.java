package com.yunhalee.walkerholic.user.domain;

public class UserTest {

    public static final User USER = User.builder()
        .id(1)
        .firstname("testFirstName")
        .lastname("TestLastName")
        .email("test@example.com")
        .password("12345678")
        .imageUrl("test/image.png")
        .role(Role.USER).build();

    public static final User SELLER = User.builder()
        .id(2)
        .firstname("sellerFirstName")
        .lastname("sellerLastName")
        .email("sellerTestExample@example.com")
        .password("12345678")
        .imageUrl("test/image.png")
        .role(Role.SELLER).build();


    public static final User FIRST_USER = User.builder()
        .id(1)
        .firstname("testFirstName")
        .lastname("TestLastName")
        .email("test@example.com")
        .password("12345678")
        .imageUrl("test/image.png")
        .phoneNumber("01000000000")
        .description("This is testUser")
        .role(Role.USER).build();

    public static final User SECOND_USER = User.builder()
        .id(2)
        .firstname("test2FirstName")
        .lastname("Test2LastName")
        .email("test2@example.com")
        .password("12345678")
        .imageUrl("test/image.png")
        .phoneNumber("01000000000")
        .description("This is test secondUser.")
        .role(Role.USER).build();

    public static final User THIRD_USER = User.builder()
        .id(3)
        .firstname("test3FirstName")
        .lastname("Test3LastName")
        .email("test3@example.com")
        .password("12345678")
        .imageUrl("test/image.png")
        .phoneNumber("01000000000")
        .description("This is test thirdUser.")
        .role(Role.USER).build();

    public static final User FOURTH_USER = User.builder()
        .id(4)
        .firstname("test4FirstName")
        .lastname("Test4LastName")
        .email("test4@example.com")
        .password("12345678")
        .imageUrl("test/image.png")
        .phoneNumber("01000000000")
        .description("This is test fourthUser.")
        .role(Role.USER).build();



}
