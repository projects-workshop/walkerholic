package com.yunhalee.walkerholic.activity.service;

import com.yunhalee.walkerholic.activity.domain.Activity;
import com.yunhalee.walkerholic.activity.domain.FakeActivityRepository;
import com.yunhalee.walkerholic.activity.dto.ActivityRequest;
import com.yunhalee.walkerholic.activity.dto.ActivityResponse;
import com.yunhalee.walkerholic.activity.dto.ActivityDetailResponse;
import com.yunhalee.walkerholic.activity.domain.ActivityRepository;
import com.yunhalee.walkerholic.common.service.FakeS3ImageUploader;
import com.yunhalee.walkerholic.common.service.S3ImageUploader;
import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class ActivityServiceTests {

    private static final String UPLOAD_DIR = "activity-uploads/";
    private static final String NAME = "testActivity";
    private static final Integer SCORE = 1;
    private static final String DESCRIPTION = "This is test Activity.";
    private static final String IMAGE_URL = "http://testActivity/imageURL";

    private ActivityRepository activityRepository = new FakeActivityRepository();

    private S3ImageUploader s3ImageUploader = new FakeS3ImageUploader();

    private ActivityService activityService = new ActivityService(activityRepository,
        s3ImageUploader);

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
        ActivityResponse response = activityService.create(activityRequest);

        //then
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
        activityService.delete(id);

        //then
        s3ImageUploader.listFolder("").forEach(file -> assertThat(file).isNotEqualTo(IMAGE_URL));
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
