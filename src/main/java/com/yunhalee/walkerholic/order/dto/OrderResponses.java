package com.yunhalee.walkerholic.order.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderResponses {

    private List<SimpleOrderResponse> orders;
    private Long totalElement;
    private Integer totalPage;

    private OrderResponses(List<SimpleOrderResponse> orders, Long totalElement, Integer totalPage) {
        this.orders = orders;
        this.totalElement = totalElement;
        this.totalPage = totalPage;
    }

    public static OrderResponses of(List<SimpleOrderResponse> orders, Long totalElement, Integer totalPage) {
        return new OrderResponses(orders, totalElement, totalPage);
    }
}
