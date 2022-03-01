package com.yunhalee.walkerholic.activity.service;

import com.yunhalee.walkerholic.MockBeans;
import com.yunhalee.walkerholic.activity.domain.Activity;
import com.yunhalee.walkerholic.activity.dto.ActivityRequest;
import com.yunhalee.walkerholic.activity.dto.ActivityResponse;
import com.yunhalee.walkerholic.activity.dto.ActivityDetailResponse;
import java.io.IOException;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@Transactional
class ActivityServiceTests extends MockBeans {

    private static final String UPLOAD_DIR = "activity-uploads/";
    private static final String NAME = "testActivity";
    private static final Integer SCORE = 1;
    private static final String DESCRIPTION = "This is test Activity.";
    private static final String IMAGE_URL = "http://testActivity/imageURL";

    @InjectMocks
    private ActivityService activityService;

    private Activity activity;

    @BeforeEach
    void setUp() {
        activity = Activity.builder()
            .name(NAME)
            .score(SCORE)
            .description(DESCRIPTION)
            .imageUrl(IMAGE_URL).build();
    }

    @Test
    @DisplayName("주어진 정보대로 액티비티를 생성한다.")
    public void create_activity() {
        //given
        ActivityRequest activityRequest = activityRequest(NAME, SCORE, DESCRIPTION, IMAGE_URL);
        //when
        when(activityRepository.save(any())).thenReturn(activity);
        ActivityResponse response = activityService.create(activityRequest);
        //then
        verify(activityRepository).save(any());
        Assertions.assertAll(
            () -> assertThat(response.getName()).isEqualTo(NAME),
            () -> assertThat(response.getDescription()).isEqualTo(DESCRIPTION),
            () -> assertThat(response.getScore()).isEqualTo(SCORE),
            () -> assertThat(response.getImageUrl()).isEqualTo(IMAGE_URL)
        );
    }

    @ParameterizedTest
    @CsvSource({"updateTest,3,update-activity-test,activity/imageUrl",
        "update,7,update-Test,update/imageUrl"})
    @DisplayName("액티비티를 수정한다.")
    public void update_activity(String name, int score, String description, String imageUrl) {
        //given
        ActivityRequest activityRequest = activityRequest(name, score, description, imageUrl);
        //when
        when(activityRepository.findById(anyInt())).thenReturn(java.util.Optional.of(activity));
        ActivityResponse response = activityService.update(1, activityRequest);
        //then
        Assertions.assertAll(
            () -> assertThat(response.getName()).isEqualTo(name),
            () -> assertThat(response.getDescription()).isEqualTo(description),
            () -> assertThat(response.getScore()).isEqualTo(score),
            () -> assertThat(response.getImageUrl()).isEqualTo(imageUrl)
        );
    }

    @DisplayName("액티비티 이미지를 업로드한다.")
    @Test
    public void upload_activity_image() throws IOException {
        //given
        String fileName = "sampleFile.txt";
        MultipartFile multipartFile = new MockMultipartFile("uploaded-file",
            fileName,
            "text/plain",
            "This is the file content".getBytes());
        //when
        when(s3ImageUploader.uploadFile(any(), any()))
            .thenReturn(UPLOAD_DIR + "/" + fileName);
        String imageUrl = activityService.uploadImage(multipartFile);
        //then
        assertTrue(imageUrl.contains(UPLOAD_DIR));
        assertTrue(imageUrl.contains(fileName));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    @DisplayName("단일 액티비티를 조회한다.")
    public void find_one_activity_with_id(Integer id) {
        //given
        //when
        when(activityRepository.findByActivityId(anyInt())).thenReturn(activity);
        ActivityDetailResponse response = activityService.activity(id);
        //then
        Assertions.assertAll(
            () -> assertThat(response.getName()).isEqualTo(NAME),
            () -> assertThat(response.getDescription()).isEqualTo(DESCRIPTION),
            () -> assertThat(response.getScore()).isEqualTo(SCORE),
            () -> assertThat(response.getImageUrl()).isEqualTo(IMAGE_URL)
        );
    }

    @DisplayName("모든 액티비티를 조회한다.")
    @Test
    public void find_all_activities() {
        //given
        //when
        when(activityRepository.findAll()).thenReturn(Arrays.asList(activity));
        List<ActivityResponse> activityResponses = activityService.activities();
        //then
        assertNotEquals(activityResponses.size(), 0);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    @DisplayName("특정 액티비티를 삭제한다.")
    public void delete_one_activity_with_id(Integer id) {
        //given
        //when
        when(activityRepository.findById(anyInt())).thenReturn(java.util.Optional.of(activity));
        activityService.delete(id);
        //then
        verify(activityRepository).delete(any());
        verify(s3ImageUploader).deleteFile(any());
    }

    private ActivityRequest activityRequest(String name, int score, String description,
        String imageUrl) {
        return ActivityRequest.builder()
            .name(name)
            .score(score)
            .description(description)
            .imageUrl(imageUrl).build();
    }
}