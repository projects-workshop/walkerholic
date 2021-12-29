package com.yunhalee.walkerholic.activity.service;

import com.yunhalee.walkerholic.activity.domain.Activity;
import com.yunhalee.walkerholic.activity.dto.ActivityRequest;
import com.yunhalee.walkerholic.activity.dto.ActivityResponse;
import com.yunhalee.walkerholic.activity.dto.ActivityDetailResponse;
import com.yunhalee.walkerholic.activity.domain.ActivityRepository;
import com.yunhalee.walkerholic.common.service.S3ImageUploader;
import java.io.IOException;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

    @Value("${AWS_S3_BASE_IMAGE_URL}")
    private String defaultImageUrl;

    private ActivityRequest activityRequest;

    private static final String UPLOAD_DIR = "activity-uploads/";
    private static final String NAME = "testActivity";
    private static final Integer SCORE = 1;
    private static final String DESCRIPTION = "This is test Activity.";
    private static final String IMAGE_URL = "http://testActivity/imageURL";

    @BeforeEach
    public void setUp() {
        activityRequest = ActivityRequest.builder()
            .name(NAME)
            .score(SCORE)
            .description(DESCRIPTION)
            .imageUrl(IMAGE_URL).build();
    }


    @DisplayName("액티비티를 생성한다.")
    @Test
    public void createActivity() {
        //given

        //when
        ActivityResponse activityResponse = activityService.create(activityRequest);

        //then
        assertNotNull(activityResponse.getId());
        assertEquals(NAME, activityResponse.getName());
        assertEquals(SCORE, activityResponse.getScore());
        assertEquals(DESCRIPTION, activityResponse.getDescription());
        assertEquals(IMAGE_URL, activityResponse.getImageUrl());
    }

    @DisplayName("액티비티를 수정한다.")
    @Test
    public void updateActivity() {
        //given
        Integer id = 1;
        String originalName = activityRepository.findById(id).get().getName();

        //when
        ActivityResponse activityResponse = activityService.update(id, activityRequest);

        //then
        assertNotEquals(originalName, activityResponse.getName());
        assertEquals(NAME, activityResponse.getName());
        assertEquals(SCORE, activityResponse.getScore());
        assertEquals(DESCRIPTION, activityResponse.getDescription());
        assertEquals(IMAGE_URL, activityResponse.getImageUrl());
    }

    @DisplayName("액티비티 이미지를 업로드한다.")
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

    @DisplayName("단일 액티비티를 조회한다.")
    @Test
    public void getActivity() {
        //given
        Integer id = 1;

        //when
        ActivityDetailResponse activityDetailResponse = activityService.activity(id);

        //then
        assertEquals(id, activityDetailResponse.getId());
    }

    @DisplayName("모든 액티비티를 조회한다.")
    @Test
    public void getActivities() {
        //given

        //when
        List<ActivityResponse> activityResponses = activityService.activities();

        //then
        assertNotEquals(activityResponses.size(), 0);
    }

    @DisplayName("특정 액티비티를 삭제한다.")
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
