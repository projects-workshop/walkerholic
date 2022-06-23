package com.yunhalee.walkerholic.postImage.api;

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
import com.yunhalee.walkerholic.postImage.domain.PostImage;
import com.yunhalee.walkerholic.postImage.dto.PostImageResponse;
import com.yunhalee.walkerholic.postImage.dto.PostImageResponses;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

public class PostImageApiTest extends ApiTest {

    public static final PostImage FIRST_POST_IMAGE = PostImage.builder()
        .id(1)
        .name("firstPostImage")
        .filePath("testPostImage/image.png").build();
    public static final PostImage SECOND_POST_IMAGE = PostImage.builder()
        .id(2)
        .name("secondPostImage")
        .filePath("testPostImage/image.png").build();


    @Test
    void create_post_image() throws Exception {
        when(postImageService.createImages(any(), any())).thenReturn(PostImageResponses.of(
            Arrays.asList(PostImageResponse.of(FIRST_POST_IMAGE), PostImageResponse.of(SECOND_POST_IMAGE))));
        this.mockMvc.perform(multipart("/api/posts/1/post-images").file(MULTIPART_FILE).file(MULTIPART_FILE)
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .accept(MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("post-image-create", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), postImageResponsesFields()));
    }

    @Test
    void delete_post_image() throws Exception {
        this.mockMvc.perform(delete("/api/posts/1/post-images")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .param("deletedImages", FIRST_POST_IMAGE.getFilePath())
            .param("deletedImages", SECOND_POST_IMAGE.getFilePath())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(document("post-image-delete"));
    }


    public static ResponseFieldsSnippet postImageResponsesFields() {
        return responseFields(
            fieldWithPath("postImages").description("post's images"),
            fieldWithPath("postImages.[].id").description("postImage id"),
            fieldWithPath("postImages.[].imageUrl").description("post imageUrl"));
    }


}
