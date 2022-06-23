package com.yunhalee.walkerholic.orderitem.api;

import static com.yunhalee.walkerholic.order.api.OrderApiTest.FIRST_ORDER;
import static com.yunhalee.walkerholic.product.api.ProductApiTest.FIRST_PRODUCT;
import static com.yunhalee.walkerholic.product.api.ProductApiTest.SECOND_PRODUCT;
import static com.yunhalee.walkerholic.productImage.api.ProductImageApiTest.FIRST_PRODUCT_IMAGE;
import static com.yunhalee.walkerholic.productImage.api.ProductImageApiTest.SECOND_PRODUCT_IMAGE;
import static com.yunhalee.walkerholic.productImage.api.ProductImageApiTest.THIRD_PRODUCT_IMAGE;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yunhalee.walkerholic.ApiTest;
import com.yunhalee.walkerholic.orderitem.domain.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class OrderItemApiTest extends ApiTest {

    public static final OrderItem FIRST_ORDER_ITEM = new OrderItem(1, 3, FIRST_PRODUCT, FIRST_ORDER);
    public static final OrderItem SECOND_ORDER_ITEM = new OrderItem(2, 3, SECOND_PRODUCT, FIRST_ORDER);

    @BeforeEach
    void setUp() {
        FIRST_PRODUCT.addProductImage(FIRST_PRODUCT_IMAGE);
        FIRST_PRODUCT.addProductImage(SECOND_PRODUCT_IMAGE);
        SECOND_PRODUCT.addProductImage(THIRD_PRODUCT_IMAGE);
        FIRST_ORDER.addOrderItem(FIRST_ORDER_ITEM);
        FIRST_ORDER.addOrderItem(SECOND_ORDER_ITEM);
    }

    @Test
    void delete_user_activity() throws Exception {
        this.mockMvc.perform(delete("/api/order-items/1")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(document("order-item-delete"));
    }


}
