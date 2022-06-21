package com.yunhalee.walkerholic.activity;

import static com.yunhalee.walkerholic.user.UserAcceptanceTest.user_token;

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
    private String token;

    @Test
    void manage_Activity() {
        // given
        token = user_token();
        // when
        ExtractableResponse<Response> createImageResponse = create_activity_image_request(imageFile);
        // then
        check_activity_image_created(createImageResponse);

        // given
        String imageUrl = createImageResponse.body().asString();
        // when
        ExtractableResponse<Response> createResponse = create_activity_request(imageUrl, token);
        // then
        check_activity_created(createResponse);

        // when
        ExtractableResponse<Response> findResponse = find_activity_request(createResponse);
        // then
        check_activity_found(findResponse);

        // when
        ExtractableResponse<Response> findActivitiesResponse = find_activities_request();
        // then
        check_activities_found(findActivitiesResponse);

        // when
        ExtractableResponse<Response> updateResponse = update_activity_request(createResponse);
        // then
        check_activity_updated(updateResponse);

        // when
        ExtractableResponse<Response> deleteResponse = delete_activity_request(createResponse);
        // then
        check_activity_deleted(deleteResponse);

    }


    public ExtractableResponse<Response> create_activity_image_request(File imageFile) {
        return RestAssured
            .given().log().all()
            .header("Authorization", "Bearer " + token)
            .contentType("multipart/form-data")
            .multiPart("multipartFile", imageFile, "image/jpeg")
            .when().post("/api/activities/images")
            .then().log().all()
            .extract();
    }

    private void check_activity_image_created(ExtractableResponse<Response> response) {
        check_ok_response(response);
    }


    public static ExtractableResponse<Response> create_activity_request(String imageUrl,
        String token) {
        ActivityRequest request = ActivityRequest.builder()
            .name(NAME)
            .score(SCORE)
            .description(DESCRIPTION)
            .imageUrl(imageUrl).build();
        return create_request(request, "/activities", token);
    }

    public static void check_activity_created(ExtractableResponse<Response> response) {
        check_ok_response(response);
    }

    private ExtractableResponse<Response> update_activity_request(ExtractableResponse<Response> response) {
        Integer id = response.body().jsonPath().getInt("id");
        ActivityRequest request = ActivityRequest.builder()
            .name(NAME)
            .score(SCORE)
            .description(DESCRIPTION)
            .imageUrl(response.body().jsonPath().getString("imageUrl")).build();
        return update_Request(request, "/activities/" + id, token);
    }

    private void check_activity_updated(ExtractableResponse<Response> updateResponse) {
        check_ok_response(updateResponse);
    }

    private ExtractableResponse<Response> find_activity_request(ExtractableResponse<Response> response) {
        Integer id = response.body().jsonPath().getInt("id");
        return find_request("/activities/" + id, token);
    }

    private void check_activity_found(ExtractableResponse<Response> response) {
        check_ok_response(response);
    }

    private ExtractableResponse<Response> find_activities_request() {
        return find_request("/activities", token);
    }

    private void check_activities_found(ExtractableResponse<Response> response) {
        check_ok_response(response);
    }

    private ExtractableResponse<Response> delete_activity_request(ExtractableResponse<Response> response) {
        Integer id = response.body().jsonPath().getInt("id");
        return delete_request("/users/" + id, token);
    }

    private void check_activity_deleted(ExtractableResponse<Response> response) {
        check_delete_response(response);
    }


}
