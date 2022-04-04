package com.yunhalee.walkerholic.cart.dto;

import com.yunhalee.walkerholic.cart.domain.Cart;
import com.yunhalee.walkerholic.common.dto.ItemResponse;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class CartResponse {

    private final Integer id;

    private final List<ItemResponse> items;

    public CartResponse(Cart cart, List<ItemResponse> items) {
        this.id = cart.getId();
        this.items = items;
    }

    public static CartResponse of(Cart cart, List<ItemResponse> items) {
        return new CartResponse(cart, items);
    }

}
