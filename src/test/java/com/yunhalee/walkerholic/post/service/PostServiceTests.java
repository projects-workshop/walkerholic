package com.yunhalee.walkerholic.post.service;

import com.yunhalee.walkerholic.MockBeans;
import com.yunhalee.walkerholic.likepost.domain.LikePost;
import com.yunhalee.walkerholic.post.domain.PostImageTest;
import com.yunhalee.walkerholic.post.dto.PostRequest;
import com.yunhalee.walkerholic.post.dto.PostResponse;
import com.yunhalee.walkerholic.post.dto.UserPostResponse;
import com.yunhalee.walkerholic.user.domain.UserTest;
import com.yunhalee.walkerholic.post.domain.Post;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@Transactional
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
        userRepository,
        postImageRepository,
        followRepository,
        s3ImageUploader,
        "https://walkerholic-test-you.s3.ap-northeast10.amazonaws.com");

    @Test
    public void createPost() throws IOException {
        //given
        PostRequest request = new PostRequest(TITLE, CONTENT, 1);
        Post post = new Post(1, TITLE, CONTENT, UserTest.USER);

        //when
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(UserTest.USER));
        when(s3ImageUploader.uploadFile(any(), any())).thenReturn(PostImageTest.POST_IMAGE.getFilePath());
        when(postImageRepository.save(any())).thenReturn(PostImageTest.POST_IMAGE);
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
    public void updatePost() throws IOException {
        //given
        String newContent = "updateTestPost";
        Post post = new Post(1, TITLE, CONTENT, UserTest.USER);
        PostRequest request = new PostRequest(post.getId(), post.getTitle(), newContent, post.getUser().getId());

        //when
        when(postRepository.findById(any())).thenReturn(Optional.of(post));
        when(s3ImageUploader.uploadFile(any(), any())).thenReturn(PostImageTest.POST_IMAGE.getFilePath());
        when(postImageRepository.save(any())).thenReturn(PostImageTest.POST_IMAGE);
        PostResponse response = postService.updatePost(request, Arrays.asList(MULTIPART_FILE), null);

        //then
        assertThat(response.getId()).isEqualTo(post.getId());
        assertThat(response.getContent()).isEqualTo(newContent);
        assertThat(response.getPostImages().get(0).getId()).isEqualTo(PostImageTest.POST_IMAGE.getId());
        assertThat(response.getPostImages().get(0).getImageUrl()).isEqualTo(PostImageTest.POST_IMAGE.getFilePath());
    }

    @Test
    public void getPostById() {
        //given
        Integer postId = 1;
        Post post = new Post(postId, TITLE, CONTENT, UserTest.USER);
        post.addPostImage(PostImageTest.POST_IMAGE);
        post.addLikePost(new LikePost(1, UserTest.SELLER, post));

        //when
        when(postRepository.existsById(anyInt())).thenReturn(true);
        when(postRepository.findByPostId(anyInt())).thenReturn(post);
        PostResponse response = postService.getPost(postId);

        //then
        assertThat(response.getId()).isEqualTo(postId);
        assertThat(response.getContent()).isEqualTo(CONTENT);
        assertThat(response.getPostImages().get(0).getId()).isEqualTo(PostImageTest.POST_IMAGE.getId());
        assertThat(response.getPostImages().get(0).getImageUrl()).isEqualTo(PostImageTest.POST_IMAGE.getFilePath());
        assertThat(response.getPostLikes().get(0).getId()).isEqualTo(1);
    }

    @Test
    public void getPostsByUserId() {
        //given
        Post post = new Post(1, TITLE, CONTENT, UserTest.USER);
        post.addPostImage(PostImageTest.POST_IMAGE);
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
//
//    @Test
//    public void getPostsByRandom() {
//        //given
//        Integer userId = 1;
//        Integer page = 1;
//
//        //when
//        HashMap<String, Object> response = postService.getPostsByRandom(page, userId);
//        List<PostResponse> postDTOS = (List<PostResponse>) response.get("posts");
//
//        //then
//        for (PostResponse postDTO : postDTOS) {
//            assertNotEquals(postRepository.findById(postDTO.getId()).get().getUser().getId(),
//                userId);
//        }
//    }
//
//    @Test
//    public void getHomePosts() {
//        //given
//        Integer page = 1;
//        String sort = "popular";
//
//        //when
//        HashMap<String, Object> response = postService.getHomePosts(page, sort);
//        List<UserPostDTO> userPostDTOS = (List<UserPostDTO>) response.get("posts");
//
//        //then
//        Integer priorLikeSize = postRepository.findById(userPostDTOS.get(0).getId()).get()
//            .getLikePosts().size();
//        for (int i = 1; i < userPostDTOS.size(); i++) {
//            assertThat(
//                postRepository.findById(userPostDTOS.get(i).getId()).get().getLikePosts().size())
//                .isLessThanOrEqualTo(priorLikeSize);
//            priorLikeSize = postRepository.findById(userPostDTOS.get(i).getId()).get()
//                .getLikePosts().size();
//        }
//    }
//
//    @Test
//    public void getPostsByFollowings() {
//        //given
//        Integer userId = 1;
//        Integer page = 1;
//
//        //when
//        HashMap<String, Object> response = postService.getPostsByFollowings(page, userId);
//        List<PostResponse> postDTOS = (List<PostResponse>) response.get("posts");
//        List<Integer> followings = followRepository.findAllByFromUserId(userId).stream()
//            .map(follow -> follow.getToUser().getId()).collect(Collectors.toList());
//
//        //then
//        for (PostResponse postDTO : postDTOS) {
//            assertThat(followings)
//                .contains(postRepository.findById(postDTO.getId()).get().getUser().getId());
//        }
//    }
//
//    @Test
//    public void getPostsByKeyword() {
//        //given
//        Integer page = 1;
//        String sort = "likeposts";
//        String keyword = "t";
//
//        //when
//        HashMap<String, Object> response = postService.getSearchPosts(page, sort, keyword);
//        List<UserPostDTO> userPostDTOS = (List<UserPostDTO>) response.get("posts");
//
//        //then
//        Integer priorLikeSize = postRepository.findById(userPostDTOS.get(0).getId()).get()
//            .getLikePosts().size();
//        for (int i = 1; i < userPostDTOS.size(); i++) {
//            assertThat(
//                postRepository.findById(userPostDTOS.get(i).getId()).get().getLikePosts().size())
//                .isLessThanOrEqualTo(priorLikeSize);
//            priorLikeSize = postRepository.findById(userPostDTOS.get(i).getId()).get()
//                .getLikePosts().size();
//        }
//        userPostDTOS.forEach(post -> assertThat(post.getTitle().contains(keyword)));
//        userPostDTOS.forEach(post -> System.out.println(post.getTitle()));
//    }
//
//    @Test
//    public void deletePost() {
//        //given
//        Integer id = 1;
//
//        //when
//        postRepository.deleteById(id);
//
//        //then
////        assertNull(postRepository.findById(id));
//    }

}
