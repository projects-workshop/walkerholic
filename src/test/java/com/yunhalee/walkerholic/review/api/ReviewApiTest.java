package com.yunhalee.walkerholic.review.api;

import static com.yunhalee.walkerholic.product.api.ProductApiTest.FIRST_PRODUCT;
import static com.yunhalee.walkerholic.user.domain.UserTest.SECOND_USER;
import static com.yunhalee.walkerholic.user.domain.UserTest.THIRD_USER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yunhalee.walkerholic.ApiTest;
import com.yunhalee.walkerholic.review.domain.Review;
import com.yunhalee.walkerholic.review.dto.ReviewRequest;
import com.yunhalee.walkerholic.review.dto.ReviewResponse;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

public class ReviewApiTest extends ApiTest {

    public static final Review FIRST_REVIEW = Review.builder()
        .id(1)
        .rating(4)
        .comment("firstTestReview")
        .user(SECOND_USER)
        .product(FIRST_PRODUCT).build();

    public static final Review SECOND_REVIEW = Review.builder()
        .id(2)
        .rating(3)
        .comment("secondTestReview")
        .user(THIRD_USER)
        .product(FIRST_PRODUCT).build();

    private static final ReviewRequest REQUEST = new ReviewRequest(FIRST_REVIEW.getRating(),
        FIRST_REVIEW.getComment(),
        FIRST_REVIEW.productId(),
        FIRST_REVIEW.userId());

    @Test
    void create_review() throws Exception {
        when(reviewService.create(any())).thenReturn(new ReviewResponse(FIRST_REVIEW));
        this.mockMvc.perform(post("/api/reviews")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .contentType(MediaTypes.HAL_JSON)
            .characterEncoding("utf-8")
            .content(request(REQUEST))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("review-create", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), reviewResponseFields()));
    }

    @Test
    void update_review() throws Exception {
        when(reviewService.update(any(), any())).thenReturn(new ReviewResponse(FIRST_REVIEW));
        this.mockMvc.perform(put("/api/reviews/1")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .contentType(MediaTypes.HAL_JSON)
            .characterEncoding("utf-8")
            .content(request(REQUEST))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("review-update", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), reviewResponseFields()));
    }

    @Test
    void delete_review() throws Exception {
        this.mockMvc.perform(delete("/api/reviews/1")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(document("review-delete"));
    }


    public static ResponseFieldsSnippet reviewResponseFields() {
        return responseFields(
            fieldWithPath("id").description("review id"),
            fieldWithPath("rating").description("review rating"),
            fieldWithPath("comment").description("review comment"),
            fieldWithPath("userId").description("reviewer id"),
            fieldWithPath("userFullname").description("reviewer name"),
            fieldWithPath("userImageUrl").description("reviewer imageUrl"),
            fieldWithPath("productId").description("product id"),
            fieldWithPath("createdAt").description("the time when review has created"),
            fieldWithPath("updatedAt").description("the time when review has recently updated"));
    }



}
