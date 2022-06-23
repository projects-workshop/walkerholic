package com.yunhalee.walkerholic.post.api;

import static com.yunhalee.walkerholic.postImage.api.PostImageApiTest.FIRST_POST_IMAGE;
import static com.yunhalee.walkerholic.postImage.api.PostImageApiTest.SECOND_POST_IMAGE;
import static com.yunhalee.walkerholic.user.domain.UserTest.FIRST_USER;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yunhalee.walkerholic.ApiTest;
import com.yunhalee.walkerholic.likepost.domain.LikePost;
import com.yunhalee.walkerholic.likepost.dto.LikePostResponse;
import com.yunhalee.walkerholic.post.domain.Post;
import com.yunhalee.walkerholic.post.dto.PostRequest;
import com.yunhalee.walkerholic.post.dto.PostResponse;
import com.yunhalee.walkerholic.post.dto.PostResponses;
import com.yunhalee.walkerholic.post.dto.SimplePostResponse;
import com.yunhalee.walkerholic.post.dto.SimplePostResponses;
import com.yunhalee.walkerholic.post.dto.UserPostResponse;
import com.yunhalee.walkerholic.postImage.domain.PostImage;
import com.yunhalee.walkerholic.postImage.dto.PostImageResponse;
import com.yunhalee.walkerholic.user.domain.UserTest;
import com.yunhalee.walkerholic.user.dto.SimpleUserResponse;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

public class PostApiTest extends ApiTest {

    private static final Post FIRST_POST = new Post(1, "firstPost", "This is first post.", UserTest.FIRST_USER);
    private static final Post SECOND_POST = new Post(2, "secondPost", "This is second post.", UserTest.SECOND_USER);
    private PostRequest REQUEST = new PostRequest(FIRST_POST.getTitle(), FIRST_POST.getContent(), FIRST_USER.getId());
    private final MockMultipartFile POST_REQUEST = new MockMultipartFile(
        "postRequest",
        "",
        "application/json",
        request(REQUEST).getBytes());

    private static final LikePost FIRST_LIKE_POST = new LikePost(1, SECOND_USER, FIRST_POST);
    private static final LikePost SECOND_LIKE_POST = new LikePost(2, THIRD_USER, FIRST_POST);

    @BeforeEach
    void setUp() {
        FIRST_POST.addPostImage(FIRST_POST_IMAGE);
        SECOND_POST.addPostImage(SECOND_POST_IMAGE);
    }

    @Test
    void create_post() throws Exception {
        when(postService.createPost(any(), any())).thenReturn(PostResponse.of(FIRST_POST,
            Arrays.asList(LikePostResponse.of(FIRST_LIKE_POST), LikePostResponse.of(SECOND_LIKE_POST)),
            Arrays.asList(PostImageResponse.of(FIRST_POST_IMAGE), PostImageResponse.of(SECOND_POST_IMAGE)),
            SimpleUserResponse.of(FIRST_USER)));
        this.mockMvc.perform(multipart("/api/posts").file(MULTIPART_FILE).file(POST_REQUEST)
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .accept(MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andDo(document("post-create", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), postResponseFields()));
    }

    @Test
    void get_post() throws Exception {
        when(postService.getPost(any())).thenReturn(PostResponse.of(FIRST_POST,
            Arrays.asList(LikePostResponse.of(FIRST_LIKE_POST), LikePostResponse.of(SECOND_LIKE_POST)),
            Arrays.asList(PostImageResponse.of(FIRST_POST_IMAGE), PostImageResponse.of(SECOND_POST_IMAGE)),
            SimpleUserResponse.of(FIRST_USER)));
        this.mockMvc.perform(get("/api/posts/1")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("post-get-by-id", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), postResponseFields()));
    }

