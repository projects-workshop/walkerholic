package com.yunhalee.walkerholic.order.api;

import static com.yunhalee.walkerholic.orderitem.api.OrderItemApiTest.FIRST_ORDER_ITEM;
import static com.yunhalee.walkerholic.orderitem.api.OrderItemApiTest.SECOND_ORDER_ITEM;
import static com.yunhalee.walkerholic.product.api.ProductApiTest.FIRST_PRODUCT;
import static com.yunhalee.walkerholic.product.api.ProductApiTest.SECOND_PRODUCT;
import static com.yunhalee.walkerholic.productImage.api.ProductImageApiTest.FIRST_PRODUCT_IMAGE;
import static com.yunhalee.walkerholic.productImage.api.ProductImageApiTest.SECOND_PRODUCT_IMAGE;
import static com.yunhalee.walkerholic.productImage.api.ProductImageApiTest.THIRD_PRODUCT_IMAGE;
import static com.yunhalee.walkerholic.user.domain.UserTest.FIRST_USER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yunhalee.walkerholic.ApiTest;
import com.yunhalee.walkerholic.common.dto.ItemResponse;
import com.yunhalee.walkerholic.order.domain.DeliveryInfoTest;
import com.yunhalee.walkerholic.order.domain.Order;
import com.yunhalee.walkerholic.order.domain.OrderItems;
import com.yunhalee.walkerholic.order.domain.OrderStatus;
import com.yunhalee.walkerholic.order.domain.PaymentInfoTest;
import com.yunhalee.walkerholic.order.dto.AddressResponse;
import com.yunhalee.walkerholic.order.dto.OrderRequest;
import com.yunhalee.walkerholic.order.dto.OrderResponse;
import com.yunhalee.walkerholic.order.dto.OrderResponses;
import com.yunhalee.walkerholic.order.dto.SimpleOrderResponse;
import com.yunhalee.walkerholic.user.dto.UserIconResponse;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

public class OrderApiTest extends ApiTest {

    public static final Order FIRST_ORDER = Order.builder()
        .id(1)
        .orderStatus(OrderStatus.ORDER)
        .payment(PaymentInfoTest.PAYMENT_INFO)
        .delivery(DeliveryInfoTest.NOT_DELIVERED_DELIVERY)
        .userId(1)
        .orderItems(new OrderItems())
        .timeSeparator("2020-1-1 10:22").build();
    public static final Order SECOND_ORDER = Order.builder()
        .id(2)
        .orderStatus(OrderStatus.ORDER)
        .payment(PaymentInfoTest.PAYMENT_INFO)
        .delivery(DeliveryInfoTest.NOT_DELIVERED_DELIVERY)
        .userId(1)
        .orderItems(new OrderItems())
        .timeSeparator("2020-1-1 10:22").build();
    public static final Order DELIVERED_ORDER = Order.builder()
        .id(1)
        .orderStatus(OrderStatus.ORDER)
        .payment(PaymentInfoTest.PAYMENT_INFO)
        .delivery(DeliveryInfoTest.DELIVERED_DELIVERY)
        .userId(1)
        .orderItems(new OrderItems())
        .timeSeparator("2020-1-1 10:22").build();
    public static final Order CANCELED_ORDER = Order.builder()
        .id(1)
        .orderStatus(OrderStatus.CANCEL)
        .payment(PaymentInfoTest.PAYMENT_INFO)
        .delivery(DeliveryInfoTest.DELIVERED_DELIVERY)
        .userId(1)
        .orderItems(new OrderItems())
        .timeSeparator("2020-1-1 10:22").build();

    private static final OrderRequest REQUEST = new OrderRequest(FIRST_ORDER.getUserId(),
        FIRST_ORDER.getShipping().floatValue(),
        FIRST_ORDER.getPaymentMethod(),
        FIRST_ORDER.getTransactionId(),
        new AddressResponse(FIRST_ORDER.getAddress()));

    @BeforeEach
    void setUp() {
        FIRST_PRODUCT.addProductImage(FIRST_PRODUCT_IMAGE);
        FIRST_PRODUCT.addProductImage(SECOND_PRODUCT_IMAGE);
        SECOND_PRODUCT.addProductImage(THIRD_PRODUCT_IMAGE);
        FIRST_ORDER.addOrderItem(FIRST_ORDER_ITEM);
        FIRST_ORDER.addOrderItem(SECOND_ORDER_ITEM);
    }

