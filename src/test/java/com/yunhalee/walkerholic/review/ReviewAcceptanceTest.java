package com.yunhalee.walkerholic.review;

import static com.yunhalee.walkerholic.product.ProductAcceptanceTest.check_product_created;
import static com.yunhalee.walkerholic.product.ProductAcceptanceTest.create_product_request;
import static com.yunhalee.walkerholic.user.UserAcceptanceTest.check_user_created;
import static com.yunhalee.walkerholic.user.UserAcceptanceTest.create_user_request;

import com.yunhalee.walkerholic.AcceptanceTest;
import com.yunhalee.walkerholic.review.dto.ReviewRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.io.File;
import org.junit.jupiter.api.Test;

public class ReviewAcceptanceTest extends AcceptanceTest {

    private static final Integer RATING = 5;
    private static final String COMMENT = "This is test review.";
    private final File productRequestFile = new File(getClass().getClassLoader().getResource("productRequest.txt").getPath());
    private Integer userId;
    private String token;
    private Integer productId;

    void user_product_set_up() {
        // when
        ExtractableResponse<Response> userCreateResponse = create_user_request(TEST_IMAGE_URL);
        // then
        check_user_created(userCreateResponse);
        userId = userCreateResponse.body().jsonPath().getInt("user.id");
        token = userCreateResponse.body().jsonPath().getString("token");

        // when
        ExtractableResponse<Response> productCreateResponse = create_product_request(imageFile, productRequestFile, token);
        // then
        check_product_created(productCreateResponse);
        productId = productCreateResponse.body().jsonPath().getInt("id");
    }

    @Test
    void manage_review() {
        // given
        user_product_set_up();
        // when
        ExtractableResponse<Response> createResponse = create_review_request();
        // then
        check_review_created(createResponse);

        // when
        ExtractableResponse<Response> updateResponse = update_review_request(createResponse);
        // then
        check_review_updated(updateResponse);

        // when
        ExtractableResponse<Response> deleteResponse = delete_review_request(createResponse);
        // then
        check_review_deleted(deleteResponse);
    }

    private ExtractableResponse<Response> create_review_request() {
        ReviewRequest request = new ReviewRequest(RATING, COMMENT, productId, userId);
        return create_request(request, "/reviews", token);
    }

    private void check_review_created(ExtractableResponse<Response> response) {
        check_ok_response(response);
    }

    private ExtractableResponse<Response> update_review_request(ExtractableResponse<Response> response) {
        Integer id = response.body().jsonPath().getInt("id");
        ReviewRequest request = new ReviewRequest(2, "updateReview", productId, userId);
        return update_Request(request, "/reviews/" + id, token);
    }

    private void check_review_updated(ExtractableResponse<Response> updateResponse) {
        check_ok_response(updateResponse);
    }

    private ExtractableResponse<Response> delete_review_request(ExtractableResponse<Response> response) {
        Integer id = response.body().jsonPath().getInt("id");
        return delete_request("/reviews/" + id, token);
    }

    private void check_review_deleted(ExtractableResponse<Response> response) {
        check_delete_response(response);
    }


}
