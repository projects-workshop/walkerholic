package com.yunhalee.walkerholic.orderitem;

import static com.yunhalee.walkerholic.cart.CartAcceptanceTest.check_cart_created;
import static com.yunhalee.walkerholic.cart.CartAcceptanceTest.check_cart_found;
import static com.yunhalee.walkerholic.cart.CartAcceptanceTest.create_cart_request;
import static com.yunhalee.walkerholic.cart.CartAcceptanceTest.find_cart_request;
import static com.yunhalee.walkerholic.cartItem.CartItemAcceptanceTest.check_cart_item_created;
import static com.yunhalee.walkerholic.cartItem.CartItemAcceptanceTest.create_cart_item_request;
import static com.yunhalee.walkerholic.order.OrderAcceptanceTest.check_order_created;
import static com.yunhalee.walkerholic.order.OrderAcceptanceTest.create_order_request;
import static com.yunhalee.walkerholic.product.ProductAcceptanceTest.check_product_created;
import static com.yunhalee.walkerholic.product.ProductAcceptanceTest.create_product_request;
import static com.yunhalee.walkerholic.user.UserAcceptanceTest.check_user_created;
import static com.yunhalee.walkerholic.user.UserAcceptanceTest.create_user_request;

import com.yunhalee.walkerholic.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.io.File;
import org.junit.jupiter.api.Test;

public class OrderItemAcceptanceTest extends AcceptanceTest {

    private final File productRequestFile = new File(getClass().getClassLoader().getResource("productRequest.txt").getPath());
    private Integer userId;
    private String token;
    private Integer cartId;
    private Integer orderItemId;

    void user_order_set_up() {
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
        Integer productId = productCreateResponse.body().jsonPath().getInt("id");

        // when
        ExtractableResponse<Response> createResponse = create_cart_item_request(productId, cartId, token);
        // then
        check_cart_item_created(createResponse);

        // when
        ExtractableResponse<Response> orderCreateResponse = create_order_request(userId, token);
        // then
        check_order_created(orderCreateResponse);
        orderItemId = orderCreateResponse.body().jsonPath().getInt("items[0].id");
    }

    @Test
    void manage_order_item() {
        // given
        user_order_set_up();
        // when
        ExtractableResponse<Response> deleteResponse = delete_order_item_request();
        // then
        check_order_item_deleted(deleteResponse);
    }

    private ExtractableResponse<Response> delete_order_item_request() {
        return delete_request("/order-items/" + orderItemId, token);
    }

    private void check_order_item_deleted(ExtractableResponse<Response> response) {
        check_delete_response(response);
    }


}
