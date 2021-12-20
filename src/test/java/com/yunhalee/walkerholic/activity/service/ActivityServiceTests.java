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

    private static final String UPLOAD_DIR = "activity-uploads/";

    @Test
    public void createActivity() throws IOException {
        //given
        String name = "testActivity";
        Integer score = 1;
        String description = "This is test Activity.";
        String fileName = "sampleFile.txt";
        String imageUrl = UPLOAD_DIR + fileName;

        ActivityRequest activityRequest = ActivityRequest.builder()
            .name(name)
            .score(score)
            .description(description)
            .imageUrl(imageUrl).build();

        //when
        ActivityResponse activityResponse = activityService.create(activityRequest);

        //then
        assertNotNull(activityResponse.getId());
        assertEquals(name, activityResponse.getName());
        assertEquals(score, activityResponse.getScore());
        assertEquals(description, activityResponse.getDescription());
        assertEquals(imageUrl, activityResponse.getImageUrl());
    }

    @Test
    public void updateActivity() throws IOException {
        //given
        Integer id = 1;
        String originalName = activityRepository.findById(id).get().getName();
        String name = "testUpdateActivity";
        Integer score = 1;
        String description = "This is test Activity.";
        String imageUrl = UPLOAD_DIR + "sampleFile.txt";

        ActivityRequest activityRequest = ActivityRequest.builder()
            .name(name)
            .score(score)
            .description(description)
            .imageUrl(imageUrl).build();

        //when
        ActivityResponse activityResponse = activityService.update(id, activityRequest);

        //then
        assertNotEquals(originalName, activityResponse.getName());
        assertEquals(name, activityResponse.getName());
        assertEquals(score, activityResponse.getScore());
        assertEquals(description, activityResponse.getDescription());
        assertEquals(imageUrl, activityResponse.getImageUrl());
    }

    @Test
    public void uploadImage() throws IOException {
        //given
        String fileName = "sampleFile.txt";
        MultipartFile multipartFile = new MockMultipartFile("uploaded-file",
            fileName,
            "text/plain",
            "This is the file content".getBytes());

        //when
        String imageUrl = activityService.uploadImage(multipartFile);

        //then
        assertTrue(imageUrl.contains(bucketUrl + UPLOAD_DIR));
        assertTrue(imageUrl.contains(fileName));

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
