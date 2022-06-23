package com.yunhalee.walkerholic.product.api;

import static com.yunhalee.walkerholic.productImage.api.ProductImageApiTest.FIRST_PRODUCT_IMAGE;
import static com.yunhalee.walkerholic.productImage.api.ProductImageApiTest.SECOND_PRODUCT_IMAGE;
import static com.yunhalee.walkerholic.productImage.api.ProductImageApiTest.THIRD_PRODUCT_IMAGE;
import static com.yunhalee.walkerholic.review.api.ReviewApiTest.FIRST_REVIEW;
import static com.yunhalee.walkerholic.review.api.ReviewApiTest.SECOND_REVIEW;
import static com.yunhalee.walkerholic.user.domain.UserTest.FIRST_USER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yunhalee.walkerholic.ApiTest;
import com.yunhalee.walkerholic.product.domain.Category;
import com.yunhalee.walkerholic.product.domain.Product;
import com.yunhalee.walkerholic.product.dto.ProductRequest;
import com.yunhalee.walkerholic.product.dto.ProductResponse;
import com.yunhalee.walkerholic.product.dto.ProductResponses;
import com.yunhalee.walkerholic.product.dto.SimpleProductResponse;
import com.yunhalee.walkerholic.productImage.dto.ProductImageResponse;
import com.yunhalee.walkerholic.review.dto.ReviewResponse;
import com.yunhalee.walkerholic.user.dto.SellerUserResponse;
import com.yunhalee.walkerholic.user.dto.SimpleUserResponse;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

public class ProductApiTest extends ApiTest {

    public static final Product FIRST_PRODUCT = new Product(1, "firstProduct", "This is first product.", "testBrand", Category.TUMBLER, 100, 15.0f, FIRST_USER);
    public static final Product SECOND_PRODUCT = new Product(1, "secondProduct", "This is second product.", "testBrand", Category.TUMBLER, 100, 15.0f, FIRST_USER);
    private ProductRequest REQUEST = new ProductRequest(FIRST_PRODUCT.getName(),
        FIRST_PRODUCT.getDescription(),
        FIRST_PRODUCT.getBrand(),
        FIRST_PRODUCT.getCategory().name(),
        FIRST_PRODUCT.getStock(),
        FIRST_PRODUCT.getPrice().floatValue(),
        FIRST_PRODUCT.getUser().getId());
    private final MockMultipartFile PRODUCT_REQUEST = new MockMultipartFile(
    "productRequest",
    "",
    "application/json",
    request(REQUEST).getBytes());

    @BeforeEach
    void setUp() {
        FIRST_PRODUCT.addProductImage(FIRST_PRODUCT_IMAGE);
        FIRST_PRODUCT.addProductImage(SECOND_PRODUCT_IMAGE);
        SECOND_PRODUCT.addProductImage(THIRD_PRODUCT_IMAGE);
        FIRST_PRODUCT.addReview(FIRST_REVIEW.getRating());
        FIRST_PRODUCT.addReview(SECOND_REVIEW.getRating());
    }

    @Test
    void create_product() throws Exception {
        when(productService.createProduct(any(), any())).thenReturn(SimpleProductResponse.of(FIRST_PRODUCT));
        this.mockMvc.perform(multipart("/api/products").file(MULTIPART_FILE).file(PRODUCT_REQUEST)
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .accept(MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andDo(document("product-create", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), simpleProductResponseFields()));
    }


    @Test
    void get_product() throws Exception {
        when(productService.getProduct(any())).thenReturn(ProductResponse.of(FIRST_PRODUCT,
            Arrays.asList(new ProductImageResponse(FIRST_PRODUCT_IMAGE), new ProductImageResponse(SECOND_PRODUCT_IMAGE)),
            SimpleUserResponse.of(FIRST_USER),
            Arrays.asList(new ReviewResponse(FIRST_REVIEW), new ReviewResponse(SECOND_REVIEW))));
        this.mockMvc.perform(get("/api/products/1")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("product-get-by-id", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), productResponseFields()));
    }

