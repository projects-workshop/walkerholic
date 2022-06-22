package com.yunhalee.walkerholic.activity.api;

import com.yunhalee.walkerholic.ApiTest;
import org.springframework.mock.web.MockMultipartFile;

public class ActivityApiTest extends ApiTest {

    private static final MockMultipartFile MULTIPART_FILE = new MockMultipartFile("multipartFile", "image.png", "image/png", "image data".getBytes());


}
