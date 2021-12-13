package com.yunhalee.walkerholic.orderitem.domain;

import com.yunhalee.walkerholic.order.domain.Order;
import com.yunhalee.walkerholic.orderitem.domain.OrderItem;
import com.yunhalee.walkerholic.product.domain.Product;
import com.yunhalee.walkerholic.orderitem.domain.OrderItemRepository;
import com.yunhalee.walkerholic.order.domain.OrderRepository;
import com.yunhalee.walkerholic.product.domain.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Rollback(false)
@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
public class OrderItemRepositoryTests {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void createOrderItem() {
        //given
        Integer orderId = 1;
        Integer productId = 1;
        Integer qty = 10;

        Order order = orderRepository.findById(orderId).get();
        Product product = productRepository.findById(productId).get();

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQty(qty);

        //when
        OrderItem orderItem1 = orderItemRepository.save(orderItem);

        //then
        assertThat(orderItem1.getId()).isNotNull();
        assertThat(orderItem1.getOrder().getId()).isEqualTo(orderId);
        assertThat(orderItem1.getProduct().getId()).isEqualTo(productId);
        assertThat(orderItem1.getQty()).isEqualTo(qty);
    }

    @Test
    public void getOrderItem() {
        //given
        Integer orderItemId = 1;

        //when
        OrderItem orderItem = orderItemRepository.findById(orderItemId).get();

        //then
        assertThat(orderItem.getId()).isEqualTo(orderItemId);
    }

    @Test
    public void updateOrderItem() {
        //given
        Integer orderItemId = 1;
        OrderItem orderItem = orderItemRepository.findById(orderItemId).get();
        Integer originalQty = orderItem.getQty();
        orderItem.setQty(originalQty + 1);

        //when
        OrderItem orderItem1 = orderItemRepository.save(orderItem);

        //then
        assertThat(orderItem1.getQty()).isNotEqualTo(originalQty);
    }


    @Test
    public void deleteOrderItem() {
        //given
        Integer orderItemId = 1;

        //when
        orderItemRepository.deleteById(orderItemId);

        //then
        assertThat(orderItemRepository.findById(orderItemId)).isNull();
    }
}
