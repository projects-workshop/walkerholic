package com.yunhalee.walkerholic.follow.domain;

import static org.junit.jupiter.api.Assertions.*;

import com.yunhalee.walkerholic.user.domain.UserTest;

public class FollowTest {

    public static final Follow FOLLOW = Follow.follow(UserTest.USER, UserTest.SELLER);
}