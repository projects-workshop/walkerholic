package com.yunhalee.walkerholic.product;

import static com.yunhalee.walkerholic.user.UserAcceptanceTest.check_user_created;
import static com.yunhalee.walkerholic.user.UserAcceptanceTest.create_user_request;

import com.yunhalee.walkerholic.AcceptanceTest;
import com.yunhalee.walkerholic.product.dto.ProductRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.io.File;
import org.junit.jupiter.api.Test;

public class ProductAcceptanceTest extends AcceptanceTest {

    private static final String NAME = "updateTest";
    private static final String DESCRIPTION = "This is test update product.";
    private static final String BRAND = "testBrand";
    private static final String CATEGORY = "CLOTHES";
    private static final Integer STOCK = 280;
    private static final Float PRICE = 12.0f;
    private static final Integer PAGE = 1;
    private static final String SORT = "createdAt";
    private static final String KEYWORD = "t";

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
    void manage_product() {
        // given
        user_set_up();
        // when
        ExtractableResponse<Response> createResponse = create_product_request(imageFile, productRequest(userId), token);
        // then
        check_product_created(createResponse);

        // when
        ExtractableResponse<Response> findResponse = find_product_request(createResponse);
        // then
        check_product_found(findResponse);

        // when
        ExtractableResponse<Response> findPostsByUserResponse = find_products_by_page_and_sort_and_category_and_keyword_request();
        // then
        check_products_found_by_page_and_sort_and_category_and_keyword(findPostsByUserResponse);

        // when
        ExtractableResponse<Response> findPostsByRandomResponse = find_products_by_seller_request();
        // then
        check_products_found_by_seller(findPostsByRandomResponse);

        // when
        ExtractableResponse<Response> updateResponse = update_product_request(createResponse);
        // then
        check_product_updated(updateResponse);

        // when
        ExtractableResponse<Response> deleteResponse = delete_product_request(createResponse);
        // then
        check_product_deleted(deleteResponse);

    }

    public static ExtractableResponse<Response> create_product_request(File imageFile, String request, String token) {
        return RestAssured
            .given().log().all()
            .header("Authorization", "Bearer " + token)
            .contentType("multipart/form-data")
            .multiPart("multipartFile", imageFile, "image/jpeg")
            .multiPart("productRequest", request, "application/json")
            .when().post("/api/products")
            .then().log().all()
            .extract();
    }

    public static void check_product_created(ExtractableResponse<Response> response) {
        check_create_response(response);
    }

    private ExtractableResponse<Response> find_product_request(ExtractableResponse<Response> response) {
        Integer id = response.body().jsonPath().getInt("id");
        return find_request("/products/" + id, token);
    }

    private void check_product_found(ExtractableResponse<Response> response) {
        check_ok_response(response);
    }

    private ExtractableResponse<Response> find_products_by_page_and_sort_and_category_and_keyword_request() {
        return find_request("/products?page=" + PAGE + "&sort=" + SORT + "&category=" + CATEGORY + "&keyword=" + KEYWORD, token);
    }

    private void check_products_found_by_page_and_sort_and_category_and_keyword(ExtractableResponse<Response> response) {
        check_ok_response(response);
    }

    private ExtractableResponse<Response> find_products_by_seller_request() {
        return find_request("/users/" + userId + "/products?page=" + PAGE + "&sort=" + SORT + "&category=" + CATEGORY + "&keyword=" + KEYWORD, token);
    }

    private void check_products_found_by_seller(ExtractableResponse<Response> response) {
        check_ok_response(response);
    }

    private ExtractableResponse<Response> update_product_request(ExtractableResponse<Response> response) {
        Integer id = response.body().jsonPath().getInt("id");
        ProductRequest request = new ProductRequest(NAME, DESCRIPTION, BRAND, CATEGORY, STOCK, PRICE, userId);
        return update_Request(request, "/products/" + id, token);
    }

    private void check_product_updated(ExtractableResponse<Response> updateResponse) {
        check_ok_response(updateResponse);
    }

    private ExtractableResponse<Response> delete_product_request(ExtractableResponse<Response> response) {
        Integer id = response.body().jsonPath().getInt("id");
        return delete_request("/products/" + id, token);
    }

    private void check_product_deleted(ExtractableResponse<Response> response) {
        check_delete_response(response);
    }


}
