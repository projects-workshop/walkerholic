package com.yunhalee.walkerholic.product.service;

import com.yunhalee.walkerholic.MockBeans;
import com.yunhalee.walkerholic.product.domain.Category;
import com.yunhalee.walkerholic.product.domain.ProductImageTest;
import com.yunhalee.walkerholic.product.dto.ProductImageResponse;
import com.yunhalee.walkerholic.product.dto.ProductRequest;
import com.yunhalee.walkerholic.product.dto.ProductResponse;
import com.yunhalee.walkerholic.product.dto.ProductResponses;
import com.yunhalee.walkerholic.product.dto.SimpleProductResponse;
import com.yunhalee.walkerholic.product.domain.Product;
import com.yunhalee.walkerholic.user.domain.Role;
import com.yunhalee.walkerholic.user.domain.User;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@Transactional
class ProductServiceTests extends MockBeans {

    private static final String NAME = "testProduct";
    private static final String DESCRIPTION = "testDescription";
    private static final String BRAND = "testBrand";
    private static final String CATEGORY = "TUMBLER";
    private static final Integer STOCK = 3;
    private static final Float PRICE = 2.3f;
    private static final MultipartFile MULTIPART_FILE = new MockMultipartFile("uploaded-file",
        "sampleFile.txt",
        "text/plain",
        "This is the file content".getBytes());

    @InjectMocks
    private ProductService productService = new ProductService(productRepository,
        reviewRepository,
        userService,
        productImageRepository,
        s3ImageUploader,
        "https://walkerholic-test-you.s3.ap-northeast10.amazonaws.com");

    private Product product;
    private Product firstProduct;
    private Product secondProduct;
    private Product thirdProduct;
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User(1,
            "testFirstName",
            "TestLastName",
            "test@example.com",
            "12345678",
            Role.USER);

