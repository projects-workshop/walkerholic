package com.yunhalee.walkerholic.product.service;

import com.yunhalee.walkerholic.MockBeans;
import com.yunhalee.walkerholic.product.domain.Category;
import com.yunhalee.walkerholic.product.domain.ProductImageTest;
import com.yunhalee.walkerholic.product.dto.ProductRequest;
import com.yunhalee.walkerholic.product.dto.SimpleProductResponse;
import com.yunhalee.walkerholic.product.domain.Product;
import com.yunhalee.walkerholic.user.domain.Role;
import com.yunhalee.walkerholic.user.domain.User;
import java.io.IOException;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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
        userService,
        productImageRepository,
        s3ImageUploader,
        "https://walkerholic-test-you.s3.ap-northeast10.amazonaws.com");

    private Product product;
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
        assertThat(response.getName()).isEqualTo(NAME);
        assertThat(response.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(response.getBrand()).isEqualTo(BRAND);
        assertThat(response.getCategory()).isEqualTo(CATEGORY);
        assertThat(response.getImagesUrl()).contains(ProductImageTest.PRODUCT_IMAGE.getFilePath());
        assertThat(response.getPrice()).isEqualTo(PRICE);
        assertThat(response.getStock()).isEqualTo(STOCK);
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

//
//    @Test
//    public void getProductById() {
//        //given
//        Integer productId = 1;
//
//        //when
//        ProductResponse productDTO = productService.getProduct(productId);
//
//        //then
//        assertEquals(productDTO.getId(), productId);
//    }
//
//    @Test
//    public void getProductsBySortAndCategoryAndKeyword() {
//        //given
//        Integer page = 1;
//        String sort = "lowest";
//        String category = "TUMBLER";
//        String keyword = "p";
//
//        //when
//        HashMap<String, Object> response = productService
//            .getProducts(page, sort, category, keyword);
//        List<ProductResponse> products = (List<ProductResponse>) response.get("products");
//
//        //then
//        for (ProductResponse product : products) {
//            assertEquals(product.getCategory(), category);
//            assertTrue(product.getName().contains(keyword));
//        }
//        Float priorPrice = products.get(0).getPrice();
//        for (int i = 1; i < products.size(); i++) {
//            assertThat(products.get(i).getPrice()).isGreaterThan(priorPrice);
//            priorPrice = products.get(i).getPrice();
//        }
//
//    }
//
//    @Test
//    public void getProductsBySellerIdAndAndSortCategoryAndKeyword() {
//        //given
//        Integer sellerId = 1;
//        Integer page = 1;
//        String sort = "lowest";
//        String category = "TUMBLER";
//        String keyword = "p";
//
//        //when
//        HashMap<String, Object> response = productService
//            .getProductsBySeller(sellerId, page, sort, category, keyword);
//        List<ProductResponse> products = (List<ProductResponse>) response.get("products");
//
//        //then
//        for (ProductResponse product : products) {
//            assertEquals(product.getCategory(), category);
//            assertTrue(product.getName().contains(keyword));
//            assertEquals(productRepository.findById(product.getId()).get().getUser().getId(),
//                sellerId);
//        }
//        Float priorPrice = products.get(0).getPrice();
//        for (int i = 1; i < products.size(); i++) {
//            assertThat(products.get(i).getPrice()).isGreaterThan(priorPrice);
//            priorPrice = products.get(i).getPrice();
//        }
//    }
//
//    @Test
//    public void getProductsBySellerIdAndSort() {
//        //given
//        Integer sellerId = 1;
//        Integer page = 1;
//        String sort = "lowest";
//
//        //when
//        HashMap<String, Object> response = productService
//            .getProductListBySeller(page, sort, sellerId);
//        List<ProductResponse> products = (List<ProductResponse>) response.get("products");
//
//        //then
//        for (ProductResponse product : products) {
//            assertEquals(productRepository.findById(product.getId()).get().getUser().getId(),
//                sellerId);
//        }
//        Float priorPrice = products.get(0).getPrice();
//        for (int i = 1; i < products.size(); i++) {
//            assertThat(products.get(i).getPrice()).isGreaterThan(priorPrice);
//            priorPrice = products.get(i).getPrice();
//        }
//    }
//
//    @Test
//    public void getProducts() {
//        //given
//        Integer page = 1;
//        String sort = "lowest";
//
//        //when
//        HashMap<String, Object> response = productService.getAllProductList(page, sort);
//        List<SimpleProductResponse> products = (List<SimpleProductResponse>) response.get("products");
//
//        //then
//        assertThat(products.size()).isGreaterThan(0);
//        Float priorPrice = products.get(0).getPrice();
//        for (int i = 1; i < products.size(); i++) {
//            assertThat(products.get(i).getPrice()).isGreaterThan(priorPrice);
//            priorPrice = products.get(i).getPrice();
//        }
//    }

//    @Test
//    public void deleteProduct(){
//        //given
//
//        //when
//
//        //then
//    }

}
