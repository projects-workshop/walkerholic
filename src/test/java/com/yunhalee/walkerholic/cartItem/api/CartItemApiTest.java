package com.yunhalee.walkerholic.cartItem.api;

import static com.yunhalee.walkerholic.cart.api.CartApiTest.FIRST_CART;
import static com.yunhalee.walkerholic.product.api.ProductApiTest.FIRST_PRODUCT;
import static com.yunhalee.walkerholic.product.api.ProductApiTest.SECOND_PRODUCT;
import static com.yunhalee.walkerholic.productImage.api.ProductImageApiTest.FIRST_PRODUCT_IMAGE;
import static com.yunhalee.walkerholic.productImage.api.ProductImageApiTest.SECOND_PRODUCT_IMAGE;
import static com.yunhalee.walkerholic.productImage.api.ProductImageApiTest.THIRD_PRODUCT_IMAGE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yunhalee.walkerholic.ApiTest;
import com.yunhalee.walkerholic.cartItem.domain.CartItem;
import com.yunhalee.walkerholic.cartItem.dto.CartItemRequest;
import com.yunhalee.walkerholic.common.dto.ItemResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

public class CartItemApiTest extends ApiTest {

    public static final CartItem FIRST_CART_ITEM = CartItem.builder()
        .id(1)
        .qty(2)
        .product(FIRST_PRODUCT)
        .cart(FIRST_CART).build();

    public static final CartItem SECOND_CART_ITEM = CartItem.builder()
        .id(1)
        .qty(2)
        .product(SECOND_PRODUCT)
        .cart(FIRST_CART).build();
    private static final CartItemRequest REQUEST = new CartItemRequest(FIRST_CART_ITEM.getQty(), FIRST_CART_ITEM.getProductId(), FIRST_CART.getId());

    @BeforeEach
    void setUp() {
        FIRST_PRODUCT.addProductImage(FIRST_PRODUCT_IMAGE);
        FIRST_PRODUCT.addProductImage(SECOND_PRODUCT_IMAGE);
        SECOND_PRODUCT.addProductImage(THIRD_PRODUCT_IMAGE);
    }

    @Test
    void create_cart_item() throws Exception {
        when(cartItemService.create(any())).thenReturn(ItemResponse.of(FIRST_CART_ITEM));
        this.mockMvc.perform(post("/api/cart-items")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .contentType(MediaTypes.HAL_JSON)
            .characterEncoding("utf-8")
            .content(request(REQUEST))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("cart-item-create", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), itemResponseFields()));
    }

    @Test
    void update_cart_item() throws Exception {
        when(cartItemService.update(any(), anyInt())).thenReturn(ItemResponse.of(FIRST_CART_ITEM));
        this.mockMvc.perform(put("/api/cart-items/1")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .param("qty", FIRST_CART_ITEM.getQty().toString())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("cart-item-update", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), itemResponseFields()));
    }

    @Test
    void delete_cart_item() throws Exception {
        this.mockMvc.perform(delete("/api/cart-items/1")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(document("cart-item-delete"));
    }

    public static ResponseFieldsSnippet itemResponseFields() {
        return responseFields(
            fieldWithPath("id").description("item id"),
            fieldWithPath("qty").description("item qty"),
            fieldWithPath("stock").description("product stock"),
            fieldWithPath("productId").description("product id"),
            fieldWithPath("productName").description("product name"),
            fieldWithPath("productPrice").description("product price"),
            fieldWithPath("productDescription").description("product description"),
            fieldWithPath("productBrand").description("product brand"),
            fieldWithPath("productImageUrl").description("product main imageUrl"));
    }


}
