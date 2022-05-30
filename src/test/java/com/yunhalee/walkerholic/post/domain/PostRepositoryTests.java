package com.yunhalee.walkerholic.post.domain;

import com.yunhalee.walkerholic.postImage.domain.PostImageRepository;
import java.time.LocalDateTime;
import java.util.Arrays;
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

import java.util.List;

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

    public static final int POST_PER_PAGE = 9;

    @Test
    public void getPostById() {
        //given
        Integer id = 8;

        //when
        Post post = postRepository.findByPostId(id);

        //then
        assertThat(post.getId()).isEqualTo(id);
    }

    @Test
    public void getPostByUserId() {
        //given
        Integer userId = 4;

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
        Integer userId = 4;
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
        List<Integer> followings = Arrays.asList(4, 14);
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
        Page<Post> postPage = postRepository.findByLikePostSize(pageable, (page -1) * POST_PER_PAGE);
        List<Post> posts = postPage.getContent();

        //then
        Integer priorLikeSize = posts.get(0).getLikePosts().size();
        for (int i = 1; i < posts.size(); i++) {
            assertThat(posts.get(i).getLikePosts().size()).isLessThanOrEqualTo(priorLikeSize);
            priorLikeSize = posts.get(i).getLikePosts().size();
        }
    }

    @Test
    public void getPostsByKeywordOrderByCreatedAtDesc() {
        //given
        Integer page = 1;
        String keyword = "t";

        //when
        Pageable pageable = PageRequest.of(page - 1, POST_PER_PAGE);
        Page<Post> postPage = postRepository.findByKeyword(pageable, keyword);
        List<Post> posts = postPage.getContent();

        //then
        posts.forEach(post -> assertThat(post.getTitle().contains(keyword)||post.getTitle().contains(keyword.toUpperCase())).isTrue());
        LocalDateTime createdAt = posts.get(0).getCreatedAt();
        for (int i = 1; i < posts.size(); i++) {
            assertThat(posts.get(i).getCreatedAt()).isBefore(createdAt);
            createdAt = posts.get(i).getCreatedAt();
        }
    }

    @Test
    public void getPostsByKeywordOrderByLikePostsSize() {
        //given
        Integer page = 1;
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
        posts.forEach(post -> assertThat(post.getTitle().contains(keyword)||post.getTitle().contains(keyword.toUpperCase())).isTrue());
    }
}
