package com.yunhalee.walkerholic.cart.api;

import static com.yunhalee.walkerholic.cartItem.api.CartItemApiTest.FIRST_CART_ITEM;
import static com.yunhalee.walkerholic.cartItem.api.CartItemApiTest.SECOND_CART_ITEM;
import static com.yunhalee.walkerholic.product.api.ProductApiTest.FIRST_PRODUCT;
import static com.yunhalee.walkerholic.product.api.ProductApiTest.SECOND_PRODUCT;
import static com.yunhalee.walkerholic.productImage.api.ProductImageApiTest.FIRST_PRODUCT_IMAGE;
import static com.yunhalee.walkerholic.productImage.api.ProductImageApiTest.SECOND_PRODUCT_IMAGE;
import static com.yunhalee.walkerholic.productImage.api.ProductImageApiTest.THIRD_PRODUCT_IMAGE;
import static com.yunhalee.walkerholic.user.domain.UserTest.FIRST_USER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yunhalee.walkerholic.ApiTest;
import com.yunhalee.walkerholic.cart.domain.Cart;
import com.yunhalee.walkerholic.cart.dto.CartResponse;
import com.yunhalee.walkerholic.common.dto.ItemResponse;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

public class CartApiTest extends ApiTest {

    public static final Cart FIRST_CART = new Cart(1, FIRST_USER.getId());

    @BeforeEach
    void setUp() {
        FIRST_PRODUCT.addProductImage(FIRST_PRODUCT_IMAGE);
        FIRST_PRODUCT.addProductImage(SECOND_PRODUCT_IMAGE);
        SECOND_PRODUCT.addProductImage(THIRD_PRODUCT_IMAGE);
        FIRST_CART.addCartItem(FIRST_CART_ITEM);
        FIRST_CART.addCartItem(SECOND_CART_ITEM);
    }

    @Test
    void create_cart() throws Exception {
        this.mockMvc.perform(post("/api/carts?userId=1")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("cart-create", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())));
    }

    @Test
    void get_cart() throws Exception {
        when(cartService.getCart(any())).thenReturn(CartResponse.of(FIRST_CART, Arrays.asList(ItemResponse.of(FIRST_CART_ITEM), ItemResponse.of(SECOND_CART_ITEM))));
        this.mockMvc.perform(get("/api/carts?userId=1")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("cart-get-by-user-id", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), cartResponseFields()));
    }

    public static ResponseFieldsSnippet cartResponseFields() {
        return responseFields(
            fieldWithPath("id").description("cart id"),
            fieldWithPath("items").description("cart items"),
            fieldWithPath("items.[].id").description("item id"),
            fieldWithPath("items.[].qty").description("item qty"),
            fieldWithPath("items.[].stock").description("product stock"),
            fieldWithPath("items.[].productId").description("product id"),
            fieldWithPath("items.[].productName").description("product name"),
            fieldWithPath("items.[].productPrice").description("product price"),
            fieldWithPath("items.[].productDescription").description("product description"),
            fieldWithPath("items.[].productBrand").description("product brand"),
            fieldWithPath("items.[].productImageUrl").description("product main imageUrl"));
    }




}
