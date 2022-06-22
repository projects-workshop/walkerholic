package com.yunhalee.walkerholic.likepost;

import static com.yunhalee.walkerholic.post.PostAcceptanceTest.check_post_created;
import static com.yunhalee.walkerholic.post.PostAcceptanceTest.create_post_request;
import static com.yunhalee.walkerholic.user.UserAcceptanceTest.check_user_created;
import static com.yunhalee.walkerholic.user.UserAcceptanceTest.create_user_request;

import com.yunhalee.walkerholic.AcceptanceTest;
import com.yunhalee.walkerholic.likepost.dto.LikePostRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class LikePostAcceptanceTest extends AcceptanceTest {

    private Integer userId;
    private String token;
    private Integer postId;

    void user_post_set_up() {
        // when
        ExtractableResponse<Response> userCreateResponse = create_user_request(TEST_IMAGE_URL);
        // then
        check_user_created(userCreateResponse);
        userId = userCreateResponse.body().jsonPath().getInt("user.id");
        token = userCreateResponse.body().jsonPath().getString("token");

        // when
        ExtractableResponse<Response> postCreateResponse = create_post_request(imageFile, postRequest(userId), token);
        // then
        check_post_created(postCreateResponse);
        postId = postCreateResponse.body().jsonPath().getInt("id");
    }

    @Test
    void manage_like_post() {
        // given
        user_post_set_up();
        // when
        ExtractableResponse<Response> createResponse = create_like_post_request();
        // then
        check_like_post_created(createResponse);

        // when
        ExtractableResponse<Response> deleteResponse = delete_like_post_request(createResponse);
        // then
        check_like_post_deleted(deleteResponse);
    }

    public ExtractableResponse<Response> create_like_post_request() {
        LikePostRequest request = new LikePostRequest(postId, userId);
        return create_request(request, "/like-posts", token);
    }

    public static void check_like_post_created(ExtractableResponse<Response> response) {
        check_create_response(response);
    }

    private ExtractableResponse<Response> delete_like_post_request(ExtractableResponse<Response> response) {
        Integer id = response.body().jsonPath().getInt("id");
        return delete_request("/like-posts/" + id, token);
    }

    private void check_like_post_deleted(ExtractableResponse<Response> response) {
        check_delete_response(response);
    }

}