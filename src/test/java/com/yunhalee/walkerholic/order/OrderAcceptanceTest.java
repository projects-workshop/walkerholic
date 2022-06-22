package com.yunhalee.walkerholic.order;

import static com.yunhalee.walkerholic.cart.CartAcceptanceTest.check_cart_created;
import static com.yunhalee.walkerholic.cart.CartAcceptanceTest.check_cart_found;
import static com.yunhalee.walkerholic.cart.CartAcceptanceTest.create_cart_request;
import static com.yunhalee.walkerholic.cart.CartAcceptanceTest.find_cart_request;
import static com.yunhalee.walkerholic.cartItem.CartItemAcceptanceTest.check_cart_item_created;
import static com.yunhalee.walkerholic.cartItem.CartItemAcceptanceTest.create_cart_item_request;
import static com.yunhalee.walkerholic.product.ProductAcceptanceTest.check_product_created;
import static com.yunhalee.walkerholic.product.ProductAcceptanceTest.create_product_request;
import static com.yunhalee.walkerholic.user.UserAcceptanceTest.check_user_created;
import static com.yunhalee.walkerholic.user.UserAcceptanceTest.create_user_request;
import static org.assertj.core.api.Assertions.assertThat;

import com.yunhalee.walkerholic.AcceptanceTest;
import com.yunhalee.walkerholic.order.dto.AddressResponse;
import com.yunhalee.walkerholic.order.dto.OrderRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.io.File;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class OrderAcceptanceTest extends AcceptanceTest {

    private static final Integer PAGE = 1;
    private static final Float SHIPPING = 2.0f;
    private static final String PAYMENT_METHOD = "paypal";
    private static final String TRANSACTION_ID = "10102938";
    private static final AddressResponse ADDRESS = new AddressResponse("testAddress", "testCountry",
        "testCity", "testZipcode", "test Address 01");

    private final File productRequestFile = new File(
        getClass().getClassLoader().getResource("productRequest.txt").getPath());
    private Integer userId;
    private String token;
    private Integer cartId;

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
        Integer productId = productCreateResponse.body().jsonPath().getInt("id");

        // when
        ExtractableResponse<Response> createResponse = create_cart_item_request(productId, cartId, token);
        // then
        check_cart_item_created(createResponse);
    }

    @Test
    void manage_order_deliver() {
        // given
        user_cart_set_up();
        // when
        ExtractableResponse<Response> createResponse = create_order_request(userId, token);
        // then
        check_order_created(createResponse);

        // when
        ExtractableResponse<Response> findResponse = find_order_request(createResponse);
        // then
        check_order_found(findResponse);

        // when
        ExtractableResponse<Response> findOrdersResponse = find_orders_request();
        // then
        check_orders_found(findOrdersResponse);

        // when
        ExtractableResponse<Response> findOrdersBySellerResponse = find_orders_by_seller_request(createResponse);
        // then
        check_orders_found_by_seller(findOrdersBySellerResponse);

        // when
        ExtractableResponse<Response> findOrdersByUserResponse = find_orders_by_user_request();
        // then
        check_orders_found_by_user(findOrdersByUserResponse);

        // when
        ExtractableResponse<Response> deliverResponse = deliver_order_request(createResponse);
        // then
        check_order_delivered(deliverResponse);
    }

    @Test
    void manage_order_cancel() {
        // given
        user_cart_set_up();
        // when
        ExtractableResponse<Response> createResponse = create_order_request(userId, token);
        // then
        check_order_created(createResponse);

        // when
        ExtractableResponse<Response> cancelResponse = cancel_order_request(createResponse);
        // then
        check_order_canceled(cancelResponse);
    }


    @Test
    void cancel_already_delivered_order_is_invalid() {
        // given
        user_cart_set_up();
        // when
        ExtractableResponse<Response> createResponse = create_order_request(userId, token);
        // then
        check_order_created(createResponse);

        // when
        ExtractableResponse<Response> deliverResponse = deliver_order_request(createResponse);
        // then
        check_order_delivered(deliverResponse);

        // when
        ExtractableResponse<Response> cancelResponse = cancel_order_request(createResponse);
        // then
        check_order_not_canceled(cancelResponse);
    }

    public static ExtractableResponse<Response> create_order_request(Integer userId, String token) {
        OrderRequest request = new OrderRequest(userId, SHIPPING, PAYMENT_METHOD, TRANSACTION_ID, ADDRESS);
        return create_request(request, "/orders", token);
    }

    public static void check_order_created(ExtractableResponse<Response> response) {
        check_ok_response(response);
    }

    private ExtractableResponse<Response> find_order_request(ExtractableResponse<Response> response) {
        Integer id = response.body().jsonPath().getInt("id");
        return find_request("/orders/" + id, token);
    }

    private void check_order_found(ExtractableResponse<Response> response) {
        check_ok_response(response);
    }

    private ExtractableResponse<Response> find_orders_request() {
        return find_request("/orders?page=" + PAGE, token);
    }

    private void check_orders_found(ExtractableResponse<Response> response) {
        check_ok_response(response);
    }

    private ExtractableResponse<Response> find_orders_by_seller_request(ExtractableResponse<Response> response) {
        Integer id = response.body().jsonPath().getInt("id");
        return find_request("/orders?page=" + PAGE + "&sellerId=" + userId, token);
    }

    private void check_orders_found_by_seller(ExtractableResponse<Response> response) {
        check_ok_response(response);
    }

    private ExtractableResponse<Response> find_orders_by_user_request() {
        return find_request("/orders?page=" + PAGE + "&userId=" + userId, token);
    }

    private void check_orders_found_by_user(ExtractableResponse<Response> response) {
        check_ok_response(response);
    }


    private ExtractableResponse<Response> deliver_order_request(ExtractableResponse<Response> response) {
        Integer id = response.body().jsonPath().getInt("id");
        return update_Request("/orders/" + id + "/delivery", token);
    }

    private void check_order_delivered(ExtractableResponse<Response> updateResponse) {
        check_ok_response(updateResponse);
    }

    private ExtractableResponse<Response> cancel_order_request(ExtractableResponse<Response> response) {
        Integer id = response.body().jsonPath().getInt("id");
        return update_Request("/orders/" + id + "/cancellation", token);
    }

    private void check_order_canceled(ExtractableResponse<Response> response) {
        check_ok_response(response);
    }

    private void check_order_not_canceled(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

}
