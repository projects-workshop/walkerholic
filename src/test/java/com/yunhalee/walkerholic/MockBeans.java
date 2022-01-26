package com.yunhalee.walkerholic;

import com.yunhalee.walkerholic.activity.domain.ActivityRepository;
import com.yunhalee.walkerholic.common.service.S3ImageUploader;
import com.yunhalee.walkerholic.user.domain.UserRepository;
import com.yunhalee.walkerholic.useractivity.domain.UserActivityRepository;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MockBeans {

    @MockBean
    protected UserActivityRepository userActivityRepository;

    @MockBean
    protected ActivityRepository activityRepository;

    @MockBean
    protected UserRepository userRepository;

    @MockBean
    protected S3ImageUploader s3ImageUploader;
}
