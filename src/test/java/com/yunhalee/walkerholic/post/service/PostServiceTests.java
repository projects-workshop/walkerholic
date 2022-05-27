package com.yunhalee.walkerholic.post.service;

import com.yunhalee.walkerholic.MockBeans;
import com.yunhalee.walkerholic.likepost.domain.LikePost;
import com.yunhalee.walkerholic.postImage.domain.PostImageTest;
import com.yunhalee.walkerholic.post.dto.PostRequest;
import com.yunhalee.walkerholic.post.dto.PostResponse;
import com.yunhalee.walkerholic.post.dto.PostResponses;
import com.yunhalee.walkerholic.post.dto.SimplePostResponses;
import com.yunhalee.walkerholic.post.dto.UserPostResponse;
import com.yunhalee.walkerholic.user.domain.UserTest;
import com.yunhalee.walkerholic.post.domain.Post;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.data.domain.PageImpl;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PostServiceTests extends MockBeans {

    private static final String TITLE = "testTitle";
    private static final String CONTENT = "testContent";
    private static final MultipartFile MULTIPART_FILE = new MockMultipartFile(
        "uploaded-file",
        "sampleFile.txt",
        "text/plain",
        "This is the file content".getBytes());

    @InjectMocks
    private PostService postService = new PostService(
        postRepository,
        userService,
        postImageService,
        followService);

    private Post post;

    @BeforeEach
    public void setUp(){
        post = new Post(1, TITLE, CONTENT, UserTest.USER);
        post.addPostImage(PostImageTest.POST_IMAGE);
    }

    @Test
    public void createPost() {
        //given
        PostRequest request = new PostRequest(TITLE, CONTENT, 1);

        //when
        when(userService.findUserById(anyInt())).thenReturn(UserTest.USER);
        when(postImageService.uploadImages(any(), any())).thenReturn(Arrays.asList(PostImageTest.POST_IMAGE));
        when(postRepository.save(any())).thenReturn(post);
        PostResponse response = postService.createPost(request, Arrays.asList(MULTIPART_FILE));

        //then
        assertThat(response.getId()).isEqualTo(post.getId());
        assertThat(response.getTitle()).isEqualTo(TITLE);
        assertThat(response.getContent()).isEqualTo(CONTENT);
        assertThat(response.getPostImages().get(0).getId()).isEqualTo(PostImageTest.POST_IMAGE.getId());
        assertThat(response.getPostImages().get(0).getImageUrl()).isEqualTo(PostImageTest.POST_IMAGE.getFilePath());
    }

    @Test
    public void updatePost() {
        //given
        String newContent = "updateTestPost";
        PostRequest request = new PostRequest(post.getTitle(), newContent, post.getUser().getId());

        //when
        when(postRepository.findByPostId(any())).thenReturn(post);
        PostResponse response = postService.updatePost(post.getId(), request);

        //then
        assertThat(response.getId()).isEqualTo(post.getId());
        assertThat(response.getContent()).isEqualTo(newContent);
    }

    @Test
    public void getPostById() {
        //given
        post.addLikePost(new LikePost(1, UserTest.SELLER, post));

        //when
        when(postRepository.existsById(anyInt())).thenReturn(true);
        when(postRepository.findByPostId(anyInt())).thenReturn(post);
        PostResponse response = postService.getPost(post.getId());

        //then
        assertThat(response.getId()).isEqualTo(post.getId());
        assertThat(response.getContent()).isEqualTo(CONTENT);
        assertThat(response.getPostImages().get(0).getId()).isEqualTo(PostImageTest.POST_IMAGE.getId());
        assertThat(response.getPostImages().get(0).getImageUrl()).isEqualTo(PostImageTest.POST_IMAGE.getFilePath());
        assertThat(response.getPostLikes().get(0).getId()).isEqualTo(1);
    }

    @Test
    public void getPostsByUserId() {
        //given
        Post likePost = new Post(2, TITLE, CONTENT, UserTest.SELLER);
        likePost.addLikePost(new LikePost(1, UserTest.USER, likePost));
        likePost.addPostImage(PostImageTest.POST_IMAGE);

        //when
        when(postRepository.existsById(anyInt())).thenReturn(true);
        when(postRepository.findByUserId(anyInt())).thenReturn(Arrays.asList(post));
        when(postRepository.findByLikePostUserId(anyInt())).thenReturn(Arrays.asList(likePost));
        UserPostResponse response = postService.getUserPosts(UserTest.USER.getId());

        //then
        assertThat(response.getPosts().get(0).getId()).isEqualTo(post.getId());
        assertThat(response.getPosts().get(0).getUserId()).isEqualTo(UserTest.USER.getId());
        assertThat(response.getLikePosts().get(0).getId()).isEqualTo(likePost.getId());
        assertThat(response.getLikePosts().get(0).getUserId()).isEqualTo(UserTest.SELLER.getId());
    }

    @Test
    public void getPostsByRandom() {
        //given
        Post likePost = new Post(2, TITLE, CONTENT, UserTest.SELLER);
        likePost.addPostImage(PostImageTest.POST_IMAGE);

        //when
        doReturn(new PageImpl<>(Arrays.asList(likePost ))).when(postRepository).findByRandom(any(), any());
        SimplePostResponses response = postService.getPostsByRandom(1, 1);

        //then
        assertThat(response.getPosts().size()).isEqualTo(1);
        assertThat(response.getPosts().get(0).getId()).isEqualTo(likePost.getId());
        assertThat(response.getTotalElement()).isEqualTo(1L);
        assertThat(response.getTotalPage()).isEqualTo(1);
    }

    @Test
    public void getHomePostsByLikePostSize() {
        //given
        Integer page = 1;
        String sort = "popular";
        Post likePost = new Post(2, TITLE, CONTENT, UserTest.SELLER);
        likePost.addLikePost(new LikePost(1, UserTest.USER, likePost));
        likePost.addPostImage(PostImageTest.POST_IMAGE);

        //when
        doReturn(new PageImpl<>(Arrays.asList(post, likePost))).when(postRepository).findByCreateAt(any(), any());
        doReturn(new PageImpl<>(Arrays.asList(likePost, post))).when(postRepository).findByLikePostSize(any(), any());
        SimplePostResponses response = postService.getHomePosts(page, sort);

        //then
        assertThat(response.getPosts().get(0).getId()).isEqualTo(likePost.getId());
    }

    @Test
    public void getHomePostsByCreatedAt() {
        //given
        Integer page = 1;
        String sort = "newest";
        Post likePost = new Post(2, TITLE, CONTENT, UserTest.SELLER);
        likePost.addLikePost(new LikePost(1, UserTest.USER, likePost));
        likePost.addPostImage(PostImageTest.POST_IMAGE);

        //when
        doReturn(new PageImpl<>(Arrays.asList(post, likePost))).when(postRepository).findByCreateAt(any(), any());
        doReturn(new PageImpl<>(Arrays.asList(likePost, post))).when(postRepository).findByLikePostSize(any(), any());
        SimplePostResponses response = postService.getHomePosts(page, sort);

        //then
        assertThat(response.getPosts().get(0).getId()).isEqualTo(post.getId());
    }

    @Test
    public void getPostsByFollowings() {
        //given
        Post likePost = new Post(2, TITLE, CONTENT, UserTest.SELLER);
        likePost.addPostImage(PostImageTest.POST_IMAGE);

        //when
        doReturn(new PageImpl<>(Arrays.asList(likePost, post))).when(postRepository).findByFollowings(any(), any());
        PostResponses response = postService.getPostsByFollowings(1, 1);

        //then
        assertThat(response.getPosts().size()).isEqualTo(2);
        assertThat(response.getTotalElement()).isEqualTo(2L);
        response.getPosts().forEach(postResponse -> assertThat(Arrays.asList(UserTest.USER.getId(), UserTest.SELLER.getId()).contains(postResponse.getUser().getId())));
    }

    @Test
    public void getPostsByKeywordOrderedByCreatedAt() {
        //given
        Integer page = 1;
        String sort = "newest";
        String keyword = "test";
        Post likePost = new Post(2, TITLE, CONTENT, UserTest.SELLER);
        likePost.addLikePost(new LikePost(1, UserTest.USER, likePost));
        likePost.addPostImage(PostImageTest.POST_IMAGE);

        //when
        doReturn(new PageImpl<>(Arrays.asList(post, likePost))).when(postRepository).findByKeyword(any(), any());
        doReturn(new PageImpl<>(Arrays.asList(likePost, post))).when(postRepository).findByLikePostSizeAndKeyword(any(), any());
        SimplePostResponses response = postService.getSearchPosts(page, sort, keyword);

        //then
        assertThat(response.getPosts().get(0).getId()).isEqualTo(post.getId());
    }
    @Test
    public void getPostsByKeywordOrderedByLikePostSize() {
        //given
        Integer page = 1;
        String sort = "popular";
        String keyword = "test";
        Post likePost = new Post(2, TITLE, CONTENT, UserTest.SELLER);
        likePost.addLikePost(new LikePost(1, UserTest.USER, likePost));
        likePost.addPostImage(PostImageTest.POST_IMAGE);

        //when
        doReturn(new PageImpl<>(Arrays.asList(post, likePost))).when(postRepository).findByKeyword(any(), any());
        doReturn(new PageImpl<>(Arrays.asList(likePost, post))).when(postRepository).findByLikePostSizeAndKeyword(any(), any());
        SimplePostResponses response = postService.getSearchPosts(page, sort, keyword);

        //then
        assertThat(response.getPosts().get(0).getId()).isEqualTo(likePost.getId());
    }

    @Test
    public void deletePost() {
        //given
        Integer id = 1;

        //when
        when(postRepository.findById(anyInt())).thenReturn(Optional.of(post));
        postService.deletePost(id);

        //then
        verify(postImageService).deletePost(anyInt());
        verify(postRepository).deleteById(anyInt());
    }

}
