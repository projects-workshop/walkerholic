package com.yunhalee.walkerholic.activity.service;

import com.yunhalee.walkerholic.activity.dto.ActivityRequest;
import com.yunhalee.walkerholic.activity.dto.ActivityResponse;
import com.yunhalee.walkerholic.activity.dto.ActivityDetailResponse;
import com.yunhalee.walkerholic.activity.domain.ActivityRepository;
import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ActivityServiceTests {

    @Autowired
    ActivityService activityService;

    @Autowired
    ActivityRepository activityRepository;

    @Value("${AWS_S3_BUCKET_URL}")
    private String AWS_S3_BUCKET_URL;

    @Test
    public void createActivity() throws IOException {
        //given
        String name = "testActivity";
        Integer score = 1;
        String description = "This is test Activity.";

        ActivityRequest activityRequest = new ActivityRequest(name, score, description);
        MultipartFile multipartFile = new MockMultipartFile("uploaded-file",
            "sampleFile.txt",
            "text/plain",
            "This is the file content".getBytes());
        //when
        ActivityResponse activityResponse1 = activityService
            .create(activityRequest, multipartFile);

        //then
        assertNotNull(activityResponse1.getId());
        assertEquals(name, activityResponse1.getName());
        assertEquals(score, activityResponse1.getScore());
        assertEquals(description, activityResponse1.getDescription());
        assertTrue(activityResponse1.getImageUrl()
            .contains(AWS_S3_BUCKET_URL + "activityUploads/" + activityResponse1.getId() + "/"));
        assertTrue(activityResponse1.getImageUrl().contains("sampleFile.txt"));
    }

    @Test
    public void updateActivity() throws IOException {
        //given
        Integer id = 1;
        String originalName = activityRepository.findById(id).get().getName();

        String name = "testUpdateActivity";
        Integer score = 1;
        String description = "This is test Activity.";
        ActivityRequest activityRequest = new ActivityRequest(name, score, description);

        //when
        ActivityResponse activityResponse1 = activityService
            .update(id, activityRequest, null);

        //then
        assertNotEquals(originalName, activityResponse1.getName());
    }

    @Test
    public void getActivity() {
        //given
        Integer id = 1;

        //when
        ActivityDetailResponse activityDetailResponse = activityService.getActivity(id);

        //then
        assertEquals(id, activityDetailResponse.getId());
    }

    @Test
    public void getActivities() {
        //given

        //when
        List<ActivityResponse> activityResponses = activityService.getActivities();

        //then
        assertEquals(1, activityResponses.size());
    }

    @Test
    public void deleteActivity() {
        //given
        Integer id = 1;

        //when
        activityService.deleteActivity(id);

        //then
        assertNull(activityRepository.findById(id));
    }
}
