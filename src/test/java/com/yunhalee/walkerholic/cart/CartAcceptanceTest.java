package com.yunhalee.walkerholic.cart;

import static com.yunhalee.walkerholic.user.UserAcceptanceTest.check_user_created;
import static com.yunhalee.walkerholic.user.UserAcceptanceTest.create_user_request;

import com.yunhalee.walkerholic.AcceptanceTest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

public class CartAcceptanceTest extends AcceptanceTest {

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
    void manage_cart() {
        // given
        user_set_up();
        // when
        ExtractableResponse<Response> createResponse = create_cart_request();
        // then
        check_cart_created(createResponse);

        // when
        ExtractableResponse<Response> findResponse = find_cart_request();
        // then
        check_cart_found(findResponse);
    }


    private ExtractableResponse<Response> create_cart_request() {
        return RestAssured
            .given().log().all()
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/api/carts?userId=" + userId)
            .then().log().all()
            .extract();
    }

    private void check_cart_created(ExtractableResponse<Response> response) {
        check_ok_response(response);
    }


    private ExtractableResponse<Response> find_cart_request() {
        return find_request("/carts?userId=" + userId, token);
    }

    private void check_cart_found(ExtractableResponse<Response> response) {
        check_ok_response(response);
    }


}
