package com.yunhalee.walkerholic.product.service;

import com.yunhalee.walkerholic.product.dto.ProductCreateDTO;
import com.yunhalee.walkerholic.product.dto.ProductDTO;
import com.yunhalee.walkerholic.product.dto.ProductListDTO;
import com.yunhalee.walkerholic.product.domain.Product;
import com.yunhalee.walkerholic.product.domain.ProductRepository;
import com.yunhalee.walkerholic.product.service.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ProductServiceTests {

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    @Test
    public void createProduct() {
        //given
        String name = "testProduct";
        String description = "testDescription";
        String brand = "testBrand";
        String category = "TUMBLER";
        Integer stock = 3;
        Float price = 2.3f;
        Integer userId = 1;
        ProductCreateDTO productCreateDTO = new ProductCreateDTO(name, description, brand, category,
            stock, price, userId);

        List<MultipartFile> multipartFileList = new ArrayList<>();
        MultipartFile multipartFile = new MockMultipartFile("uploaded-file",
            "sampleFile.txt",
            "text/plain",
            "This is the file content".getBytes());
        multipartFileList.add(multipartFile);

        //when
        ProductListDTO productListDTO = productService
            .saveProduct(productCreateDTO, multipartFileList, null);

        //then
        assertNotNull(productListDTO.getId());
        Product product = productRepository.findById(productListDTO.getId()).get();
        assertEquals(product.getDescription(), description);
        assertEquals(product.getBrand(), brand);
        assertEquals(product.getCategory().name(), category);
        assertEquals(product.getStock(), stock);
        assertEquals(product.getPrice(), price);
        assertEquals(product.getUser().getId(), userId);
        assertEquals(product.getProductImages().size(), 1);
        assertTrue(product.getProductImages().get(0).getName().contains("sampleFile.txt"));
    }

    @Test
    public void updateProduct() {
        //given
        Integer productId = 1;
        String name = "testUpdateProduct";
        String description = "testUpdateDescription";
        String brand = "testUpdateBrand";
        String category = "CLOTHES";
        Integer stock = 6;
        Float price = 2.7f;
        Integer userId = 1;

        ProductCreateDTO productCreateDTO = new ProductCreateDTO(productId, name, description,
            brand, category, stock, price, userId);

        List<MultipartFile> multipartFileList = new ArrayList<>();
        MultipartFile multipartFile = new MockMultipartFile("uploaded-file",
            "sampleUpdateFile.txt",
            "text/plain",
            "This is the file content".getBytes());
        multipartFileList.add(multipartFile);

        List<String> deletedImages = productRepository.findById(productId).get().getProductImages()
            .stream().map(productImage -> productImage.getFilePath()).collect(Collectors.toList());

        //when
        ProductListDTO productListDTO = productService
            .saveProduct(productCreateDTO, multipartFileList, deletedImages);

        //then
        assertEquals(productListDTO.getId(), productId);
        Product product = productRepository.findById(productListDTO.getId()).get();
        assertEquals(product.getDescription(), description);
        assertEquals(product.getBrand(), brand);
        assertEquals(product.getCategory().name(), category);
        assertEquals(product.getStock(), stock);
        assertEquals(product.getPrice(), price);
        assertEquals(product.getUser().getId(), userId);
        assertEquals(product.getProductImages().size(), 1);
        assertTrue(product.getProductImages().get(0).getName().contains("sampleUpdateFile.txt"));
    }

    @Test
    public void getProductById() {
        //given
        Integer productId = 1;

        //when
        ProductDTO productDTO = productService.getProduct(productId);

        //then
        assertEquals(productDTO.getId(), productId);
    }

    @Test
    public void getProductsBySortAndCategoryAndKeyword() {
        //given
        Integer page = 1;
        String sort = "lowest";
        String category = "TUMBLER";
        String keyword = "p";

        //when
        HashMap<String, Object> response = productService
            .getProducts(page, sort, category, keyword);
        List<ProductDTO> products = (List<ProductDTO>) response.get("products");

        //then
        for (ProductDTO product : products) {
            assertEquals(product.getCategory(), category);
            assertTrue(product.getName().contains(keyword));
        }
        Float priorPrice = products.get(0).getPrice();
        for (int i = 1; i < products.size(); i++) {
            assertThat(products.get(i).getPrice()).isGreaterThan(priorPrice);
            priorPrice = products.get(i).getPrice();
        }

    }

    @Test
    public void getProductsBySellerIdAndAndSortCategoryAndKeyword() {
        //given
        Integer sellerId = 1;
        Integer page = 1;
        String sort = "lowest";
        String category = "TUMBLER";
        String keyword = "p";

        //when
        HashMap<String, Object> response = productService
            .getProductsBySeller(sellerId, page, sort, category, keyword);
        List<ProductDTO> products = (List<ProductDTO>) response.get("products");

        //then
        for (ProductDTO product : products) {
            assertEquals(product.getCategory(), category);
            assertTrue(product.getName().contains(keyword));
            assertEquals(productRepository.findById(product.getId()).get().getUser().getId(),
                sellerId);
        }
        Float priorPrice = products.get(0).getPrice();
        for (int i = 1; i < products.size(); i++) {
            assertThat(products.get(i).getPrice()).isGreaterThan(priorPrice);
            priorPrice = products.get(i).getPrice();
        }
    }

    @Test
    public void getProductsBySellerIdAndSort() {
        //given
        Integer sellerId = 1;
        Integer page = 1;
        String sort = "lowest";

        //when
        HashMap<String, Object> response = productService
            .getProductListBySeller(page, sort, sellerId);
        List<ProductDTO> products = (List<ProductDTO>) response.get("products");

        //then
        for (ProductDTO product : products) {
            assertEquals(productRepository.findById(product.getId()).get().getUser().getId(),
                sellerId);
        }
        Float priorPrice = products.get(0).getPrice();
        for (int i = 1; i < products.size(); i++) {
            assertThat(products.get(i).getPrice()).isGreaterThan(priorPrice);
            priorPrice = products.get(i).getPrice();
        }
    }

    @Test
    public void getProducts() {
        //given
        Integer page = 1;
        String sort = "lowest";

        //when
        HashMap<String, Object> response = productService.getAllProductList(page, sort);
        List<ProductListDTO> products = (List<ProductListDTO>) response.get("products");

        //then
        assertThat(products.size()).isGreaterThan(0);
        Float priorPrice = products.get(0).getPrice();
        for (int i = 1; i < products.size(); i++) {
            assertThat(products.get(i).getPrice()).isGreaterThan(priorPrice);
            priorPrice = products.get(i).getPrice();
        }
    }

//    @Test
//    public void deleteProduct(){
//        //given
//
//        //when
//
//        //then
//    }

}
