package com.yunhalee.walkerholic.follow.api;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yunhalee.walkerholic.ApiTest;
import com.yunhalee.walkerholic.follow.dto.FollowResponse;
import com.yunhalee.walkerholic.follow.dto.FollowUserResponse;
import com.yunhalee.walkerholic.follow.dto.FollowsResponse;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

public class FollowApiTest extends ApiTest {

    @Test
    void follow() throws Exception {
        when(followService.follow(any(), any())).thenReturn(FollowResponse.of(SECOND_USER.getId(), FollowUserResponse.of(SECOND_USER)));
        this.mockMvc.perform(post("/api/follows?fromId=1&toId=2")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("follow-create", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), followResponseFields()));
    }

    @Test
    void get_followers() throws Exception {
        when(followService.getFollowers(any())).thenReturn(Arrays.asList(
            FollowResponse.of(THIRD_USER.getId(), FollowUserResponse.of(THIRD_USER)),
            FollowResponse.of(SECOND_USER.getId(), FollowUserResponse.of(SECOND_USER))));
        this.mockMvc.perform(get("/api/users/1/followers")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("follow-get-all-followers", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), followResponsesFields()));
    }

    @Test
    void get_followings() throws Exception {
        when(followService.getFollowings(any())).thenReturn(Arrays.asList(
            FollowResponse.of(SECOND_USER.getId(), FollowUserResponse.of(SECOND_USER)),
            FollowResponse.of(THIRD_USER.getId(), FollowUserResponse.of(THIRD_USER))));
        this.mockMvc.perform(get("/api/users/1/followings")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("follow-get-all-followings", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), followResponsesFields()));
    }

    @Test
    void get_follows() throws Exception {
        when(followService.getFollows(any())).thenReturn(FollowsResponse.of(
            Arrays.asList(FollowResponse.of(THIRD_USER.getId(), FollowUserResponse.of(THIRD_USER)),
                FollowResponse.of(SECOND_USER.getId(), FollowUserResponse.of(SECOND_USER))),
            Arrays.asList(FollowResponse.of(SECOND_USER.getId(), FollowUserResponse.of(SECOND_USER)),
                FollowResponse.of(THIRD_USER.getId(), FollowUserResponse.of(THIRD_USER)))));
        this.mockMvc.perform(get("/api/users/1/follows")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("follow-get-all", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), followsResponseFields()));
    }


    @Test
    void unfollow() throws Exception {
        this.mockMvc.perform(delete("/api/follows/1")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(document("follow-delete"));
    }


    public static ResponseFieldsSnippet followResponseFields() {
        return responseFields(
            fieldWithPath("id").description("follow id"),
            fieldWithPath("user").description("following user(toId user)'s information"),
            fieldWithPath("user.id").description("following's id"),
            fieldWithPath("user.fullname").description("following's fullName"),
            fieldWithPath("user.imageUrl").description("following's imageUrl"));
    }

    public static ResponseFieldsSnippet followResponsesFields() {
        return responseFields(
            fieldWithPath("[].id").description("follow id"),
            fieldWithPath("[].user").description("follower user's information"),
            fieldWithPath("[].user.id").description("follower's id"),
            fieldWithPath("[].user.fullname").description("follower's fullName"),
            fieldWithPath("[].user.imageUrl").description("follower's imageUrl"));
    }


    public static ResponseFieldsSnippet followsResponseFields() {
        return responseFields(
            fieldWithPath("followers").description("the information of followers"),
            fieldWithPath("followers.[].id").description("follow id"),
            fieldWithPath("followers.[].user").description("follower user's information"),
            fieldWithPath("followers.[].user.id").description("follower's id"),
            fieldWithPath("followers.[].user.fullname").description("follower's fullName"),
            fieldWithPath("followers.[].user.imageUrl").description("follower's imageUrl"),
            fieldWithPath("followings").description("the information of followings"),
            fieldWithPath("followings.[].id").description("follow id"),
            fieldWithPath("followings.[].user").description("following user's information"),
            fieldWithPath("followings.[].user.id").description("following's id"),
            fieldWithPath("followings.[].user.fullname").description("following's fullName"),
            fieldWithPath("followings.[].user.imageUrl").description("following's imageUrl"));
    }



}