    @Test
    void get_products() throws Exception {
        when(productService.getProducts(any(), any(), any(), any())).thenReturn(ProductResponses.of(
            Arrays.asList(SimpleProductResponse.of(FIRST_PRODUCT), SimpleProductResponse.of(SECOND_PRODUCT)),
            SellerUserResponse.of(FIRST_USER),
            2L,
            1));
        this.mockMvc.perform(get("/api/products")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .param("page", "1")
            .param("sort", "id")
            .param("category", "tumbler")
            .param("keyword", "test")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("product-get-all", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), productResponsesFields()));
    }

    @Test
    void get_products_by_seller() throws Exception {
        when(productService.getProductsBySeller(any(), any(), any(), any(), any())).thenReturn(ProductResponses.of(
            Arrays.asList(SimpleProductResponse.of(FIRST_PRODUCT), SimpleProductResponse.of(SECOND_PRODUCT)),
            SellerUserResponse.of(FIRST_USER),
            2L,
            1));
        this.mockMvc.perform(get("/api/users/1/products")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .param("page", "1")
            .param("sort", "id")
            .param("category", "tumbler")
            .param("keyword", "test")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("product-get-all-by-seller", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), productResponsesFields()));
    }


    @Test
    void update_product() throws Exception {
        when(productService.updateProduct(any(), any())).thenReturn(SimpleProductResponse.of(FIRST_PRODUCT));
        this.mockMvc.perform(put("/api/products/1")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .contentType(MediaTypes.HAL_JSON)
            .characterEncoding("utf-8")
            .content(request(REQUEST))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("product-update", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), simpleProductResponseFields()));
    }

    @Test
    void delete_product() throws Exception {
        this.mockMvc.perform(delete("/api/products/1")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(document("product-delete"));
    }




    public static ResponseFieldsSnippet simpleProductResponseFields() {
        return responseFields(
            fieldWithPath("id").description("product id"),
            fieldWithPath("name").description("product name"),
            fieldWithPath("brand").description("product brand"),
            fieldWithPath("category").description("product category"),
            fieldWithPath("stock").description("product stock"),
            fieldWithPath("price").description("product price"),
            fieldWithPath("average").description("product rating average"),
            fieldWithPath("imagesUrl").description("product's images"),
            fieldWithPath("imagesUrl.[]").description("product imageUrl"),
            fieldWithPath("description").description("product description"));
    }


    public static ResponseFieldsSnippet productResponseFields() {
        return responseFields(
            fieldWithPath("id").description("product id"),
            fieldWithPath("name").description("product name"),
            fieldWithPath("brand").description("product brand"),
            fieldWithPath("category").description("product category"),
            fieldWithPath("stock").description("product stock"),
            fieldWithPath("price").description("product price"),
            fieldWithPath("average").description("product rating average"),
            fieldWithPath("description").description("product description"),
            fieldWithPath("productImages").description("product's images"),
            fieldWithPath("productImages.[].name").description("product image name"),
            fieldWithPath("productImages.[].imageUrl").description("product imageUrl"),
            fieldWithPath("user").description("product seller"),
            fieldWithPath("user.id").description("seller id"),
            fieldWithPath("user.fullname").description("seller fullName"),
            fieldWithPath("user.email").description("seller email"),
            fieldWithPath("user.imageUrl").description("seller imageUrl"),
            fieldWithPath("user.description").description("seller description"),
            fieldWithPath("productReviews").description("product's reviews"),
            fieldWithPath("productReviews.[].id").description("review id"),
            fieldWithPath("productReviews.[].rating").description("review rating"),
            fieldWithPath("productReviews.[].comment").description("review comment"),
            fieldWithPath("productReviews.[].userId").description("review writer id"),
            fieldWithPath("productReviews.[].userFullname").description("review writer name"),
            fieldWithPath("productReviews.[].userImageUrl").description("review writer imageUrl"),
            fieldWithPath("productReviews.[].productId").description("product id"),
            fieldWithPath("productReviews.[].createdAt").description("the time when review has created"),
            fieldWithPath("productReviews.[].updatedAt").description("the time when review has recently updated"));
    }



    public static ResponseFieldsSnippet productResponsesFields() {
        return responseFields(
            fieldWithPath("products").description("found products"),
            fieldWithPath("products.[].id").description("product id"),
            fieldWithPath("products.[].name").description("product name"),
            fieldWithPath("products.[].brand").description("product brand"),
            fieldWithPath("products.[].category").description("product category"),
            fieldWithPath("products.[].stock").description("product stock"),
            fieldWithPath("products.[].price").description("product price"),
            fieldWithPath("products.[].average").description("product rating average"),
            fieldWithPath("products.[].imagesUrl").description("product's images"),
            fieldWithPath("products.[].imagesUrl.[]").description("product imageUrl"),
            fieldWithPath("products.[].description").description("product description"),
            fieldWithPath("seller").description("product seller"),
            fieldWithPath("seller.id").description("seller id"),
            fieldWithPath("seller.fullname").description("seller fullname"),
            fieldWithPath("seller.email").description("seller email"),
            fieldWithPath("seller.imageUrl").description("seller imageUrl"),
            fieldWithPath("seller.phoneNumber").description("seller phoneNumber"),
            fieldWithPath("seller.level").description("seller activity level"),
            fieldWithPath("seller.description").description("seller description"),
            fieldWithPath("totalElement").description("the number of products"),
            fieldWithPath("totalPage").description("the number of totalPage"));
    }


}
