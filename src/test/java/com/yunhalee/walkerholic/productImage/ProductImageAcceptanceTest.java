package com.yunhalee.walkerholic.productImage;

import static com.yunhalee.walkerholic.product.ProductAcceptanceTest.check_product_created;
import static com.yunhalee.walkerholic.product.ProductAcceptanceTest.create_product_request;
import static com.yunhalee.walkerholic.user.UserAcceptanceTest.check_user_created;
import static com.yunhalee.walkerholic.user.UserAcceptanceTest.create_user_request;

import com.yunhalee.walkerholic.AcceptanceTest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.io.File;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

public class ProductImageAcceptanceTest extends AcceptanceTest {

    private final File productRequestFile = new File(getClass().getClassLoader().getResource("productRequest.txt").getPath());
    private Integer productId;
    private String token;

    void user_product_set_up() {
        // when
        ExtractableResponse<Response> userCreateResponse = create_user_request(TEST_IMAGE_URL);
        // then
        check_user_created(userCreateResponse);
        token = userCreateResponse.body().jsonPath().getString("token");

        // when
        ExtractableResponse<Response> productCreateResponse = create_product_request(imageFile, productRequestFile, token);
        // then
        check_product_created(productCreateResponse);
        productId = productCreateResponse.body().jsonPath().getInt("id");
    }

    @Test
    void manage_product_image() {
        // given
        user_product_set_up();
        // when
        ExtractableResponse<Response> createResponse = create_product_images_request();
        // then
        check_product_image_created(createResponse);

        // when
        ExtractableResponse<Response> deleteResponse = delete_product_images_request(
            createResponse);
        // then
        check_product_images_deleted(deleteResponse);
    }


    public ExtractableResponse<Response> create_product_images_request() {
        return RestAssured
            .given().log().all()
            .header("Authorization", "Bearer " + token)
            .contentType("multipart/form-data")
            .multiPart("multipartFile", imageFile, "image/jpeg")
            .when().post("/api/products/" + productId + "/product-images")
            .then().log().all()
            .extract();
    }

    private void check_product_image_created(ExtractableResponse<Response> response) {
        check_ok_response(response);
    }

    private ExtractableResponse<Response> delete_product_images_request(ExtractableResponse<Response> response) {
        String imageUrl = response.body().jsonPath().getString("imagesUrl[0]");
        return RestAssured
            .given().log().all()
            .header("Authorization", "Bearer " + token)
            .param("deletedImages", imageUrl)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().delete("/api/products/" + productId + "/product-images")
            .then().log().all()
            .extract();
    }

    private void check_product_images_deleted(ExtractableResponse<Response> response) {
        check_delete_response(response);
    }


}