    @Test
    void create_order() throws Exception {
        when(orderService.createOrder(any())).thenReturn(OrderResponse.of(FIRST_ORDER,
            UserIconResponse.of(FIRST_USER),
            Arrays.asList(ItemResponse.of(FIRST_ORDER_ITEM), ItemResponse.of(SECOND_ORDER_ITEM))));
        this.mockMvc.perform(post("/api/orders")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .contentType(MediaTypes.HAL_JSON)
            .characterEncoding("utf-8")
            .content(request(REQUEST))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("order-create", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), orderResponseFields()));
    }

    @Test
    void get_order() throws Exception {
        when(orderService.getOrder(any())).thenReturn(OrderResponse.of(FIRST_ORDER,
            UserIconResponse.of(FIRST_USER),
            Arrays.asList(ItemResponse.of(FIRST_ORDER_ITEM), ItemResponse.of(SECOND_ORDER_ITEM))));
        this.mockMvc.perform(get("/api/orders/1")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("order-get-by-id", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), orderResponseFields()));
    }


    @Test
    void get_orders() throws Exception {
        when(orderService.getOrderList(any())).thenReturn(OrderResponses.of(
            Arrays.asList(SimpleOrderResponse.of(FIRST_ORDER, UserIconResponse.of(FIRST_USER)),
                SimpleOrderResponse.of(SECOND_ORDER, UserIconResponse.of(FIRST_USER))),
            2L,
            1));
        this.mockMvc.perform(get("/api/orders")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .param("page", "1")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("order-get-all", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), orderResponsesFields()));
    }

    @Test
    void get_orders_by_seller() throws Exception {
        when(orderService.getOrderListBySeller(any(), anyInt())).thenReturn(OrderResponses.of(
            Arrays.asList(SimpleOrderResponse.of(FIRST_ORDER, UserIconResponse.of(FIRST_USER)),
                SimpleOrderResponse.of(SECOND_ORDER, UserIconResponse.of(FIRST_USER))),
            2L,
            1));
        this.mockMvc.perform(get("/api/orders")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .param("page", "1")
            .param("sellerId", "1")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("order-get-all-by-seller-id", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), orderResponsesFields()));
    }

    @Test
    void get_orders_by_user() throws Exception {
        when(orderService.getOrderListByUser(any(), anyInt())).thenReturn(OrderResponses.of(
            Arrays.asList(SimpleOrderResponse.of(FIRST_ORDER, UserIconResponse.of(FIRST_USER)),
                SimpleOrderResponse.of(SECOND_ORDER, UserIconResponse.of(FIRST_USER))),
            2L,
            1));
        this.mockMvc.perform(get("/api/orders")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .param("page", "1")
            .param("userId", "1")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("order-get-all-by-user-id", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), orderResponsesFields()));
    }


    @Test
    void deliver_order() throws Exception {
        when(orderService.deliverOrder(any())).thenReturn(SimpleOrderResponse.of(DELIVERED_ORDER, UserIconResponse.of(FIRST_USER)));
        this.mockMvc.perform(put("/api/orders/1/delivery")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("order-deliver", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), simpleOrderResponseFields()));
    }

    @Test
    void cancel_order() throws Exception {
        when(orderService.cancelOrder(any())).thenReturn(SimpleOrderResponse.of(CANCELED_ORDER, UserIconResponse.of(FIRST_USER)));
        this.mockMvc.perform(put("/api/orders/1/cancellation")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("order-cancel", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), simpleOrderResponseFields()));
    }


    public static ResponseFieldsSnippet orderResponseFields() {
        return responseFields(
            fieldWithPath("id").description("order id"),
            fieldWithPath("orderStatus").description("order status"),
            fieldWithPath("paymentMethod").description("order payment method"),
            fieldWithPath("transactionId").description("order transaction id"),
            fieldWithPath("paidAt").description("the time when order has payed"),
            fieldWithPath("delivered").description("whether this order has delivered or not"),
            fieldWithPath("deliveredAt").description("the time when order has delivered"),
            fieldWithPath("address").description("where to deliver this order"),
            fieldWithPath("address.name").description("address name"),
            fieldWithPath("address.country").description("address country"),
            fieldWithPath("address.city").description("address city"),
            fieldWithPath("address.zipcode").description("address zipcode"),
            fieldWithPath("address.address").description("address details"),
            fieldWithPath("address.latitude").description("address latitude"),
            fieldWithPath("address.longitude").description("address longitude"),
            fieldWithPath("user").description("user who ordered this"),
            fieldWithPath("user.id").description("user id"),
            fieldWithPath("user.fullname").description("user fullName"),
            fieldWithPath("user.imageUrl").description("user imageUrl"),
            fieldWithPath("items").description("order items"),
            fieldWithPath("items.[].id").description("item id"),
            fieldWithPath("items.[].qty").description("item qty"),
            fieldWithPath("items.[].stock").description("product stock"),
            fieldWithPath("items.[].productId").description("product id"),
            fieldWithPath("items.[].productName").description("product name"),
            fieldWithPath("items.[].productPrice").description("product price"),
            fieldWithPath("items.[].productDescription").description("product description"),
            fieldWithPath("items.[].productBrand").description("product brand"),
            fieldWithPath("items.[].productImageUrl").description("product main imageUrl"),
            fieldWithPath("total").description("the total amount of this order"),
            fieldWithPath("shipping").description("shipping cost of this order"));
    }


    public static ResponseFieldsSnippet orderResponsesFields() {
        return responseFields(
            fieldWithPath("orders").description("found orders"),
            fieldWithPath("orders.[].id").description("order id"),
            fieldWithPath("orders.[].orderStatus").description("order status"),
            fieldWithPath("orders.[].paidAt").description("the time when order has payed"),
            fieldWithPath("orders.[].delivered").description("whether this order has delivered or not"),
            fieldWithPath("orders.[].deliveredAt").description("the time when order has delivered"),
            fieldWithPath("orders.[].user").description("user who ordered this order"),
            fieldWithPath("orders.[].user.id").description("user id"),
            fieldWithPath("orders.[].user.fullname").description("user fullName"),
            fieldWithPath("orders.[].user.imageUrl").description("user imageUrl"),
            fieldWithPath("orders.[].totalAmount").description("the total amount of this order"),
            fieldWithPath("totalElement").description("the number of orders"),
            fieldWithPath("totalPage").description("the number of totalPage"));
    }


    public static ResponseFieldsSnippet simpleOrderResponseFields() {
        return responseFields(
            fieldWithPath("id").description("order id"),
            fieldWithPath("orderStatus").description("order status"),
            fieldWithPath("paidAt").description("the time when order has payed"),
            fieldWithPath("delivered").description("whether this order has delivered or not"),
            fieldWithPath("deliveredAt").description("the time when order has delivered"),
            fieldWithPath("user").description("user who ordered this order"),
            fieldWithPath("user.id").description("user id"),
            fieldWithPath("user.fullname").description("user fullName"),
            fieldWithPath("user.imageUrl").description("user imageUrl"),
            fieldWithPath("totalAmount").description("the total amount of this order"));
    }

}
