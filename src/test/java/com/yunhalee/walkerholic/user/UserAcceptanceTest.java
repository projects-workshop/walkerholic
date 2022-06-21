package com.yunhalee.walkerholic.user;

import static org.assertj.core.api.Assertions.assertThat;

import com.yunhalee.walkerholic.AcceptanceTest;
import com.yunhalee.walkerholic.user.dto.UserRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.io.File;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class UserAcceptanceTest extends AcceptanceTest {

    private static final String FIRST_NAME = "firstname";
    private static final String LAST_NAME = "lastname";
    private static final String EMAIL = "test@example.com";
    private static final String PASSWORD = "12345678";
    private static final String PHONE_NUMBER = "01000000000";
    private static final String DESCRIPTION = "This is Test user";
    private static final boolean IS_SELLER = true;

    private String token;

    @Test
    void manage_user() {
        // when
        ExtractableResponse<Response> createImageResponse = create_user_image_request(imageFile);
        // then
        check_activity_image_created(createImageResponse);

        // given
        String imageUrl = createImageResponse.body().asString();
        // when
        ExtractableResponse<Response> createResponse = create_user_request(imageUrl);
        // then
        check_user_created(createResponse);
        token = createResponse.body().jsonPath().getString("token");

        // when
        ExtractableResponse<Response> findResponse = find_user_request(createResponse);
        // then
        check_user_found(findResponse);

        // when
        ExtractableResponse<Response> findUsersResponse = find_users_request();
        // then
        check_users_found(findUsersResponse);

        // when
        ExtractableResponse<Response> findUsersByKeywordResponse = find_users_by_keyword_request();
        // then
        check_users_found_by_keyword(findUsersByKeywordResponse);

        // when
        ExtractableResponse<Response> updateResponse = update_user_request(createResponse);
        // then
        check_user_updated(updateResponse);

        // when
        ExtractableResponse<Response> forgotPasswordResponse = send_new_password_request(createResponse);
        // then
        check_new_password_sent(forgotPasswordResponse);

        // when
        ExtractableResponse<Response> deleteResponse = delete_user_request(createResponse);
        // then
        check_user_deleted(deleteResponse);
    }


    public static ExtractableResponse<Response> create_user_image_request(File imageFile) {
        return RestAssured
            .given().log().all()
            .contentType("multipart/form-data")
            .multiPart("multipartFile", imageFile, "image/jpeg")
            .when().post("/api/users/images")
            .then().log().all()
            .extract();
    }

    private void check_activity_image_created(ExtractableResponse<Response> response) {
        check_ok_response(response);
    }


    public static ExtractableResponse<Response> create_user_request(String imageUrl) {
        UserRequest request = new UserRequest(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, imageUrl, PHONE_NUMBER, DESCRIPTION, IS_SELLER);
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().post("/api/users")
            .then().log().all()
            .extract();
    }

    public static void check_user_created(ExtractableResponse<Response> response) {
        check_ok_response(response);
    }

    private ExtractableResponse<Response> find_user_request(ExtractableResponse<Response> response) {
        Integer id = response.body().jsonPath().getInt("user.id");
        return find_request("/users/" + id, token);
    }

    private void check_user_found(ExtractableResponse<Response> response) {
        check_ok_response(response);
    }

    private ExtractableResponse<Response> find_users_request() {
        Integer page = 1;
        String sort = "id";
        return find_request("/users?page=" + page + "&sort=" + sort, token);
    }

    private void check_users_found(ExtractableResponse<Response> response) {
        check_ok_response(response);
    }

    private ExtractableResponse<Response> find_users_by_keyword_request() {
        String keyword = "f";
        return find_request("/users?keyword=" + keyword, token);
    }

    private void check_users_found_by_keyword(ExtractableResponse<Response> response) {
        check_ok_response(response);
    }


    private ExtractableResponse<Response> update_user_request(ExtractableResponse<Response> response) {
        Integer id = response.body().jsonPath().getInt("user.id");
        UserRequest request = new UserRequest(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD,response.body().jsonPath().getString("user.imageUrl"), PHONE_NUMBER, DESCRIPTION, IS_SELLER);
        return update_Request(request, "/users/" + id, token);
    }

    private void check_user_updated(ExtractableResponse<Response> updateResponse) {
        check_ok_response(updateResponse);
    }

    private ExtractableResponse<Response> send_new_password_request(ExtractableResponse<Response> response) {
        String email = response.body().jsonPath().getString("user.email");
        return RestAssured
            .given().log().all()
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/api/users?email=" + email)
            .then().log().all()
            .extract();

    }

    private void check_new_password_sent(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> delete_user_request(ExtractableResponse<Response> response) {
        Integer id = response.body().jsonPath().getInt("user.id");
        return delete_request("/users/" + id, token);
    }

    private void check_user_deleted(ExtractableResponse<Response> response) {
        check_delete_response(response);
    }

    public static String user_token() {
        // when
        ExtractableResponse<Response> createResponse = create_user_request(USER_IMAGE_URL);
        // then
        check_user_created(createResponse);
        return createResponse.body().jsonPath().getString("token");
    }





}
