package com.yunhalee.walkerholic.cartItem;

import static com.yunhalee.walkerholic.cart.CartAcceptanceTest.check_cart_created;
import static com.yunhalee.walkerholic.cart.CartAcceptanceTest.check_cart_found;
import static com.yunhalee.walkerholic.cart.CartAcceptanceTest.create_cart_request;
import static com.yunhalee.walkerholic.cart.CartAcceptanceTest.find_cart_request;
import static com.yunhalee.walkerholic.product.ProductAcceptanceTest.check_product_created;
import static com.yunhalee.walkerholic.product.ProductAcceptanceTest.create_product_request;
import static com.yunhalee.walkerholic.user.UserAcceptanceTest.check_user_created;
import static com.yunhalee.walkerholic.user.UserAcceptanceTest.create_user_request;

import com.yunhalee.walkerholic.AcceptanceTest;
import com.yunhalee.walkerholic.cartItem.dto.CartItemRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.io.File;
import org.junit.jupiter.api.Test;

public class CartItemAcceptanceTest extends AcceptanceTest {

    private static final Integer QTY = 10;
    private final File productRequestFile = new File(getClass().getClassLoader().getResource("productRequest.txt").getPath());
    private Integer userId;
    private String token;
    private Integer cartId;
    private Integer productId;


    void user_cart_set_up() {
        // when
        ExtractableResponse<Response> userCreateResponse = create_user_request(TEST_IMAGE_URL);
        // then
        check_user_created(userCreateResponse);
        userId = userCreateResponse.body().jsonPath().getInt("user.id");
        token = userCreateResponse.body().jsonPath().getString("token");

        // when
        ExtractableResponse<Response> cartCreateResponse = create_cart_request(userId, token);
        // then
        check_cart_created(cartCreateResponse);
        // when
        ExtractableResponse<Response> findCartResponse = find_cart_request(userId, token);
        // then
        check_cart_found(findCartResponse);
        cartId = findCartResponse.body().jsonPath().getInt("id");

        // when
        ExtractableResponse<Response> productCreateResponse = create_product_request(imageFile, productRequestFile, token);
        // then
        check_product_created(productCreateResponse);
        productId = productCreateResponse.body().jsonPath().getInt("id");
    }

    @Test
    void manage_cart_item() {
        // given
        user_cart_set_up();
        // when
        ExtractableResponse<Response> createResponse = create_cart_item_request(productId, cartId, token);
        // then
        check_cart_item_created(createResponse);

        // when
        ExtractableResponse<Response> updateResponse = update_cart_item_request(createResponse);
        // then
        check_cart_item_updated(updateResponse);

        // when
        ExtractableResponse<Response> deleteResponse = delete_cart_item_request(createResponse);
        // then
        check_cart_item_deleted(deleteResponse);
    }


    public static ExtractableResponse<Response> create_cart_item_request(Integer productId, Integer cartId, String token) {
        CartItemRequest request = new CartItemRequest(QTY, productId, cartId);
        return create_request(request, "/cart-items", token);
    }

    public static void check_cart_item_created(ExtractableResponse<Response> response) {
        check_ok_response(response);
    }

    private ExtractableResponse<Response> update_cart_item_request(ExtractableResponse<Response> response) {
        Integer id = response.body().jsonPath().getInt("id");
        return update_Request("/cart-items/" + id + "?qty=" + 8, token);
    }

    private void check_cart_item_updated(ExtractableResponse<Response> updateResponse) {
        check_ok_response(updateResponse);
    }


    private ExtractableResponse<Response> delete_cart_item_request(ExtractableResponse<Response> response) {
        Integer id = response.body().jsonPath().getInt("id");
        return delete_request("/cart-items/" + id, token);
    }

    private void check_cart_item_deleted(ExtractableResponse<Response> response) {
        check_delete_response(response);
    }
}
