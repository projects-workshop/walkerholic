package com.yunhalee.walkerholic.cartItem.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.yunhalee.walkerholic.RepositoryTest;
import com.yunhalee.walkerholic.cart.domain.Cart;
import com.yunhalee.walkerholic.product.domain.Category;
import com.yunhalee.walkerholic.product.domain.Product;
import com.yunhalee.walkerholic.productImage.domain.ProductImage;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.user.domain.UserTest;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;

public class CartItemRepositoryTest extends RepositoryTest {

    private User user;
    private User seller;
    private Product product;
    private CartItem firstCartItem;
    private CartItem secondCartItem;
    private Cart cart;

    @Before
    public void setUp() {
        user = userRepository.save(UserTest.USER);
        seller = userRepository.save(UserTest.SELLER);
        product = productRepository.save(Product.of("first", "firstProduct", "test", Category.TUMBLER, 200, 2.00f, seller));
        ProductImage productImage = productImageRepository.save(ProductImage.of("first", "firstProduct", product));
        product.addProductImage(productImage);
        cart = cartRepository.save(Cart.of(user.getId()));
        firstCartItem = cartItemRepository.save(CartItem.of(5, product, cart));
        cart.addCartItem(firstCartItem);
    }

    @Test
    public void delete_all_cart_items_by_cart_id() {
        //given
        addSecondCartItem();

        //when
        cartItemRepository.deleteAllByCart(cart);

        //then
        assertThat(cartItemRepository.findById(firstCartItem.getId())).isEqualTo(Optional.empty());
        assertThat(cartItemRepository.findById(secondCartItem.getId())).isEqualTo(Optional.empty());
    }

    private void addSecondCartItem() {
        secondCartItem = cartItemRepository.save(CartItem.of(2, product, cart));
        cart.addCartItem(secondCartItem);
    }



}