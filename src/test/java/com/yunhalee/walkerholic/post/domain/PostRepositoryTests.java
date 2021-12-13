package com.yunhalee.walkerholic.post.domain;

import com.yunhalee.walkerholic.post.domain.Post;
import com.yunhalee.walkerholic.post.domain.PostImage;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.post.domain.PostImageRepository;
import com.yunhalee.walkerholic.post.domain.PostRepository;
import com.yunhalee.walkerholic.user.domain.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Rollback(false)
@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
public class PostRepositoryTests {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostImageRepository postImageRepository;

    @Autowired
    private UserRepository userRepository;

    public static final int POST_PER_PAGE = 9;

    @Test
    public void createPost() {
        //given
        String content = "testPost";
        Integer postImageId = 1;
        Integer userId = 1;
        List<PostImage> postImages = new ArrayList<>();
        postImages.add(postImageRepository.findById(postImageId).get());
        User user = userRepository.findById(userId).get();

        Post post = new Post();
        post.setContent(content);
        post.setPostImages(postImages);
        post.setUser(user);

        //when
        Post post1 = postRepository.save(post);

        //then
        assertThat(post1.getId()).isNotNull();
        assertThat(post1.getContent()).isEqualTo(content);
        assertThat(post1.getUser().getId()).isEqualTo(userId);
        List<Integer> postImageIds = post1.getPostImages().stream()
            .map(postImage -> postImage.getId()).collect(Collectors.toList());
        assertThat(postImageIds).contains(postImageId);
    }

    @Test
    public void updatePost() {
        //given
        Integer postId = 1;
        Post post = postRepository.findById(postId).get();
        String originalContent = post.getContent();

        post.setContent("updateTestContent");

        //when
        Post post1 = postRepository.save(post);

        //then
        assertThat(post1.getContent()).isNotEqualTo(originalContent);
    }

    @Test
    public void getPostById() {
        //given
        Integer id = 1;

        //when
        Post post = postRepository.findByPostId(id);

        //then
        assertThat(post.getId()).isEqualTo(id);
    }

    @Test
    public void getPostByUserId() {
        //given
        Integer userId = 1;

        //when
        List<Post> posts = postRepository.findByUserId(userId);

        //then
        for (Post post : posts) {
            assertThat(post.getUser().getId()).isEqualTo(userId);
        }
    }

    @Test
    public void getPostByRandom() {
        //given
        Integer userId = 1;
        Integer page = 1;

        //when
        Pageable pageable = PageRequest.of(page - 1, POST_PER_PAGE);
        Page<Post> pagePost = postRepository.findByRandom(pageable, userId);
        List<Post> posts = pagePost.getContent();

        //then
        for (Post post : posts) {
            assertThat(post.getUser().getId()).isNotEqualTo(userId);
        }
    }

    @Test
    public void getPostByFollowings() {
        //given
        List<Integer> followings = new ArrayList<>();
        followings.add(1);
        Integer page = 1;

        //when
        Pageable pageable = PageRequest.of(page - 1, POST_PER_PAGE);
        Page<Post> pagePost = postRepository.findByFollowings(pageable, followings);
        List<Post> posts = pagePost.getContent();

        //then
        for (Post post : posts) {
            assertThat(followings).contains(post.getUser().getId());
        }
    }

    @Test
    public void getPostByLikePosts() {
        //given
        Integer page = 1;

        //when
        Pageable pageable = PageRequest.of(page - 1, POST_PER_PAGE);
        Page<Post> postPage = postRepository.findByLikePostSize(pageable);
        List<Post> posts = postPage.getContent();

        //then
        Integer priorLikeSize = posts.get(0).getLikePosts().size();
        for (int i = 1; i < posts.size(); i++) {
            assertThat(posts.get(i).getLikePosts().size()).isLessThanOrEqualTo(priorLikeSize);
            priorLikeSize = posts.get(i).getLikePosts().size();
        }
    }

    @Test
    public void getPostsByKeywordOrderByCreatedAt() {
        //given
        Integer page = 1;
        String sort = "newest";
        String keyword = "t";

        //when
        Pageable pageable = PageRequest.of(page - 1, POST_PER_PAGE);
        Page<Post> postPage = postRepository.findByKeyword(pageable, keyword);
        List<Post> posts = postPage.getContent();

        //then
        posts.forEach(post -> assertThat(post.getTitle().contains(keyword)));
        posts.forEach(post -> System.out.println(post.getTitle()));
    }

    @Test
    public void getPostsByKeywordOrderByLikePostsSize() {
        //given
        Integer page = 1;
        String sort = "likeposts";
        String keyword = "t";

        //when
        Pageable pageable = PageRequest.of(page - 1, POST_PER_PAGE);
        Page<Post> postPage = postRepository.findByLikePostSizeAndKeyword(pageable, keyword);
        List<Post> posts = postPage.getContent();

        //then
        Integer priorLikeSize = posts.get(0).getLikePosts().size();
        for (int i = 1; i < posts.size(); i++) {
            assertThat(posts.get(i).getLikePosts().size()).isLessThanOrEqualTo(priorLikeSize);
            priorLikeSize = posts.get(i).getLikePosts().size();
        }
        posts.forEach(post -> assertThat(post.getTitle().contains(keyword)));
        posts.forEach(post -> System.out.println(post.getTitle()));
    }


    @Test
    public void deletePostById() {
        //given
        Integer id = 1;
        Post post = postRepository.findById(id).get();
        for (PostImage postImage : post.getPostImages()) {
            postImageRepository.deleteById(postImage.getId());
        }

        //when
        postRepository.deleteById(id);

        //then
        assertThat(postRepository.findById(id)).isNull();
    }

}
