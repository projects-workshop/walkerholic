package com.yunhalee.walkerholic.productImage.api;

import static com.yunhalee.walkerholic.product.api.ProductApiTest.FIRST_PRODUCT;
import static com.yunhalee.walkerholic.product.api.ProductApiTest.SECOND_PRODUCT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yunhalee.walkerholic.ApiTest;
import com.yunhalee.walkerholic.productImage.domain.ProductImage;
import com.yunhalee.walkerholic.productImage.dto.SimpleProductImageResponses;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

public class ProductImageApiTest extends ApiTest {

    public static final ProductImage FIRST_PRODUCT_IMAGE = ProductImage.builder()
        .id(1)
        .name("firstProductImage")
        .filePath("firstProductImage/image.png")
        .product(FIRST_PRODUCT).build();
    public static final ProductImage SECOND_PRODUCT_IMAGE = ProductImage.builder()
        .id(2)
        .name("secondProductImage")
        .filePath("secondProductImage/image.png")
        .product(FIRST_PRODUCT).build();
    public static final ProductImage THIRD_PRODUCT_IMAGE = ProductImage.builder()
        .id(3)
        .name("thirdProductImage")
        .filePath("thirdProductImage/image.png")
        .product(SECOND_PRODUCT).build();


    @Test
    void create_product_image() throws Exception {
        when(productImageService.createImages(any(), any())).thenReturn(SimpleProductImageResponses.of(Arrays.asList(FIRST_PRODUCT_IMAGE.getFilePath(), SECOND_PRODUCT_IMAGE.getFilePath())));
        this.mockMvc.perform(multipart("/api/products/1/product-images").file(MULTIPART_FILE).file(MULTIPART_FILE)
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .accept(MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("product-image-create", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), simpleProductImageResponseFields()));
    }

    @Test
    void delete_product_image() throws Exception {
        this.mockMvc.perform(delete("/api/products/1/product-images")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .param("deletedImages", FIRST_PRODUCT_IMAGE.getFilePath())
            .param("deletedImages", SECOND_PRODUCT_IMAGE.getFilePath())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(document("product-image-delete"));
    }

    public static ResponseFieldsSnippet simpleProductImageResponseFields() {
        return responseFields(
            fieldWithPath("imagesUrl").description("product's imageUrls"),
            fieldWithPath("imagesUrl.[]").description("product imageUrl"));
    }

}
