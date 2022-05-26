package com.yunhalee.walkerholic.cart.dto;

import com.yunhalee.walkerholic.cart.domain.Cart;
import com.yunhalee.walkerholic.common.dto.ItemResponse;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartResponse {

    private Integer id;

    private List<ItemResponse> items;

    private CartResponse(Cart cart, List<ItemResponse> items) {
        this.id = cart.getId();
        this.items = items;
    }

    public static CartResponse of(Cart cart, List<ItemResponse> items) {
        if (cart.getId() == null) {
            return new CartResponse();
        }
        return new CartResponse(cart, items);
    }


}
