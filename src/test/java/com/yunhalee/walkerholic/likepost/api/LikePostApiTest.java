package com.yunhalee.walkerholic.likepost.api;

import static com.yunhalee.walkerholic.post.api.PostApiTest.FIRST_POST;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yunhalee.walkerholic.ApiTest;
import com.yunhalee.walkerholic.likepost.domain.LikePost;
import com.yunhalee.walkerholic.likepost.dto.LikePostRequest;
import com.yunhalee.walkerholic.likepost.dto.LikePostResponse;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

public class LikePostApiTest extends ApiTest {

    public static final LikePost FIRST_LIKE_POST = new LikePost(1, SECOND_USER, FIRST_POST);
    public static final LikePost SECOND_LIKE_POST = new LikePost(2, THIRD_USER, FIRST_POST);
    private LikePostRequest REQUEST = new LikePostRequest(FIRST_POST.getId(), SECOND_USER.getId());

    @Test
    void create_like_post() throws Exception {
        when(likePostService.likePost(any())).thenReturn(LikePostResponse.of(FIRST_LIKE_POST));
        this.mockMvc.perform(post("/api/like-posts")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .contentType(MediaTypes.HAL_JSON)
            .characterEncoding("utf-8")
            .content(request(REQUEST))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andDo(document("like-post-create", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), likePostResponseFields()));
    }


    @Test
    void delete_like_post() throws Exception {
        this.mockMvc.perform(delete("/api/like-posts/1")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(document("like-post-delete"));
    }


    public static ResponseFieldsSnippet likePostResponseFields() {
        return responseFields(
            fieldWithPath("id").description("likePost id"),
            fieldWithPath("userId").description("the id of user who liked post"),
            fieldWithPath("fullname").description("the fullName of user who liked post"),
            fieldWithPath("imageUrl").description("the imageUrl of user who liked post"));
    }

}
