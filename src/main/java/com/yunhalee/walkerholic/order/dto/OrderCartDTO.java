package com.yunhalee.walkerholic.order.dto;

import com.yunhalee.walkerholic.order.domain.Order;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class OrderCartDTO {

    private Integer id;

    private List<OrderItem> orderItems;

    public OrderCartDTO(Order order) {
        this.id = order.getId();
        this.orderItems = OrderItem.listItems(order.getOrderItems());
    }

    public OrderCartDTO() {
    }


    @Getter
    static class OrderItem {

        private Integer id;
        private Integer qty;
        private Integer stock;

        private Integer productId;
        private String productName;
        private Float productPrice;
        private String productDescription;
        private String productBrand;
        private String productImageUrl;

        static List<OrderItem> listItems(
            Set<com.yunhalee.walkerholic.orderitem.domain.OrderItem> orderItems) {
            List<OrderItem> orderItemList = new ArrayList<>();
            orderItems.forEach(orderItem -> orderItemList.add(new OrderItem(orderItem)));
            return orderItemList;
        }

        public OrderItem(com.yunhalee.walkerholic.orderitem.domain.OrderItem orderItem) {
            this.id = orderItem.getId();
            this.qty = orderItem.getQty();
            this.stock = orderItem.getProduct().getStock();
            this.productId = orderItem.getProduct().getId();
            this.productName = orderItem.getProduct().getName();
            this.productPrice = orderItem.getProduct().getPrice().floatValue();
            this.productDescription = orderItem.getProduct().getDescription();
            this.productBrand = orderItem.getProduct().getBrand();
            this.productImageUrl = orderItem.getProduct().getMainImageUrl();
        }
    }


}