        product = new Product(NAME,
            DESCRIPTION,
            BRAND,
            Category.TUMBLER,
            STOCK,
            PRICE,
            user);
    }

    public void setOtherProduct(){
        firstProduct = new Product("apple",
            "testBrand",
            Category.TUMBLER,
            3,
            2.0f,
            "testDescription");
        secondProduct = new Product("banana",
            "testBrand",
            "testDescription",
            Category.CLOTHES,
            32,
            21.0f,
            user);
        thirdProduct= new Product("peach",
            "testBrand",
            Category.CLOTHES,
            5,
            28.0f,
            "testDescription");
    }

    @Test
    public void createProduct() throws IOException {
        //given
        ProductRequest request = new ProductRequest(NAME, DESCRIPTION, BRAND, CATEGORY, STOCK, PRICE, 1);

        //when
        when(userService.findUserById(anyInt())).thenReturn(user);
        when(productRepository.save(any())).thenReturn(product);
        when(s3ImageUploader.uploadFile(any(), any())).thenReturn(ProductImageTest.PRODUCT_IMAGE.getFilePath());
        when(productImageRepository.save(any())).thenReturn(ProductImageTest.PRODUCT_IMAGE);
        SimpleProductResponse response = productService.createProduct(request, Arrays.asList(MULTIPART_FILE));

        //then
        equals(response, product);
        assertThat(product.getImageUrls().size()).isEqualTo(1);
    }

    @Test
    public void updateProduct() throws IOException {
        //given
        String updatedDescription = "updateTest";
        product.addProductImage(ProductImageTest.PRODUCT_IMAGE);
        ProductRequest request = new ProductRequest(NAME, updatedDescription, BRAND, CATEGORY, STOCK, PRICE, 1);

        //when
        when(userService.findUserById(anyInt())).thenReturn(user);
        when(productRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(product));
        when(s3ImageUploader.uploadFile(any(), any())).thenReturn(ProductImageTest.PRODUCT_SECOND_IMAGE.getFilePath());
        when(productImageRepository.save(any())).thenReturn(ProductImageTest.PRODUCT_SECOND_IMAGE);
        SimpleProductResponse response = productService.updateProduct(request,
            Arrays.asList(MULTIPART_FILE),
            Arrays.asList(ProductImageTest.PRODUCT_IMAGE.getFilePath()));

        //then
        verify(productImageRepository).deleteByFilePath(any());
        verify(s3ImageUploader).deleteFile(any());
        assertThat(response.getImagesUrl()).contains(ProductImageTest.PRODUCT_SECOND_IMAGE.getFilePath());
        assertThat(response.getImagesUrl()).doesNotContain(ProductImageTest.PRODUCT_IMAGE.getFilePath());
        assertThat(response.getDescription()).isEqualTo(updatedDescription);
    }


    @Test
    public void find_product_with_id() {
        //given
        product.addProductImage(ProductImageTest.PRODUCT_IMAGE);

        //when
        when(productRepository.findByProductId(anyInt())).thenReturn(product);
        when(reviewRepository.findAllByPostId(anyInt())).thenReturn(Arrays.asList());
        ProductResponse response = productService.getProduct(1);

        //then
        equals(response, product);
    }

    @Test
    public void find_products_by_sort_and_keyword() {
        //given
        setOtherProduct();

        //when
        doReturn(new PageImpl<>(Arrays.asList(firstProduct, product, thirdProduct))).when(productRepository).findAllByKeyword(any(), any());
        ProductResponses responses = productService.getProducts(1, "lowest", null, "ba");

        //then
        assertThat(responses.getTotalElement()).isEqualTo(3L);
    }

    @Test
    public void find_products_by_sort_and_category_and_keyword(){
        //given
        setOtherProduct();

        //when
        doReturn(new PageImpl<>(Arrays.asList(secondProduct))).when(productRepository).findAllByCategory(any(), any(), any());
        ProductResponses responses = productService.getProducts(1, "lowest", "CLOTHES", "ba");

        //then
        assertThat(responses.getTotalElement()).isEqualTo(1L);
    }

    @Test
    public void find_products_by_seller_and_sort_and_keyword() {
        //given
        setOtherProduct();

        //when
        when(userService.findUserById(anyInt())).thenReturn(user);
        doReturn(new PageImpl<>(Arrays.asList(product))).when(productRepository).findAllBySellerAndKeyword(any(), any(), any());
        ProductResponses responses = productService.getProductsBySeller(1, 1, "lowest", null, "p");

        //then
        assertThat(responses.getTotalElement()).isEqualTo(1);
        assertThat(responses.getSeller().getId()).isEqualTo(user.getId());
    }

    @Test
    public void find_products_by_seller_and_sort_and_category_and_keyword() {
        //given
        setOtherProduct();

        //when
        when(userService.findUserById(anyInt())).thenReturn(user);
        doReturn(new PageImpl<>(Arrays.asList(secondProduct))).when(productRepository).findAllBySellerAndCategory(any(), any(), any(), any());
        ProductResponses responses = productService.getProductsBySeller(1, 1, "lowest", "CLOTHES", "ba");

        //then
        assertThat(responses.getTotalElement()).isEqualTo(1);
        assertThat(responses.getSeller().getId()).isEqualTo(user.getId());
    }

    @Test
    public void find_all_products_list_by_sort() {
        //given
        setOtherProduct();

        //when
        doReturn(new PageImpl<>(Arrays.asList(firstProduct, product, secondProduct, thirdProduct))).when(productRepository).findAllProductList(any());
        ProductResponses responses = productService.getAllProductList(1, "lowest");

        //then
        assertThat(responses.getTotalElement()).isEqualTo(3);
    }

    @Test
    public void find_all_products_list_by_seller_and_sort() {
        //given
        setOtherProduct();

        //when
        doReturn(new PageImpl<>(Arrays.asList(product, secondProduct))).when(productRepository).findByUserId(any(), any());
        ProductResponses responses = productService.getProductListBySeller(1, "lowest", 1);

        //then
        assertThat(responses.getTotalElement()).isEqualTo(2);
    }

    @Test
    public void deleteProduct(){
        //given
        Integer id = 1;

        //when
        productService.deleteProduct(id);

        //then
        verify(s3ImageUploader).removeFolder(any());
        verify(productRepository).deleteById(any());
    }

    private void equals(ProductResponse response, Product product){
        assertThat(response.getName()).isEqualTo(product.getName());
        assertThat(response.getDescription()).isEqualTo(product.getDescription());
        assertThat(response.getBrand()).isEqualTo(product.getBrand());
        assertThat(response.getCategory()).isEqualTo(product.getCategory().name());
        response.getProductImages().stream()
            .map(ProductImageResponse::getImageUrl)
            .collect(Collectors.toList())
            .forEach(imageUrl -> assertThat(product.getImageUrls().contains(imageUrl)).isTrue());
        assertThat(response.getPrice()).isEqualTo(product.getPrice());
        assertThat(response.getStock()).isEqualTo(product.getStock());
    }

    private void equals(SimpleProductResponse response, Product product){
        assertThat(response.getName()).isEqualTo(product.getName());
        assertThat(response.getDescription()).isEqualTo(product.getDescription());
        assertThat(response.getBrand()).isEqualTo(product.getBrand());
        assertThat(response.getCategory()).isEqualTo(product.getCategory().name());
        response.getImagesUrl().forEach(imageUrl -> assertThat(product.getImageUrls().contains(imageUrl)).isTrue());
        assertThat(response.getPrice()).isEqualTo(product.getPrice());
        assertThat(response.getStock()).isEqualTo(product.getStock());
    }

}
