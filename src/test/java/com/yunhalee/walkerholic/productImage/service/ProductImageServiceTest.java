package com.yunhalee.walkerholic.productImage.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.yunhalee.walkerholic.MockBeans;
import com.yunhalee.walkerholic.product.domain.Product;
import com.yunhalee.walkerholic.product.domain.ProductTest;
import com.yunhalee.walkerholic.productImage.domain.ProductImage;
import com.yunhalee.walkerholic.productImage.dto.SimpleProductImageResponses;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

class ProductImageServiceTest extends MockBeans {

    private static final String NAME = "testImage";
    private static final String FILE_PATH = "https://walkerholic-test-you.s3.ap-northeast10.amazonaws.com/productUploads/testImageUrl";
    private static final MultipartFile MULTIPART_FILE = new MockMultipartFile(
        "uploaded-file",
        "sampleFile.txt",
        "text/plain",
        "This is the file content".getBytes());

    @InjectMocks
    private ProductImageService productImageService = new ProductImageService(
        productImageRepository,
        productRepository,
        s3ImageUploader);

    private ProductImage productImage;
    private Product product;

    @BeforeEach
    public void setUp() {
        product = ProductTest.FIRST_PRODUCT;
        productImage = ProductImage.builder()
            .id(1)
            .name(NAME)
            .filePath(FILE_PATH)
            .product(product).build();
    }


    @Test
    public void createImages() {
        //given

        //when
        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        when(s3ImageUploader.uploadImage(any(), any())).thenReturn(FILE_PATH);
        when(s3ImageUploader.getFileName(any(), any())).thenReturn(NAME);
        when(productImageRepository.save(any())).thenReturn(productImage);

        SimpleProductImageResponses response = productImageService.createImages(1, Arrays.asList(MULTIPART_FILE));

        //then
        assertThat(response.getImagesUrl().size()).isEqualTo(1);
        assertThat(response.getImagesUrl().get(0)).isEqualTo(FILE_PATH);
    }

    @Test
    public void deleteImages() {
        //given
        product.addProductImage(productImage);

        //when
        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        when(productImageRepository.deleteByFilePath(any())).thenReturn(1);
        productImageService.deleteImages(1, Arrays.asList(FILE_PATH));

        //then
        verify(s3ImageUploader).deleteByFilePath(any());
        assertThat(product.getProductImages().size()).isEqualTo(0);
    }

    @Test
    public void deleteProduct() {
        //when
        productImageService.deleteProduct(1);

        //then
        verify(s3ImageUploader).removeFolder(any());
    }


}