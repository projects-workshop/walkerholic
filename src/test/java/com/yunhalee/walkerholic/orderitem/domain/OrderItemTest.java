package com.yunhalee.walkerholic.orderitem.domain;

import com.yunhalee.walkerholic.order.domain.OrderTest;
import com.yunhalee.walkerholic.product.domain.Category;
import com.yunhalee.walkerholic.product.domain.Product;
import com.yunhalee.walkerholic.product.domain.ProductTest;
import com.yunhalee.walkerholic.productImage.domain.ProductImageTest;
import com.yunhalee.walkerholic.user.domain.UserTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class OrderItemTest {

    public static final OrderItem FIRST_ORDER_ITEM = new OrderItem(
        1,
        20,
        ProductTest.SECOND_PRODUCT,
        OrderTest.ORDER);

    public static final OrderItem SECOND_ORDER_ITEM = new OrderItem(
        2,
        3,
        ProductTest.THIRD_PRODUCT,
        OrderTest.ORDER);

    private OrderItem orderItem;
    private Product product;

    void setUp() {
        product = new Product(1,
            "firstProduct",
            "testDescription",
            "testBrand",
            Category.TUMBLER,
            30,
            2.0f,
            UserTest.SELLER,
            ProductImageTest.PRODUCT_IMAGE
        );
        orderItem = OrderItem.of(20, product, OrderTest.ORDER);
    }

    @Test
    public void create_order_item() {
        // given
        // when
        setUp();

        // then
        assertThat(product.getStock()).isEqualTo(10);
    }

    @Test
    public void cancel_order_item() {
        // given
        setUp();

        // when
        orderItem.cancel();

        // then
        assertThat(product.getStock()).isEqualTo(30);
    }

}
