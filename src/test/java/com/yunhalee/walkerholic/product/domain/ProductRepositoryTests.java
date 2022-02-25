package com.yunhalee.walkerholic.product.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Rollback(false)
@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    public static final int PRODUCT_PER_PAGE = 9;

    @Test
    public void find_by_product_id() {
        //given
        Integer id = 1;

        //when
        Product product = productRepository.findByProductId(id);

        //then
        assertThat(product.getId()).isEqualTo(id);
    }

    @Test
    public void find_products_by_user_id() {
        //given
        Integer userId = 1;

        //when
        Pageable pageable = PageRequest.of(0, PRODUCT_PER_PAGE);
        Page<Product> productPage = productRepository.findByUserId(userId, pageable);
        List<Product> products = productPage.getContent();

        //then
        for (Product product : products) {
            assertThat(product.getUser().getId()).isEqualTo(userId);
        }
    }

    @Test
    public void find_products_by_category_and_keyword() {
        //given
        Category category = Category.CLOTHES;
        String keyword = "a";

        //when
        Pageable pageable = PageRequest.of(0, PRODUCT_PER_PAGE);
        Page<Product> productPage = productRepository.findAllByCategory(pageable, category, keyword);
        List<Product> products = productPage.getContent();

        //then
        for (Product product : products) {
            assertThat(product.getCategory()).isEqualTo(category);
            assertThat(product.getName().contains(keyword)).isTrue();
        }
    }

    @Test
    public void find_products_by_keyword() {
        //given
        String keyword = "a";

        //when
        Pageable pageable = PageRequest.of(0, PRODUCT_PER_PAGE);
        Page<Product> productPage = productRepository.findAllByKeyword(pageable, keyword);
        List<Product> products = productPage.getContent();

        //then
        for (Product product : products) {
            assertThat(product.getName().contains(keyword)).isTrue();
        }
    }

    @Test
    public void find_products_by_seller_id_and_category_and_keyword() {
        //given
        Integer sellerId = 1;
        Category category = Category.CLOTHES;
        String keyword = "a";

        //when
        Pageable pageable = PageRequest.of(0, PRODUCT_PER_PAGE);
        Page<Product> productPage = productRepository.findAllBySellerAndCategory(pageable, sellerId, category, keyword);
        List<Product> products = productPage.getContent();

        //then
        for (Product product : products) {
            assertThat(product.getUser().getId()).isEqualTo(sellerId);
            assertThat(product.getCategory()).isEqualTo(category);
            assertThat(product.getName().contains(keyword)).isTrue();
        }
    }

    @Test
    public void find_products_by_seller_id_and_keyword() {
        //given
        Integer sellerId = 1;
        String keyword = "a";

        //when
        Pageable pageable = PageRequest.of(0, PRODUCT_PER_PAGE);
        Page<Product> productPage = productRepository.findAllBySellerAndKeyword(pageable, sellerId, keyword);
        List<Product> products = productPage.getContent();

        //then
        for (Product product : products) {
            assertThat(product.getUser().getId()).isEqualTo(sellerId);
            assertThat(product.getName().contains(keyword)).isTrue();
        }
    }

    @Test
    public void find_products() {
        //when
        Pageable pageable = PageRequest.of(0, PRODUCT_PER_PAGE);
        Page<Product> productPage = productRepository.findAllProductList(pageable);
        List<Product> products = productPage.getContent();

        //then
        assertThat(products.size()).isEqualTo(PRODUCT_PER_PAGE);
    }


}
