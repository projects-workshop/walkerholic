package com.yunhalee.walkerholic.activity.domain;

public class ActivityTest {

    public static final Activity ACTIVITY = Activity.builder()
        .name("testActivity")
        .score(500)
        .description("test-activity").build();

    public static final Activity FIRST_ACTIVITY = Activity.builder()
        .id(1)
        .name("firstActivity")
        .score(20)
        .imageUrl("firstActivity/image.png")
        .description("This is first activity.").build();

    public static final Activity SECOND_ACTIVITY = Activity.builder()
        .id(2)
        .name("secondActivity")
        .score(50)
        .imageUrl("firstActivity/image.png")
        .description("This is second activity.").build();


}
