package com.yunhalee.walkerholic.order.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class OrderResponses {

    private final List<SimpleOrderResponse> orders;
    private final Long totalElement;
    private final Integer totalPage;

    private OrderResponses(List<SimpleOrderResponse> orders, Long totalElement, Integer totalPage) {
        this.orders = orders;
        this.totalElement = totalElement;
        this.totalPage = totalPage;
    }

    public static OrderResponses of(List<SimpleOrderResponse> orders, Long totalElement, Integer totalPage) {
        return new OrderResponses(orders, totalElement, totalPage);
    }
}
