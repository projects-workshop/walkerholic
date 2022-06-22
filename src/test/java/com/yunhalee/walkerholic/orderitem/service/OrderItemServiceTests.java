package com.yunhalee.walkerholic.orderitem.service;

import com.yunhalee.walkerholic.ServiceTest;
import com.yunhalee.walkerholic.cartItem.domain.CartItem;
import com.yunhalee.walkerholic.cartItem.domain.CartItemTest;
import com.yunhalee.walkerholic.order.domain.DeliveryInfoTest;
import com.yunhalee.walkerholic.order.domain.Order;
import com.yunhalee.walkerholic.order.domain.OrderItems;
import com.yunhalee.walkerholic.order.domain.OrderStatus;
import com.yunhalee.walkerholic.order.domain.PaymentInfoTest;
import com.yunhalee.walkerholic.orderitem.domain.OrderItem;
import com.yunhalee.walkerholic.product.domain.Product;
import com.yunhalee.walkerholic.product.domain.ProductTest;
import com.yunhalee.walkerholic.productImage.domain.ProductImageTest;
import com.yunhalee.walkerholic.user.domain.UserTest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

class OrderItemServiceTests extends ServiceTest {

    @InjectMocks
    OrderItemService orderItemService = new OrderItemService(
        orderItemRepository
    );

    private OrderItem orderItem;
    private Product product;
    private Order order;


    @BeforeEach
    public void setUp() {
        order = Order.builder()
            .id(1)
            .orderStatus(OrderStatus.ORDER)
            .payment(PaymentInfoTest.PAYMENT_INFO)
            .delivery(DeliveryInfoTest.DELIVERED_DELIVERY)
            .userId(UserTest.USER.getId())
            .orderItems(new OrderItems()).build();
        product = ProductTest.SECOND_PRODUCT;
        product.addProductImage(ProductImageTest.PRODUCT_IMAGE);
        orderItem = new OrderItem(
            1,
            20,
            product,
            order);
    }

    @Test
    public void create_order_items_from_cart_items() {
        //given
        Set<CartItem> cartItems = new HashSet<>(Arrays.asList(CartItemTest.FIRST_CART_ITEM));

        //when
        when(orderItemRepository.save(any())).thenReturn(orderItem);
        Set<OrderItem> orderItems = orderItemService.createOrderItems(cartItems, order);

        //then
        assertThat(order.getOrderItems().size()).isEqualTo(1);
        isEqual(new ArrayList<>(orderItems), new ArrayList<>(Arrays.asList(CartItemTest.FIRST_CART_ITEM)));
    }


    @Test
    public void deleteOrderItem() {
        //given

        //when
        when(orderItemRepository.findById(anyInt())).thenReturn(Optional.of(orderItem));
        orderItemService.deleteOrderItem(orderItem.getId());

        //then
        verify(orderItemRepository).delete(any());
    }

    private void isEqual(List<OrderItem> orderItems, List<CartItem> cartItems) {
        assertThat(orderItems.size()).isEqualTo(cartItems.size());
        for (int i = 0; i < orderItems.size(); i++) {
            assertThat(orderItems.get(i).getProduct().getId()).isEqualTo(cartItems.get(i).getProduct().getId());
            assertThat(orderItems.get(i).getQty()).isEqualTo(cartItems.get(i).getQty());
            assertThat(orderItems.get(i).getAmount()).isEqualTo(cartItems.get(i).getAmount());
        }
    }

}
