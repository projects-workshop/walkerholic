package com.yunhalee.walkerholic.product.domain;

import com.yunhalee.walkerholic.product.domain.Category;
import com.yunhalee.walkerholic.product.domain.Product;
import com.yunhalee.walkerholic.product.domain.ProductImage;
import com.yunhalee.walkerholic.product.domain.ProductImageRepository;
import com.yunhalee.walkerholic.product.domain.ProductRepository;
import com.yunhalee.walkerholic.user.domain.UserRepository;
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

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Rollback(false)
@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private UserRepository userRepository;

    public static final int PRODUCT_PER_PAGE = 9;


    @Test
    public void createProduct() {
        //given
        Float price = 10.00f;
        String brand = "testBrand";
        String name = "testName";
        Integer stock = 100;
        String description = "testDescription";
        Integer productImageId = 1;
        List<ProductImage> productImages = new ArrayList<>();
        productImages.add(productImageRepository.findById(productImageId).get());

        Product product = new Product(name, brand, Category.CLOTHES, stock, price, description);
//        product.setPrice(price);
//        product.setBrand(brand);
//        product.setCategory(Category.CLOTHES);
//        product.setName(name);
//        product.setStock(stock);
//        product.setDescription(description);
        product.addProductImage(productImageRepository.findById(productImageId).get());

        //when
        Product product1 = productRepository.save(product);

        //then
        assertThat(product1.getId()).isNotNull();
        assertThat(product1.getName()).isEqualTo(name);
        assertThat(product1.getPrice()).isEqualTo(price);
        assertThat(product1.getBrand()).isEqualTo(brand);
        assertThat(product1.getStock()).isEqualTo(stock);
        assertThat(product1.getDescription()).isEqualTo(description);
        assertThat(product1.getProductImages().get(0).getId()).isEqualTo(productImageId);
    }

    @Test
    public void testUpdateProduct() {
        //given
        Integer id = 4;
        Product product = productRepository.findByProductId(id);
        Float originalPrice = product.getPrice();

        product.setPrice(originalPrice + 1.00f);

        //when
        Product product1 = productRepository.save(product);

        //then
        assertThat(product1.getId()).isEqualTo(id);
        assertThat(product1.getPrice()).isNotEqualTo(originalPrice);
        assertThat(product1.getPrice()).isEqualTo(originalPrice + 1.00f);
    }

    @Test
    public void getProductById() {
        //given
        Integer id = 1;

        //when
        Product product = productRepository.findByProductId(id);

        //then
        assertThat(product.getId()).isEqualTo(id);
    }

    @Test
    public void getProductsBySellerId() {
        //given
        Integer sellerId = 1;
        Integer page = 1;

        //when
        Pageable pageable = PageRequest.of(page - 1, PRODUCT_PER_PAGE);
        Page<Product> productPage = productRepository.findByUserId(sellerId, pageable);
        List<Product> products = productPage.getContent();

        //then
        for (Product product : products) {
            assertThat(product.getUser().getId()).isEqualTo(sellerId);
        }
    }

    @Test
    public void getProductsByCategoryAndKeyword() {
        //given
        Category category = Category.CLOTHES;
        String keyword = "a";
        Integer page = 1;

        //when
        Pageable pageable = PageRequest.of(page - 1, PRODUCT_PER_PAGE);
        Page<Product> productPage = productRepository
            .findAllByCategory(pageable, category, keyword);
        List<Product> products = productPage.getContent();

        //then
        for (Product product : products) {
            assertThat(product.getCategory()).isEqualTo(category);
            assertThat(product.getName().contains(keyword)).isTrue();
        }
    }

    @Test
    public void getProductByKeyword() {
        //given
        String keyword = "a";
        Integer page = 1;

        //when
        Pageable pageable = PageRequest.of(page - 1, PRODUCT_PER_PAGE);
        Page<Product> productPage = productRepository.findAllByKeyword(pageable, keyword);
        List<Product> products = productPage.getContent();

        //then
        for (Product product : products) {
            assertThat(product.getName().contains(keyword)).isTrue();
        }
    }

    @Test
    public void getProductsBySellerIdAndCategoryAndKeyword() {
        //given
        Integer sellerId = 1;
        Category category = Category.CLOTHES;
        String keyword = "a";
        Integer page = 1;

        //when
        Pageable pageable = PageRequest.of(page - 1, PRODUCT_PER_PAGE);
        Page<Product> productPage = productRepository
            .findAllBySellerAndCategory(pageable, sellerId, category, keyword);
        List<Product> products = productPage.getContent();

        //then
        for (Product product : products) {
            assertThat(product.getUser().getId()).isEqualTo(sellerId);
            assertThat(product.getCategory()).isEqualTo(category);
            assertThat(product.getName().contains(keyword)).isTrue();
        }
    }

    @Test
    public void getProductsBySellerIdAndKeyword() {
        //given
        Integer sellerId = 1;
        String keyword = "a";
        Integer page = 1;

        //when
        Pageable pageable = PageRequest.of(page - 1, PRODUCT_PER_PAGE);
        Page<Product> productPage = productRepository
            .findAllBySellerAndKeyword(pageable, sellerId, keyword);
        List<Product> products = productPage.getContent();

        //then
        //then
        for (Product product : products) {
            assertThat(product.getUser().getId()).isEqualTo(sellerId);
            assertThat(product.getName().contains(keyword)).isTrue();
        }
    }

    @Test
    public void getProducts() {
        //given
        Integer page = 1;

        //when
        Pageable pageable = PageRequest.of(page - 1, PRODUCT_PER_PAGE);
        Page<Product> productPage = productRepository.findAllProductList(pageable);
        List<Product> products = productPage.getContent();

        //then
        assertThat(products.size()).isEqualTo(PRODUCT_PER_PAGE);
    }


    @Test
    public void deleteById() {
        //given
        Integer id = 1;
//        Product product = productRepository.findById(id).get();
//        for (ProductImage productImage : product.getProductImages()) {
//            productImageRepository.deleteById(productImage.getId());
//        }
//
        //when
        productRepository.deleteById(id);

        //then
        assertThat(productRepository.findById(id)).isNull();
    }


}
