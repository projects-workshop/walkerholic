package com.yunhalee.walkerholic.follow;

import static com.yunhalee.walkerholic.user.UserAcceptanceTest.check_user_created;
import static com.yunhalee.walkerholic.user.UserAcceptanceTest.create_user_request;

import com.yunhalee.walkerholic.AcceptanceTest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

public class FollowAcceptanceTest extends AcceptanceTest {

    private Integer followerId;
    private String token;
    private Integer followingId;

    void follow_set_up() {
        // when
        ExtractableResponse<Response> followerCreateResponse = create_user_request(TEST_IMAGE_URL);
        // then
        check_user_created(followerCreateResponse);
        followerId = followerCreateResponse.body().jsonPath().getInt("user.id");
        token = followerCreateResponse.body().jsonPath().getString("token");

        // when
        ExtractableResponse<Response> followingCreateResponse = create_user_request("test2@example.com", TEST_IMAGE_URL);
        // then
        check_user_created(followingCreateResponse);
        followingId = followingCreateResponse.body().jsonPath().getInt("user.id");
    }

    @Test
    void manage_follow() {
        // given
        follow_set_up();
        // when
        ExtractableResponse<Response> createResponse = create_user_activity_request();
        // then
        check_user_activity_created(createResponse);

        // when
        ExtractableResponse<Response> findFollowersResponse = find_followers_request();
        // then
        check_followers_found(findFollowersResponse);

        // when
        ExtractableResponse<Response> findFollowingsResponse = find_followings_request();
        // then
        check_followings_found(findFollowingsResponse);

        // when
        ExtractableResponse<Response> deleteResponse = delete_follow_request(createResponse);
        // then
        check_follow_deleted(deleteResponse);
    }

    public ExtractableResponse<Response> create_user_activity_request() {
        return RestAssured
            .given().log().all()
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/api/follows?fromId=" + followerId + "&toId=" + followingId)
            .then().log().all()
            .extract();
    }

    public static void check_user_activity_created(ExtractableResponse<Response> response) {
        check_ok_response(response);
    }

    private ExtractableResponse<Response> find_followers_request() {
        return find_request("/users/" + followingId + "/followers", token);
    }

    private void check_followers_found(ExtractableResponse<Response> response) {
        check_ok_response(response);
    }

    private ExtractableResponse<Response> find_followings_request() {
        return find_request("/users/" + followerId + "/followings", token);
    }

    private void check_followings_found(ExtractableResponse<Response> response) {
        check_ok_response(response);
    }

    private ExtractableResponse<Response> delete_follow_request(ExtractableResponse<Response> response) {
        Integer id = response.body().jsonPath().getInt("id");
        return delete_request("/follows/" + id, token);
    }

    private void check_follow_deleted(ExtractableResponse<Response> response) {
        check_delete_response(response);
    }


}
