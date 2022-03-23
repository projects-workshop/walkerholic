package com.yunhalee.walkerholic.order.service;

import com.yunhalee.walkerholic.order.domain.Order;
import com.yunhalee.walkerholic.order.dto.OrderCartDTO;
import com.yunhalee.walkerholic.order.dto.OrderCreateDTO;
import com.yunhalee.walkerholic.order.dto.OrderDTO;
import com.yunhalee.walkerholic.order.dto.OrderListDTO;
import com.yunhalee.walkerholic.orderitem.domain.OrderItem;
import com.yunhalee.walkerholic.order.domain.OrderStatus;
import com.yunhalee.walkerholic.orderitem.dto.OrderItemRequest;
import com.yunhalee.walkerholic.orderitem.dto.OrderItemResponse;
import com.yunhalee.walkerholic.product.domain.Product;
import com.yunhalee.walkerholic.orderitem.domain.OrderItemRepository;
import com.yunhalee.walkerholic.order.domain.OrderRepository;
import com.yunhalee.walkerholic.product.domain.ProductRepository;
import com.yunhalee.walkerholic.user.domain.UserRepository;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.order.domain.Address;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Value("${base-url}")
    private String baseUrl;

    public static final int ORDER_LIST_PER_PAGE = 10;


    public OrderDTO createOrder(OrderCreateDTO orderCreateDTO) {

        Address address = new Address(orderCreateDTO.getAddress().getName(),
            orderCreateDTO.getAddress().getCountry(),
            orderCreateDTO.getAddress().getCity(),
            orderCreateDTO.getAddress().getZipcode(),
            orderCreateDTO.getAddress().getAddress(),
            orderCreateDTO.getAddress().getLatitude(),
            orderCreateDTO.getAddress().getLongitude());

        User user = userRepository.findById(orderCreateDTO.getUserId()).get();

        List<OrderItemRequest> orderItemCreateDTOS = orderCreateDTO.getOrderItems();
        List<OrderItem> orderItems = new ArrayList<>();
        orderItemCreateDTOS.forEach(orderItemCreateDTO -> {
            Product product = productRepository.findById(orderItemCreateDTO.getProductId()).get();
            orderItems.add(OrderItem.createOrderItem(product, orderItemCreateDTO.getQty()));
        });

        Order order = Order
            .createOrder(user, address, orderItems, orderCreateDTO.getPaymentMethod());

        orderRepository.save(order);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userRepository.findById(orderCreateDTO.getUserId()).get().getEmail());
        message.setFrom(sender);
        message.setSubject(user.getFullname() + " : Created Order " + order.getId());
        message
            .setText("Hello" + user.getFirstname() + "! Your order has been made successfully. " +
                "\n\nOrder Id :  " + order.getId() +
                "\nTotal Amount : " + order.getTotalAmount() +
                "\nPaid At : " + order.getPaidAt() +
                "\n\nFor more Details visit " + baseUrl + "/order/" + order.getId());
        mailSender.send(message);

        return new OrderDTO(order);
    }

    public OrderListDTO cancelOrder(Integer id) {
        Order order = orderRepository.findById(id).get();
        order.cancel();
        orderRepository.save(order);

        SimpleMailMessage message = new SimpleMailMessage();
        User user = order.getUser();
        message.setTo(user.getEmail());
        message.setFrom(sender);
        message.setSubject(user.getFullname() + " : Cancel Order " + id);
        message.setText(
            "Hello" + user.getFirstname() + "! Your order has been canceled successfully. " +
                "\n\nOrder Id :  " + order.getId() +
                "\nTotal Amount : " + order.getTotalAmount() +
                "\nCanceled At : " + order.getUpdatedAt() +
                "\n\nFor more Details visit " + baseUrl + "/order/" + order.getId());
        mailSender.send(message);

        return new OrderListDTO(order);
    }

    public OrderListDTO deliverOrder(Integer id) {
        Order order = orderRepository.findById(id).get();
        order.deliver();
        orderRepository.save(order);

        return new OrderListDTO(order);
    }

    public OrderDTO getOrder(Integer id) {
        Order order = orderRepository.findByOrderId(id);
        return new OrderDTO(order);
    }

    public OrderCartDTO getCart(Integer id) {
        Order order = orderRepository.findCartItemsByUserId(OrderStatus.CART, id);
        if (order == null) {
            return new OrderCartDTO();
        }
        return new OrderCartDTO(order);
    }

    public Integer createCart(Integer id) {
        Order order = new Order();
        User user = userRepository.findById(id).get();
        order.setOrderStatus(OrderStatus.CART);
        order.setUser(user);
        orderRepository.save(order);
        return order.getId();
    }

    public OrderItemResponse addToCart(Integer id, OrderItemRequest orderItemCreateDTO) {
        Order order = orderRepository.findById(id).get();
        Product product = productRepository.findById(orderItemCreateDTO.getProductId()).get();

        OrderItemResponse createdOrderItemDTO;
        if (orderItemCreateDTO.getId() == null) {
            OrderItem orderItem = OrderItem.createOrderItem(product, orderItemCreateDTO.getQty());
            orderItemRepository.save(orderItem);
            order.addOrderItem(orderItem);
            createdOrderItemDTO = new OrderItemResponse(orderItem);
        } else {
            OrderItem orderItem = orderItemRepository.findById(orderItemCreateDTO.getId()).get();
            orderItem.changeQty(orderItemCreateDTO.getQty());
            orderItemRepository.save(orderItem);
            order.addOrderItem(orderItem);
            createdOrderItemDTO = new OrderItemResponse(orderItem);
        }

        orderRepository.save(order);
        return createdOrderItemDTO;
    }

    public HashMap<String, Object> getOrderList(Integer page) {
        Pageable pageable = PageRequest.of(page - 1, ORDER_LIST_PER_PAGE);
        Page<Order> orderPage = orderRepository.findAll(pageable, OrderStatus.CART);
        List<Order> orders = orderPage.getContent();
        List<OrderListDTO> orderListDTOS = new ArrayList<>();
        orders.forEach(order -> orderListDTOS.add(new OrderListDTO(order)));

        HashMap<String, Object> orderList = new HashMap<>();
        orderList.put("orders", orderListDTOS);
        orderList.put("totalElement", orderPage.getTotalElements());
        orderList.put("totalPage", orderPage.getTotalPages());

        return orderList;
    }

    public HashMap<String, Object> getOrderListBySeller(Integer page, Integer id) {
        Pageable pageable = PageRequest.of(page - 1, ORDER_LIST_PER_PAGE);
        Page<Order> orderPage = orderRepository.findBySellerId(pageable, id, OrderStatus.CART);
        List<Order> orders = orderPage.getContent();
        List<OrderListDTO> orderListDTOS = new ArrayList<>();
        orders.forEach(order -> orderListDTOS.add(new OrderListDTO(order)));

        HashMap<String, Object> orderList = new HashMap<>();
        orderList.put("orders", orderListDTOS);
        orderList.put("totalElement", orderPage.getTotalElements());
        orderList.put("totalPage", orderPage.getTotalPages());

        return orderList;
    }

    public HashMap<String, Object> getOrderListByUser(Integer page, Integer id) {
        Pageable pageable = PageRequest.of(page - 1, ORDER_LIST_PER_PAGE);
        Page<Order> orderPage = orderRepository.findByUserId(pageable, id, OrderStatus.CART);
        List<Order> orders = orderPage.getContent();
        List<OrderListDTO> orderListDTOS = new ArrayList<>();
        orders.forEach(order -> orderListDTOS.add(new OrderListDTO(order)));

        HashMap<String, Object> orderList = new HashMap<>();
        orderList.put("orders", orderListDTOS);
        orderList.put("totalElement", orderPage.getTotalElements());
        orderList.put("totalPage", orderPage.getTotalPages());

        return orderList;
    }

    public void payOrder(OrderCreateDTO orderCreateDTO) {
        Order order = orderRepository.findById(orderCreateDTO.getId()).get();
        Address address = new Address(orderCreateDTO.getAddress().getName(),
            orderCreateDTO.getAddress().getCountry(), orderCreateDTO.getAddress().getCity(),
            orderCreateDTO.getAddress().getZipcode(), orderCreateDTO.getAddress().getAddress());
        order.setAddress(address);
        order.setShipping(orderCreateDTO.getShipping());
        order.setPaymentMethod(orderCreateDTO.getPaymentMethod());
        order.setPaid(true);
        order.setPaidAt(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.ORDER);
        orderRepository.save(order);
    }
}
