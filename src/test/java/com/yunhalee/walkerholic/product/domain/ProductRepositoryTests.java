package com.yunhalee.walkerholic.product.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.yunhalee.walkerholic.RepositoryTest;
import com.yunhalee.walkerholic.productImage.domain.ProductImage;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.user.domain.UserTest;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class ProductRepositoryTests extends RepositoryTest {

    private static final int PRODUCT_PER_PAGE = 9;
    private static final Pageable PAGEABLE = PageRequest.of(0, PRODUCT_PER_PAGE);

    private User user;
    private User seller;
    private Product firstProduct;
    private Product secondProduct;
    private Product thirdProduct;
    private Product fourthProduct;
    private Product fifthProduct;

    @Before
    public void setUp() {
        user = userRepository.save(UserTest.USER);
        seller = userRepository.save(UserTest.SELLER);
        firstProduct = save("first", "firstProduct", "test", Category.TUMBLER, 2, 2.00f, user);
        secondProduct = save("second", "secondProduct", "test", Category.TUMBLER, 200, 3.00f, seller);
        thirdProduct = save("third", "thirdProduct", "productTest", Category.CLOTHES, 6, 6.00f, user);
        fourthProduct = save("fourth", "fourthProduct", "test", Category.BAG, 21, 8.00f, seller);
        fifthProduct = save("fifth", "fifthProduct", "productTest", Category.CLOTHES, 1, 9.00f, seller);
    }

    @Test
    public void find_by_product_id() {
        //given
        Integer id = firstProduct.getId();

        //when
        Product product = productRepository.findByProductId(id);

        //then
        assertThat(product.getId()).isEqualTo(id);
    }

    @Test
    public void find_products_by_category_and_keyword() {
        //given
        Category category = Category.CLOTHES;
        String keyword = "th";

        //when
        Page<Product> productPage = productRepository.findAllByCategory(PAGEABLE, category, keyword);
        List<Product> products = productPage.getContent();

        //then
        assertThat(products.equals(Arrays.asList(thirdProduct, fifthProduct))).isTrue();
        for (Product product : products) {
            assertThat(product.getCategory()).isEqualTo(category);
            assertThat(product.getName().contains(keyword)).isTrue();
        }

    }

    @Test
    public void find_products_by_keyword() {
        //given
        String keyword = "second";

        //when
        Page<Product> productPage = productRepository.findAllByKeyword(PAGEABLE, keyword);
        List<Product> products = productPage.getContent();

        //then
        assertThat(products.equals(Arrays.asList(secondProduct))).isTrue();
        for (Product product : products) {
            assertThat(product.getName().contains(keyword)).isTrue();
        }
    }

    @Test
    public void find_products_by_seller_id_and_category_and_keyword() {
        //given
        Integer sellerId = user.getId();
        Category category = Category.CLOTHES;
        String keyword = "th";

        //when
        Page<Product> productPage = productRepository.findAllBySellerAndCategory(PAGEABLE, sellerId, category, keyword);
        List<Product> products = productPage.getContent();

        //then
        assertThat(products.equals(Arrays.asList(thirdProduct))).isTrue();
        for (Product product : products) {
            assertThat(product.getUser().getId()).isEqualTo(sellerId);
            assertThat(product.getCategory()).isEqualTo(category);
            assertThat(product.getName().contains(keyword)).isTrue();
        }
    }

    @Test
    public void find_products_by_seller_id_and_keyword() {
        //given
        Integer sellerId = seller.getId();
        String keyword = "th";

        //when
        Page<Product> productPage = productRepository.findAllBySellerAndKeyword(PAGEABLE, sellerId, keyword);
        List<Product> products = productPage.getContent();

        //then
        assertThat(products.equals(Arrays.asList(fourthProduct, fifthProduct))).isTrue();
        for (Product product : products) {
            assertThat(product.getUser().getId()).isEqualTo(sellerId);
            assertThat(product.getName().contains(keyword)).isTrue();
        }
    }

    private Product save(String name, String description, String brand, Category category,
        Integer stock, Float price, User user) {
        Product product = productRepository.save(Product.of(name, description, brand, category, stock, price, user));
        ProductImage productImage = productImageRepository.save(ProductImage.of(name, description, product));
        product.addProductImage(productImage);
        return product;
    }

}
