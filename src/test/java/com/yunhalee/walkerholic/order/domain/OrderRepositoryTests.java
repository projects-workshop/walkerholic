package com.yunhalee.walkerholic.order.domain;

import com.yunhalee.walkerholic.orderitem.domain.OrderItem;
import com.yunhalee.walkerholic.product.domain.Category;
import com.yunhalee.walkerholic.product.domain.Product;
import com.yunhalee.walkerholic.product.domain.ProductRepository;
import com.yunhalee.walkerholic.productImage.domain.ProductImage;
import com.yunhalee.walkerholic.productImage.domain.ProductImageRepository;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.orderitem.domain.OrderItemRepository;
import com.yunhalee.walkerholic.user.domain.UserRepository;
import com.yunhalee.walkerholic.user.domain.UserTest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderRepositoryTests {

    public static final int ORDER_LIST_PER_PAGE = 10;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductImageRepository productImageRepository;


    private User user;
    private User seller;
    private Order firstOrder;
    private Order secondOrder;
    private Order thirdOrder;
    private Order fourthOrder;
    private Order fifthOrder;
    private Product product;

    @Before
    public void setUp() {
        user = userRepository.save(UserTest.USER);
        seller = userRepository.save(UserTest.SELLER);
        product = productRepository.save(Product.of("first", "firstProduct", "test", Category.TUMBLER, 2, 2.00f, seller));
        ProductImage productImage = productImageRepository.save(ProductImage.of("first", "firstProduct", product));
        product.addProductImage(productImage);
        firstOrder = save(OrderStatus.ORDER, PaymentInfoTest.PAYMENT_INFO, DeliveryInfoTest.NOT_DELIVERED_DELIVERY, user.getId());
        secondOrder = save(OrderStatus.CANCEL, PaymentInfoTest.PAYMENT_INFO, DeliveryInfoTest.NOT_DELIVERED_DELIVERY, user.getId());
        thirdOrder = save(OrderStatus.ORDER, PaymentInfoTest.PAYMENT_INFO, DeliveryInfoTest.DELIVERED_DELIVERY, user.getId());
        fourthOrder = save(OrderStatus.ORDER, PaymentInfoTest.PAYMENT_INFO, DeliveryInfoTest.NOT_DELIVERED_DELIVERY, seller.getId());
        fifthOrder = save(OrderStatus.CANCEL, PaymentInfoTest.PAYMENT_INFO, DeliveryInfoTest.NOT_DELIVERED_DELIVERY, seller.getId());
    }

    private Order save(OrderStatus orderStatus, Payment payment, Delivery deliveryInfo, Integer userId) {
        Order order = Order.builder()
            .orderStatus(orderStatus)
            .payment(payment)
            .delivery(deliveryInfo)
            .userId(userId)
            .orderItems(new OrderItems()).build();
        orderRepository.save(order);
        OrderItem orderItem = new OrderItem(10, product, order);
        orderItemRepository.save(orderItem);
        order.addOrderItem(orderItem);
        return order;
    }


    @Test
    public void find_by_order_id() {
        //given

        //when
        Order order = orderRepository.findByOrderId(firstOrder.getId());

        //then
        assertThat(order.getId()).isEqualTo(firstOrder.getId());
    }

    @Test
    public void find_by_seller_id() {
        //given
        Pageable pageable = PageRequest.of(0, ORDER_LIST_PER_PAGE);

        //when
        Page<Order> orderPage = orderRepository.findBySellerId(pageable, seller.getId());
        List<Order> orders = orderPage.getContent();

        //then
        assertThat(orders.equals(Arrays.asList(firstOrder, secondOrder, thirdOrder, fourthOrder, fifthOrder))).isTrue();
    }


    @Test
    public void find_by_user_id() {
        //given

        //when
        Pageable pageable = PageRequest.of(0, ORDER_LIST_PER_PAGE);
        Page<Order> orderPage = orderRepository.findByUserId(pageable, user.getId());
        List<Order> orders = orderPage.getContent();

        //then
        assertThat(orders.equals(Arrays.asList(firstOrder, secondOrder, thirdOrder))).isTrue();
    }

    @Test
    public void find_all_orders() {
        //given

        //when
        Pageable pageable = PageRequest.of(0, ORDER_LIST_PER_PAGE);
        Page<Order> orderPage = orderRepository.findAllOrders(pageable);
        List<Order> orders = orderPage.getContent();

        //then
        LocalDateTime createdAt = orders.get(0).getCreatedAt();
        for (Order order : orders) {
            assertThat(createdAt.isBefore(order.getCreatedAt()) || createdAt.isEqual(order.getCreatedAt())).isTrue();
            createdAt = order.getCreatedAt();
        }
    }

    @Test
    public void find_existence_by_created_at_between_from_to() {
        LocalDateTime from = LocalDateTime.now().minusSeconds(30);
        LocalDateTime to = LocalDateTime.now();

        assertThat(orderRepository.existsByCreatedAtBetweenAndUserId(from, to, user.getId())).isTrue();
    }

}
