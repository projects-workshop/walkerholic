package com.yunhalee.walkerholic.useractivity;

import static com.yunhalee.walkerholic.activity.ActivityAcceptanceTest.check_activity_created;
import static com.yunhalee.walkerholic.activity.ActivityAcceptanceTest.create_activity_request;
import static com.yunhalee.walkerholic.user.UserAcceptanceTest.check_user_created;
import static com.yunhalee.walkerholic.user.UserAcceptanceTest.create_user_request;

import com.yunhalee.walkerholic.AcceptanceTest;
import com.yunhalee.walkerholic.useractivity.dto.UserActivityRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class UserActivityAcceptanceTest extends AcceptanceTest {

    private static final Integer DISTANCE = 10;
    private static final boolean FINISHED = true;

    private Integer userId;
    private String token;
    private Integer activityId;

    void user_activity_set_up() {
        // when
        ExtractableResponse<Response> userCreateResponse = create_user_request(TEST_IMAGE_URL);
        // then
        check_user_created(userCreateResponse);
        userId = userCreateResponse.body().jsonPath().getInt("user.id");
        token = userCreateResponse.body().jsonPath().getString("token");

        // when
        ExtractableResponse<Response> activityCreateResponse = create_activity_request(
            TEST_IMAGE_URL, token);
        // then
        check_activity_created(activityCreateResponse);
        activityId = activityCreateResponse.body().jsonPath().getInt("id");
    }

    @Test
    void manage_user_activity() {
        // given
        user_activity_set_up();
        // when
        ExtractableResponse<Response> createResponse = create_user_activity_request();
        // then
        check_user_activity_created(createResponse);

        // when
        ExtractableResponse<Response> findResponse = find_user_activities_request();
        // then
        check_user_activities_found(findResponse);

        // when
        ExtractableResponse<Response> updateResponse = update_user_activity_request(createResponse);
        // then
        check_user_activity_updated(updateResponse);

        // when
        ExtractableResponse<Response> deleteResponse = delete_user_activity_request(createResponse);
        // then
        check_user_activity_deleted(deleteResponse);
    }

    public ExtractableResponse<Response> create_user_activity_request() {
        UserActivityRequest request = UserActivityRequest.builder()
            .userId(userId)
            .activityId(activityId)
            .distance(DISTANCE)
            .finished(FINISHED).build();
        return create_request(request, "/user-activities", token);
    }

    public static void check_user_activity_created(ExtractableResponse<Response> response) {
        check_ok_response(response);
    }


    private ExtractableResponse<Response> find_user_activities_request() {
        Integer page = 1;
        return find_request("/users/" + userId + "/user-activities?page=" + page, token);
    }

    private void check_user_activities_found(ExtractableResponse<Response> response) {
        check_ok_response(response);
    }

    private ExtractableResponse<Response> update_user_activity_request(
        ExtractableResponse<Response> response) {
        Integer id = response.body().jsonPath().getInt("id");
        UserActivityRequest request = UserActivityRequest.builder()
            .userId(userId)
            .activityId(activityId)
            .distance(DISTANCE)
            .finished(FINISHED).build();
        return update_Request(request, "/user-activities/" + id, token);
    }

    private void check_user_activity_updated(ExtractableResponse<Response> updateResponse) {
        check_ok_response(updateResponse);
    }

    private ExtractableResponse<Response> delete_user_activity_request(
        ExtractableResponse<Response> response) {
        Integer id = response.body().jsonPath().getInt("id");
        return delete_request("/users/" + userId + "/user-activities/" + id, token);
    }

    private void check_user_activity_deleted(ExtractableResponse<Response> response) {
        check_ok_response(response);
    }


}
