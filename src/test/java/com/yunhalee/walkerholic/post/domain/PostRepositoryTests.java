package com.yunhalee.walkerholic.post.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.yunhalee.walkerholic.RepositoryTest;
import com.yunhalee.walkerholic.follow.domain.Follow;
import com.yunhalee.walkerholic.likepost.domain.LikePost;
import com.yunhalee.walkerholic.postImage.domain.PostImage;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.user.domain.UserTest;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PostRepositoryTests extends RepositoryTest {

    private static final String FILE_NAME = "testFileName";
    private static final String IMAGE_URL = "testImageUrl";
    private static final Integer PAGE = 1;
    private static final int POST_PER_PAGE = 9;
    private static final Pageable PAGEABLE = PageRequest.of(PAGE - 1, POST_PER_PAGE);

    private User user;
    private User seller;
    private Post firstPost;
    private Post secondPost;
    private Post thirdPost;
    private Post fourthPost;
    private Post fifthPost;

    @Before
    public void setUp() {
        user = userRepository.save(UserTest.USER);
        seller = userRepository.save(UserTest.SELLER);
        followRepository.save(Follow.of(user, seller));
        firstPost = save("firstPost", "This is first post.", user);
        secondPost =  save("secondPost", "This is second post.", user);
        thirdPost =  save("thirdPost", "This is third post.", seller);
        fourthPost = save("fourthPost", "This is fourth post.", user);
        fifthPost =  save("fifthPost", "This is fifth post.", seller);
        likePostRepository.save(LikePost.of(user, thirdPost));
        likePostRepository.save(LikePost.of(user, fifthPost));
    }

    private Post save(String title, String content, User user) {
        Post post = postRepository.save(Post.of(title, content, user));
        post.addPostImage(postImageRepository.save(PostImage.of(FILE_NAME, IMAGE_URL, post)));
        return post;
    }

    @Test
    public void find_post_by_id() {
        //when
        Post post = postRepository.findByPostId(secondPost.getId());

        //then
        assertThat(post.getId()).isEqualTo(secondPost.getId());
    }

    @Test
    public void find_posts_by_user_id() {
        //when
        List<Post> posts = postRepository.findByUserId(user.getId());

        //then
        assertThat(posts.size()).isEqualTo(3);
        assertThat(posts.equals(Arrays.asList(firstPost, secondPost, fourthPost))).isTrue();
    }


    @Test
    public void find_posts_by_random() {
        //when
        Page<Post> pagePost = postRepository.findByRandom(PAGEABLE, user.getId());
        List<Post> posts = pagePost.getContent();

        //then
        assertThat(posts.size()).isEqualTo(2);
        for (Post post : posts) {
            assertThat(post.getUser().getId()).isNotEqualTo(user.getId());
        }
    }

    @Test
    public void find_posts_by_followings() {
        //when
        Page<Post> pagePost = postRepository.findByFollowings(PAGEABLE, Arrays.asList(seller.getId()));
        List<Post> posts = pagePost.getContent();

        //then
        assertThat(posts.size()).isEqualTo(2);
        assertThat(posts.equals(Arrays.asList(thirdPost, fifthPost)));
    }

    @Test
    public void getPostByLikePosts() {
        //when
        Page<Post> postPage = postRepository.findByLikePostSize(PAGEABLE, (PAGE -1) * POST_PER_PAGE);
        List<Post> posts = postPage.getContent();

        //then
        assertThat(posts.equals(Arrays.asList(thirdPost, fifthPost, firstPost, secondPost, fourthPost)));
    }

    @Test
    public void find_posts_by_keyword() {
        //given
        String keyword = "i";

        //when
        Page<Post> postPage = postRepository.findByKeyword(PAGEABLE, keyword);
        List<Post> posts = postPage.getContent();

        //then
        assertThat(posts.size()).isEqualTo(3);
        assertThat(posts.equals(Arrays.asList(fifthPost, thirdPost, firstPost)));
    }

    @Test
    public void find_posts_by_keyword_and_like_posts_size() {
        //given
        String keyword = "r";

        //when
        Page<Post> postPage = postRepository.findByLikePostSizeAndKeyword(PAGEABLE, keyword);
        List<Post> posts = postPage.getContent();

        //then
        assertThat(posts.size()).isEqualTo(3);
        assertThat(posts.equals(Arrays.asList(thirdPost, firstPost, fourthPost)));
    }
}
