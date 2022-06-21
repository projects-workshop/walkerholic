package com.yunhalee.walkerholic.activity;

import com.yunhalee.walkerholic.AcceptanceTest;
import com.yunhalee.walkerholic.activity.dto.ActivityRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.io.File;
import org.junit.jupiter.api.Test;

public class ActivityAcceptanceTest extends AcceptanceTest {

    private static final String NAME = "activity";
    private static final Integer SCORE = 3;
    private static final String DESCRIPTION = "This is activity test.";

    String imageUrl = "https://walkerholic-with-you.s3.ap-northeast-2.amazonaws.com/testImage/image.jpeg";


    @Test
    void manage_Activity() {
        // when
        ExtractableResponse<Response> createImageResponse = create_activity_image_request(imageFile);
        // then
        check_activity_image_created(createImageResponse);

        // given
        String imageUrl = createImageResponse.response().toString();
        // when
        ExtractableResponse<Response> createResponse = create_activity_request(NAME, SCORE, DESCRIPTION, imageUrl);

        System.out.println(createResponse.response().asString());
    }


    public static ExtractableResponse<Response> create_activity_image_request(File imageFile) {
        return RestAssured
            .given().log().all()
            .contentType("multipart/form-data")
            .multiPart("multipartFile", imageFile, "image/jpeg")
            .when().post("/api/activities/images")
            .then().log().all()
            .extract();
    }

    private void check_activity_image_created(ExtractableResponse<Response> createImageResponse) {
        check_ok_response(createImageResponse);
    }


    private ExtractableResponse<Response> create_activity_request(String name, Integer score, String description, String imageUrl) {
        ActivityRequest request = ActivityRequest.builder()
            .name(name)
            .score(score)
            .description(description)
            .imageUrl(imageUrl).build();
        return create_request(request, "/activities", "");
    }

}
