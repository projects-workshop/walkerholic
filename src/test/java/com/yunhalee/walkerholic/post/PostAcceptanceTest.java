package com.yunhalee.walkerholic.post;

import static com.yunhalee.walkerholic.user.UserAcceptanceTest.check_user_created;
import static com.yunhalee.walkerholic.user.UserAcceptanceTest.create_user_request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yunhalee.walkerholic.AcceptanceTest;
import com.yunhalee.walkerholic.post.dto.PostRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.io.File;
import org.junit.jupiter.api.Test;

public class PostAcceptanceTest extends AcceptanceTest {

    private static final Integer PAGE = 1;
    private static final String SORT = "createdAt";
    private Integer userId;
    private String token;

    void user_set_up() {
        // when
        ExtractableResponse<Response> userCreateResponse = create_user_request(TEST_IMAGE_URL);
        // then
        check_user_created(userCreateResponse);
        userId = userCreateResponse.body().jsonPath().getInt("user.id");
        token = userCreateResponse.body().jsonPath().getString("token");
    }


    @Test
    void manage_post() {
        // given
        user_set_up();
        // when
        ExtractableResponse<Response> createResponse = create_post_request(imageFile, postRequest(userId), token);
        // then
        check_post_created(createResponse);

        // when
        ExtractableResponse<Response> findResponse = find_post_request(createResponse);
        // then
        check_post_found(findResponse);

        // when
        ExtractableResponse<Response> findPostsByUserResponse = find_posts_by_user_request();
        // then
        check_posts_found_by_user(findPostsByUserResponse);

        // when
        ExtractableResponse<Response> findPostsByRandomResponse = find_posts_by_random_request();
        // then
        check_posts_found_by_random(findPostsByRandomResponse);

        // when
        ExtractableResponse<Response> findFollowingPostsResponse = find_following_posts_request();
        // then
        check_following_posts_found(findFollowingPostsResponse);

        // when
        ExtractableResponse<Response> findHomePostsResponse = find_home_posts_request();
        // then
        check_home_posts_found(findHomePostsResponse);

        // when
        ExtractableResponse<Response> findPostsByKeywordResponse = find_posts_by_keyword();
        // then
        check_posts_found_by_keyword(findPostsByKeywordResponse);

        // when
        ExtractableResponse<Response> updateResponse = update_post_request(createResponse);
        // then
        check_post_updated(updateResponse);

        // when
        ExtractableResponse<Response> deleteResponse = delete_post_request(createResponse);
        // then
        check_post_deleted(deleteResponse);


    }

    public static ExtractableResponse<Response> create_post_request(File imageFile, String request, String token) {
        return RestAssured
            .given().log().all()
            .header("Authorization", "Bearer " + token)
            .contentType("multipart/form-data")
            .multiPart("multipartFile", imageFile, "image/jpeg")
            .multiPart("postRequest", request, "application/json")
            .when().post("/api/posts")
            .then().log().all()
            .extract();
    }


    public static void check_post_created(ExtractableResponse<Response> response) {
        check_create_response(response);
    }

    private ExtractableResponse<Response> find_post_request(ExtractableResponse<Response> response) {
        Integer id = response.body().jsonPath().getInt("id");
        return find_request("/posts/" + id, token);
    }

    private void check_post_found(ExtractableResponse<Response> response) {
        check_ok_response(response);
    }

    private ExtractableResponse<Response> find_posts_by_user_request() {
        return find_request("/users/" + userId + "/posts/", token);
    }

    private void check_posts_found_by_user(ExtractableResponse<Response> response) {
        check_ok_response(response);
    }

    private ExtractableResponse<Response> find_posts_by_random_request() {
        return find_request("/users/" + userId + "/posts/discover?page=" + PAGE, token);
    }

    private void check_posts_found_by_random(ExtractableResponse<Response> response) {
        check_ok_response(response);
    }

    private ExtractableResponse<Response> find_following_posts_request() {
        return find_request("/users/" + userId + "/posts/follow?page=" + PAGE, token);
    }

    private void check_following_posts_found(ExtractableResponse<Response> response) {
        check_ok_response(response);
    }

    private ExtractableResponse<Response> find_home_posts_request() {
        return find_request("/users/" + userId + "/posts?page=" + PAGE + "sort=" + SORT);
    }

    private void check_home_posts_found(ExtractableResponse<Response> response) {
        check_ok_response(response);
    }

    private ExtractableResponse<Response> find_posts_by_keyword() {
        String keyword = "t";
        return find_request(
            "/users/" + userId + "/posts?page=" + PAGE + "sort=" + SORT + "&keyword=" + keyword,
            token);
    }

    private void check_posts_found_by_keyword(ExtractableResponse<Response> response) {
        check_ok_response(response);
    }

    private ExtractableResponse<Response> update_post_request(ExtractableResponse<Response> response) {
        Integer id = response.body().jsonPath().getInt("id");
        PostRequest request = new PostRequest("updatePost", "testUpdatePost", userId);
        return update_Request(request, "/posts/" + id, token);
    }

    private void check_post_updated(ExtractableResponse<Response> updateResponse) {
        check_ok_response(updateResponse);
    }


    private ExtractableResponse<Response> delete_post_request(ExtractableResponse<Response> response) {
        Integer id = response.body().jsonPath().getInt("id");
        return delete_request("/posts/" + id, token);
    }

    private void check_post_deleted(ExtractableResponse<Response> response) {
        check_delete_response(response);
    }

}
