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
    private String bucketUrl;

    private static final String UPLOAD_URL = "activityUploads/";

    @Test
    public void createActivity() throws IOException {
        //given
        String name = "testActivity";
        Integer score = 1;
        String description = "This is test Activity.";
        String fileName = "sampleFile.txt";

        ActivityRequest activityRequest = new ActivityRequest(name, score, description);
        MultipartFile multipartFile = new MockMultipartFile("uploaded-file",
            fileName,
            "text/plain",
            "This is the file content".getBytes());
        //when
        ActivityResponse activityResponse = activityService
            .create(activityRequest, multipartFile);

        //then
        assertNotNull(activityResponse.getId());
        assertEquals(name, activityResponse.getName());
        assertEquals(score, activityResponse.getScore());
        assertEquals(description, activityResponse.getDescription());
        assertTrue(activityResponse.getImageUrl()
            .contains(bucketUrl + UPLOAD_URL + activityResponse.getId() + "/"));
        assertTrue(activityResponse.getImageUrl().contains(fileName));
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
        ActivityResponse activityResponse = activityService
            .update(id, activityRequest, null);

        //then
        assertNotEquals(originalName, activityResponse.getName());
    }

    @Test
    public void getActivity() {
        //given
        Integer id = 1;

        //when
        ActivityDetailResponse activityDetailResponse = activityService.activity(id);

        //then
        assertEquals(id, activityDetailResponse.getId());
    }

    @Test
    public void getActivities() {
        //given

        //when
        List<ActivityResponse> activityResponses = activityService.activities();

        //then
        assertEquals(1, activityResponses.size());
    }

    @Test
    public void deleteActivity() {
        //given
        Integer id = 1;

        //when
        activityService.delete(id);

        //then
        assertNull(activityRepository.findById(id));
    }
}