    @Test
    void get_posts_by_user() throws Exception {
        when(postService.getUserPosts(any())).thenReturn(new UserPostResponse(
            Arrays.asList(SimplePostResponse.of(FIRST_POST), SimplePostResponse.of(SECOND_POST)),
            Arrays.asList(SimplePostResponse.of(FIRST_POST), SimplePostResponse.of(SECOND_POST))));
        this.mockMvc.perform(get("/api/users/1/posts")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("post-get-all-by-user", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), userPostResponseFields()));
    }

    @Test
    void get_posts_by_random() throws Exception {
        when(postService.getPostsByRandom(any(), any())).thenReturn(SimplePostResponses.of(
            Arrays.asList(SimplePostResponse.of(FIRST_POST), SimplePostResponse.of(SECOND_POST)), 2L, 1));
        this.mockMvc.perform(get("/api/users/1/posts/discover")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .param("page", "1")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("post-get-all-by-random", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), simplePostResponsesFields()));
    }

    @Test
    void get_posts_by_followings() throws Exception {
        when(postService.getPostsByFollowings(any(), any())).thenReturn(PostResponses.of(
            Arrays.asList(
                PostResponse.of(FIRST_POST,
                    Arrays.asList(LikePostResponse.of(FIRST_LIKE_POST), LikePostResponse.of(SECOND_LIKE_POST)),
                    Arrays.asList(PostImageResponse.of(FIRST_POST_IMAGE), PostImageResponse.of(SECOND_POST_IMAGE)),
                    SimpleUserResponse.of(FIRST_USER)),
                PostResponse.of(FIRST_POST,
                    Arrays.asList(LikePostResponse.of(FIRST_LIKE_POST), LikePostResponse.of(SECOND_LIKE_POST)),
                    Arrays.asList(PostImageResponse.of(FIRST_POST_IMAGE), PostImageResponse.of(SECOND_POST_IMAGE)),
                    SimpleUserResponse.of(FIRST_USER))),
            2L, 1));
        this.mockMvc.perform(get("/api/users/1/posts/follow")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .param("page", "1")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("post-get-all-by-followings", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), postResponsesFields()));
    }


    @Test
    void get_home_posts() throws Exception {
        when(postService.getHomePosts(any(), any())).thenReturn(SimplePostResponses.of(
            Arrays.asList(SimplePostResponse.of(FIRST_POST), SimplePostResponse.of(SECOND_POST)), 2L, 1));
        this.mockMvc.perform(get("/api/posts")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .param("page", "1")
            .param("sort", "id")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("post-get-all-home-posts", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), simplePostResponsesFields()));
    }

    @Test
    void get_posts_by_keyword() throws Exception {
        when(postService.getSearchPosts(any(), any(), any())).thenReturn(SimplePostResponses.of(
            Arrays.asList(SimplePostResponse.of(FIRST_POST), SimplePostResponse.of(SECOND_POST)), 2L, 1));
        this.mockMvc.perform(get("/api/posts")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .param("page", "1")
            .param("sort", "id")
            .param("keyword", "post")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("post-get-all-by-keyword", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), simplePostResponsesFields()));
    }

    @Test
    void update_user_activity() throws Exception {
        when(postService.updatePost(any(), any())).thenReturn(PostResponse.of(FIRST_POST,
            Arrays.asList(LikePostResponse.of(FIRST_LIKE_POST), LikePostResponse.of(SECOND_LIKE_POST)),
            Arrays.asList(PostImageResponse.of(FIRST_POST_IMAGE), PostImageResponse.of(SECOND_POST_IMAGE)),
            SimpleUserResponse.of(FIRST_USER)));
        this.mockMvc.perform(put("/api/posts/1")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .contentType(MediaTypes.HAL_JSON)
            .characterEncoding("utf-8")
            .content(request(REQUEST))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("post-update", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), postResponseFields()));
    }

    @Test
    void delete_user_activity() throws Exception {
        this.mockMvc.perform(delete("/api/posts/1")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(document("post-delete"));
    }




    public static ResponseFieldsSnippet postResponseFields() {
        return responseFields(
            fieldWithPath("id").description("post id"),
            fieldWithPath("title").description("post title"),
            fieldWithPath("content").description("post content"),
            fieldWithPath("user").description("post writer's information"),
            fieldWithPath("user.id").description("writer id"),
            fieldWithPath("user.fullname").description("writer fullname"),
            fieldWithPath("user.email").description("writer email"),
            fieldWithPath("user.imageUrl").description("writer imageUrl"),
            fieldWithPath("user.description").description("writer description"),
            fieldWithPath("postLikes").description("users who like this post"),
            fieldWithPath("postLikes.[].id").description("likePost id"),
            fieldWithPath("postLikes.[].userId").description("user id"),
            fieldWithPath("postLikes.[].fullname").description("user fullName"),
            fieldWithPath("postLikes.[].imageUrl").description("user imageUrl"),
            fieldWithPath("postImages").description("post's images"),
            fieldWithPath("postImages.[].id").description("postImage id"),
            fieldWithPath("postImages.[].imageUrl").description("post imageUrl"),
            fieldWithPath("createdAt").description("the time when post created"));
    }



    public static ResponseFieldsSnippet userPostResponseFields() {
        return responseFields(
            fieldWithPath("posts").description("the information of user's posts"),
            fieldWithPath("posts.[].id").description("post id"),
            fieldWithPath("posts.[].title").description("post title"),
            fieldWithPath("posts.[].imageUrl").description("post first image url"),
            fieldWithPath("posts.[].userImageUrl").description("user imageUrl"),
            fieldWithPath("posts.[].userName").description("user fullName"),
            fieldWithPath("posts.[].userId").description("user id"),
            fieldWithPath("likePosts").description("the information of the posts which user likes"),
            fieldWithPath("likePosts.[].id").description("like post id"),
            fieldWithPath("likePosts.[].title").description("like post title"),
            fieldWithPath("likePosts.[].imageUrl").description("like post first image url"),
            fieldWithPath("likePosts.[].userImageUrl").description("writer imageUrl"),
            fieldWithPath("likePosts.[].userName").description("writer fullName"),
            fieldWithPath("likePosts.[].userId").description("writer id"));
    }

    public static ResponseFieldsSnippet simplePostResponsesFields() {
        return responseFields(
            fieldWithPath("posts").description("the information of user's posts"),
            fieldWithPath("posts.[].id").description("post id"),
            fieldWithPath("posts.[].title").description("post title"),
            fieldWithPath("posts.[].imageUrl").description("post first image url"),
            fieldWithPath("posts.[].userImageUrl").description("user imageUrl"),
            fieldWithPath("posts.[].userName").description("user fullName"),
            fieldWithPath("posts.[].userId").description("user id"),
            fieldWithPath("totalElement").description("the number of total posts"),
            fieldWithPath("totalPage").description("the number of total page"));
    }

    public static ResponseFieldsSnippet postResponsesFields() {
        return responseFields(
            fieldWithPath("posts").description("the posts of following's and mine"),
            fieldWithPath("posts.[].id").description("post id"),
            fieldWithPath("posts.[].title").description("post title"),
            fieldWithPath("posts.[].content").description("post content"),
            fieldWithPath("posts.[].user").description("post writer's information"),
            fieldWithPath("posts.[].user.id").description("writer id"),
            fieldWithPath("posts.[].user.fullname").description("writer fullname"),
            fieldWithPath("posts.[].user.email").description("writer email"),
            fieldWithPath("posts.[].user.imageUrl").description("writer imageUrl"),
            fieldWithPath("posts.[].user.description").description("writer description"),
            fieldWithPath("posts.[].postLikes").description("users who like this post"),
            fieldWithPath("posts.[].postLikes.[].id").description("likePost id"),
            fieldWithPath("posts.[].postLikes.[].userId").description("user id"),
            fieldWithPath("posts.[].postLikes.[].fullname").description("user fullName"),
            fieldWithPath("posts.[].postLikes.[].imageUrl").description("user imageUrl"),
            fieldWithPath("posts.[].postImages").description("post's images"),
            fieldWithPath("posts.[].postImages.[].id").description("postImage id"),
            fieldWithPath("posts.[].postImages.[].imageUrl").description("post imageUrl"),
            fieldWithPath("posts.[].createdAt").description("the time when post created"),
            fieldWithPath("totalElement").description("the number of total posts"),
            fieldWithPath("totalPage").description("the number of total page"));
    }

}
