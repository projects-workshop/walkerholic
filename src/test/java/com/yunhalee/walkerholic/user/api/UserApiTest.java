package com.yunhalee.walkerholic.user.api;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yunhalee.walkerholic.ApiTest;
import com.yunhalee.walkerholic.security.jwt.domain.JwtUserDetails;
import com.yunhalee.walkerholic.security.jwt.dto.JwtRequest;
import com.yunhalee.walkerholic.user.domain.Role;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.user.dto.UserRequest;
import com.yunhalee.walkerholic.user.dto.UserResponse;
import com.yunhalee.walkerholic.user.dto.UserResponses;
import com.yunhalee.walkerholic.user.dto.UserSearchResponse;
import com.yunhalee.walkerholic.user.dto.UserSearchResponses;
import com.yunhalee.walkerholic.user.dto.UserTokenResponse;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

public class UserApiTest extends ApiTest {

    private static final String TOKEN = "token";
    private static final User SELLER = User.builder()
        .id(1)
        .firstname("test2FirstName")
        .lastname("Test2LastName")
        .email("test2@example.com")
        .password("12345678")
        .imageUrl("test/image.png")
        .phoneNumber("01000000000")
        .description("This is testSeller")
        .role(Role.SELLER).build();

    private static final UserRequest REQUEST = new UserRequest(FIRST_USER.getFirstname(),
        FIRST_USER.getLastname(),
        FIRST_USER.getEmail(),
        FIRST_USER.getPassword(),
        FIRST_USER.getImageUrl(),
        FIRST_USER.getPhoneNumber(),
        FIRST_USER.getDescription(),
        FIRST_USER.isSeller());

    @Test
    void save_image() throws Exception {
        when(userService.uploadImage(any())).thenReturn(FIRST_USER.getImageUrl());
        this.mockMvc.perform(multipart("/api/users/images").file(MULTIPART_FILE)
            .accept(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isOk())
            .andDo(document("user-save-image", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())));
    }

