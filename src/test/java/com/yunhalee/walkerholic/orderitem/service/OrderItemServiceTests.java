package com.yunhalee.walkerholic.orderitem.service;

import com.yunhalee.walkerholic.MockBeans;
import com.yunhalee.walkerholic.order.domain.Order;
import com.yunhalee.walkerholic.order.domain.OrderTest;
import com.yunhalee.walkerholic.orderitem.domain.OrderItem;
import com.yunhalee.walkerholic.orderitem.dto.OrderItemRequest;
import com.yunhalee.walkerholic.orderitem.dto.OrderItemResponse;
import com.yunhalee.walkerholic.orderitem.dto.OrderItemResponses;
import com.yunhalee.walkerholic.product.domain.Product;
import com.yunhalee.walkerholic.product.domain.ProductTest;
import com.yunhalee.walkerholic.productImage.domain.ProductImageTest;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

class OrderItemServiceTests extends MockBeans {

    @InjectMocks
    OrderItemService orderItemService = new OrderItemService(
        orderItemRepository,
        productService,
        orderRepository
    );

    private OrderItem orderItem;
    private Product product;
    private Order order;


    @BeforeEach
    public void setUp(){
        order = OrderTest.CART;
        product = ProductTest.FIRST_PRODUCT;
        product.addProductImage(ProductImageTest.PRODUCT_IMAGE);
        orderItem = new OrderItem(
            1,
            3,
            product,
            order);
    }

    @Test
    public void createOrderItem(){
        //given
        OrderItemRequest request = new OrderItemRequest(orderItem.getQty(), product.getId(), order.getId());

        //when
        when(orderRepository.findById(anyInt())).thenReturn(Optional.of(order));
        when(productService.findProductById(anyInt())).thenReturn(product);
        when(orderItemRepository.save(any())).thenReturn(orderItem);
        OrderItemResponse response = orderItemService.create(1, request);

        //then
        assertThat(response.getId()).isEqualTo(orderItem.getId());
        assertThat(response.getQty()).isEqualTo(orderItem.getQty());
        assertThat(response.getProductId()).isEqualTo(product.getId());
        assertThat(order.getOrderItems().size()).isEqualTo(1);
    }



    @Test
    public void updateOrderItem() {
        //given
        Integer qty = 200;

        //when
        when(orderItemRepository.findById(anyInt())).thenReturn(Optional.of(orderItem));
        OrderItemResponse response = orderItemService.update(orderItem.getId(), qty);

        //then
        assertThat(response.getQty()).isEqualTo(qty);
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

    @Test
    public void createOrderItems(){
        //given
        OrderItemRequest request = new OrderItemRequest(orderItem.getQty(), product.getId());

        //when
        when(productService.findProductById(anyInt())).thenReturn(product);
        when(orderItemRepository.save(any())).thenReturn(orderItem);
        OrderItemResponses responses = orderItemService.createOrderItems(order, Arrays.asList(request, request));

        //then
        assertThat(responses.getOrderItems().size()).isEqualTo(2);
        isEqual(responses.getOrderItems().get(0),responses.getOrderItems().get(1));

    }

    private void isEqual(OrderItemResponse firstItem, OrderItemResponse secondItem){
        assertThat(firstItem.getId()).isEqualTo(secondItem.getId());
        assertThat(firstItem.getQty()).isEqualTo(secondItem.getQty());
        assertThat(firstItem.getStock()).isEqualTo(secondItem.getStock());
        assertThat(firstItem.getProductId()).isEqualTo(secondItem.getProductId());
    }
}
