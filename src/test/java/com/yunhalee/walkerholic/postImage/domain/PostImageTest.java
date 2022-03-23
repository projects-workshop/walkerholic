package com.yunhalee.walkerholic.postImage.domain;

import com.yunhalee.walkerholic.postImage.domain.PostImage;

public class PostImageTest {

    public static final PostImage POST_IMAGE = PostImage.builder()
        .id(1)
        .name("testImageUrl")
        .filePath("https://walkerholic-test-you.s3.ap-northeast10.amazonaws.com/postUploads/testImageUrl").build();
}
