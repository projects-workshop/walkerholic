package com.yunhalee.walkerholic.postImage;

import static com.yunhalee.walkerholic.post.PostAcceptanceTest.check_post_created;
import static com.yunhalee.walkerholic.post.PostAcceptanceTest.create_post_request;
import static com.yunhalee.walkerholic.user.UserAcceptanceTest.check_user_created;
import static com.yunhalee.walkerholic.user.UserAcceptanceTest.create_user_request;

import com.yunhalee.walkerholic.AcceptanceTest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.io.File;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

public class PostImageAcceptanceTest extends AcceptanceTest {

    private final File postRequestFile = new File(getClass().getClassLoader().getResource("postRequest").getPath());
    private String token;
    private Integer postId;

    void user_post_set_up() {
        // when
        ExtractableResponse<Response> userCreateResponse = create_user_request(TEST_IMAGE_URL);
        // then
        check_user_created(userCreateResponse);
        token = userCreateResponse.body().jsonPath().getString("token");

        // when
        ExtractableResponse<Response> postCreateResponse = create_post_request(imageFile, postRequestFile, token);
        // then
        check_post_created(postCreateResponse);
        postId = postCreateResponse.body().jsonPath().getInt("id");
    }

    @Test
    void manage_post_image() {
        // given
        user_post_set_up();
        // when
        ExtractableResponse<Response> createResponse = create_post_image_request();
        // then
        check_post_image_created(createResponse);

        // when
        ExtractableResponse<Response> deleteResponse = delete_post_images_request(createResponse);
        // then
        check_post_images_deleted(deleteResponse);
    }

    public ExtractableResponse<Response> create_post_image_request() {
        return RestAssured
            .given().log().all()
            .header("Authorization", "Bearer " + token)
            .contentType("multipart/form-data")
            .multiPart("multipartFile", imageFile, "image/jpeg")
            .when().post("/api/posts/" + postId + "/post-images")
            .then().log().all()
            .extract();
    }

    private void check_post_image_created(ExtractableResponse<Response> response) {
        check_ok_response(response);
    }

    private ExtractableResponse<Response> delete_post_images_request(ExtractableResponse<Response> response) {
        String imageUrl = response.body().jsonPath().getString("postImages[0].imageUrl");
        return RestAssured
            .given().log().all()
            .header("Authorization", "Bearer " + token)
            .param("deletedImages", imageUrl)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().delete("/api/posts/" + postId + "/post-images")
            .then().log().all()
            .extract();
    }

    private void check_post_images_deleted(ExtractableResponse<Response> response) {
        check_delete_response(response);
    }


}