    @Test
    void create_user() throws Exception {
        when(userService.create(any())).thenReturn(UserTokenResponse.of(UserResponse.of(FIRST_USER), TOKEN));
        this.mockMvc.perform(post("/api/users")
            .contentType(MediaTypes.HAL_JSON)
            .characterEncoding("utf-8")
            .content(request(REQUEST))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("user-create", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), userTokenResponseFields()));
    }


    @Test
    void login_with_email_and_password() throws Exception {
        when(jwtUserDetailsService.loadUserByUsername(any())).thenReturn(new JwtUserDetails(FIRST_USER));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(jwtUserDetailsService.signIn(any())).thenReturn(UserTokenResponse.of(UserResponse.of(FIRST_USER), TOKEN));
        JwtRequest request = new JwtRequest(FIRST_USER.getEmail(), FIRST_USER.getPassword());
        this.mockMvc.perform(post("/api/sign-in")
            .contentType(MediaTypes.HAL_JSON)
            .characterEncoding("utf-8")
            .content(request(request))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("user-login-with-email-and-password",preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), userTokenResponseFields()));
    }

    @Test
    void login_with_token() throws Exception {
        when(jwtUserDetailsService.signInWithToken(any())).thenReturn(UserTokenResponse.of(UserResponse.of(FIRST_USER), TOKEN));
        this.mockMvc.perform(post("/api/sign-in")
            .param("token", TOKEN)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("user-login-with-token", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), userTokenResponseFields()));
    }

    @Test
    void get_user() throws Exception {
        when(userService.getUser(any())).thenReturn(UserResponse.of(FIRST_USER));
        this.mockMvc.perform(get("/api/users/1")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("user-get-by-id", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), userResponseFields()));
    }

    @Test
    void get_users() throws Exception {
        when(userService.getUsers(any(), any())).thenReturn(UserResponses.of(Arrays.asList(UserResponse.of(FIRST_USER), UserResponse.of(SELLER)), 2L, 1));
        this.mockMvc.perform(get("/api/users")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .param("page", "1")
            .param("sort", "id")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("user-get-all", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), userResponsesFields()));
    }

    @Test
    void get_users_by_keyword() throws Exception {
        when(userService.searchUser(any())).thenReturn(UserSearchResponses.of(Arrays.asList(UserSearchResponse.of(FIRST_USER), UserSearchResponse.of(SELLER))));
        this.mockMvc.perform(get("/api/users")
            .param("keyword", "test")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("user-get-all-by-keyword", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), userSearchResponsesFields()));
    }

    @Test
    void update_user() throws Exception {
        when(userService.update(any(), any())).thenReturn(UserResponse.of(FIRST_USER));
        this.mockMvc.perform(put("/api/users/1")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .contentType(MediaTypes.HAL_JSON)
            .characterEncoding("utf-8")
            .content(request(REQUEST))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("user-update", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), userResponseFields()));
    }

    @Test
    void delete_user() throws Exception {
        this.mockMvc.perform(delete("/api/users/1")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(document("user-delete"));
    }


    @Test
    void send_forgot_password_notification() throws Exception {
        this.mockMvc.perform(post("/api/users")
            .param("email", FIRST_USER.getEmail())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(document("user-send-forget-password"));
    }


    public static ResponseFieldsSnippet userResponseFields() {
        return responseFields(
            fieldWithPath("id").description("user id"),
            fieldWithPath("firstname").description("user firstname"),
            fieldWithPath("lastname").description("user lastname"),
            fieldWithPath("email").description("user email"),
            fieldWithPath("role").description("user role"),
            fieldWithPath("imageUrl").description("user image url"),
            fieldWithPath("phoneNumber").description("user phoneNumber"),
            fieldWithPath("level").description("user activity level"),
            fieldWithPath("description").description("user description"),
            fieldWithPath("seller").description("whether user is seller or not"));
    }


    public static ResponseFieldsSnippet userTokenResponseFields() {
        return responseFields(
            fieldWithPath("user").description("created user's information"),
            fieldWithPath("user.id").description("user id"),
            fieldWithPath("user.firstname").description("user firstname"),
            fieldWithPath("user.lastname").description("user lastname"),
            fieldWithPath("user.email").description("user email"),
            fieldWithPath("user.role").description("user role"),
            fieldWithPath("user.imageUrl").description("user image url"),
            fieldWithPath("user.phoneNumber").description("user phoneNumber"),
            fieldWithPath("user.level").description("user activity level"),
            fieldWithPath("user.description").description("user description"),
            fieldWithPath("user.seller").description("whether user is seller or not"),
            fieldWithPath("token").description("user token"));
    }

    public static ResponseFieldsSnippet userResponsesFields() {
        return responseFields(
            fieldWithPath("users").description("found user's information"),
            fieldWithPath("users.[].id").description("user id"),
            fieldWithPath("users.[].firstname").description("user firstname"),
            fieldWithPath("users.[].lastname").description("user lastname"),
            fieldWithPath("users.[].email").description("user email"),
            fieldWithPath("users.[].role").description("user role"),
            fieldWithPath("users.[].imageUrl").description("user image url"),
            fieldWithPath("users.[].phoneNumber").description("user phoneNumber"),
            fieldWithPath("users.[].level").description("user activity level"),
            fieldWithPath("users.[].description").description("user description"),
            fieldWithPath("users.[].seller").description("whether user is seller or not"),
            fieldWithPath("totalElement").description("the number of total element"),
            fieldWithPath("totalPage").description("the number of total page"));
    }

    public static ResponseFieldsSnippet userSearchResponsesFields() {
        return responseFields(
            fieldWithPath("users").description("found user's information"),
            fieldWithPath("users.[].id").description("user id"),
            fieldWithPath("users.[].firstname").description("user firstname"),
            fieldWithPath("users.[].lastname").description("user lastname"),
            fieldWithPath("users.[].email").description("user email"),
            fieldWithPath("users.[].imageUrl").description("user image url"));
    }


}
