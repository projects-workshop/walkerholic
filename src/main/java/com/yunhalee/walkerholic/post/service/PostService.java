package com.yunhalee.walkerholic.post.service;

import com.yunhalee.walkerholic.follow.service.FollowService;
import com.yunhalee.walkerholic.likepost.domain.LikePost;
import com.yunhalee.walkerholic.likepost.dto.LikePostResponse;
import com.yunhalee.walkerholic.post.PostNotFoundException;
import com.yunhalee.walkerholic.postImage.dto.PostImageResponse;
import com.yunhalee.walkerholic.post.dto.PostResponses;
import com.yunhalee.walkerholic.post.dto.SimplePostResponses;
import com.yunhalee.walkerholic.post.dto.SimplePostResponse;
import com.yunhalee.walkerholic.postImage.service.PostImageService;
import com.yunhalee.walkerholic.user.dto.SimpleUserResponse;
import com.yunhalee.walkerholic.post.dto.UserPostResponse;
import com.yunhalee.walkerholic.post.dto.PostRequest;
import com.yunhalee.walkerholic.post.dto.PostResponse;
import com.yunhalee.walkerholic.follow.domain.Follow;
import com.yunhalee.walkerholic.post.domain.Post;
import com.yunhalee.walkerholic.postImage.domain.PostImage;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.post.domain.PostRepository;
import com.yunhalee.walkerholic.user.service.UserService;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PostService {

    public static final int POST_PER_PAGE = 9;

    private PostRepository postRepository;
    private UserService userService;
    private PostImageService postImageService;
    private FollowService followService;

    public PostService(PostRepository postRepository, UserService userService, PostImageService postImageService, FollowService followService) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.postImageService = postImageService;
        this.followService = followService;
    }

    @Transactional
    public PostResponse createPost(PostRequest request, List<MultipartFile> multipartFiles) {
        User user = userService.findUserById(request.getUserId());
        Post post = postRepository.save(request.toPost(user));
        postImageService.uploadImages(post, multipartFiles);
        return postResponse(post);
    }

    @Transactional
    public PostResponse updatePost(Integer id, PostRequest request) {
        Post post = postRepository.findByPostId(id);
        post.update(request.getTitle(), request.getContent());
        return postResponse(post);
    }

    public PostResponse getPost(Integer id) {
        checkPostExists(id);
        Post post = postRepository.findByPostId(id);
        return postResponse(post);
    }

    public UserPostResponse getUserPosts(Integer id) {
        checkPostExists(id);
        List<Post> posts = postRepository.findByUserId(id);
        List<Post> likePosts = postRepository.findByLikePostUserId(id);
        return new UserPostResponse(simplePostResponses(posts), simplePostResponses(likePosts));
    }


    public SimplePostResponses getPostsByRandom(Integer page, Integer userId) {
        Pageable pageable = PageRequest.of(page - 1, POST_PER_PAGE);
        Page<Post> pagePost = postRepository.findByRandom(pageable, userId);
        return simplePostResponses(pagePost);
    }


    public SimplePostResponses getHomePosts(Integer page, String sort) {
        Pageable pageable = PageRequest.of(page - 1, POST_PER_PAGE);
        if (sort.equals("newest")) {
            Page<Post> pagePost = postRepository.findByCreateAt(pageable);
            return simplePostResponses(pagePost);
        }
        Page<Post> pagePost = postRepository.findByLikePostSize(pageable);
        return simplePostResponses(pagePost);
    }

    public PostResponses getPostsByFollowings(Integer page, Integer userId) {
        List<Follow> follows = followService.findAllFollowByFromUserId(userId);
        List<Integer> followings = follows.stream()
            .map(Follow::getToUserId)
            .collect(Collectors.toList());
        followings.add(userId);
        Pageable pageable = PageRequest.of(page - 1, POST_PER_PAGE, Sort.by("createdAt").descending());
        Page<Post> pagePost = postRepository.findByFollowings(pageable, followings);
        return postResponses(pagePost);
    }

    public SimplePostResponses getSearchPosts(Integer page, String sort, String keyword) {
        Pageable pageable = PageRequest.of(page - 1, POST_PER_PAGE);
        if (sort.equals("newest")) {
            Page<Post> pagePost = postRepository.findByKeyword(pageable, keyword);
            return simplePostResponses(pagePost);
        }
        Page<Post> pagePost = postRepository.findByLikePostSizeAndKeyword(pageable, keyword);
        return simplePostResponses(pagePost);
    }

    @Transactional
    public void deletePost(Integer id) {
        postImageService.deletePost(id);
        postRepository.deleteById(id);
    }

    private void checkPostExists(Integer id) {
        if (!postRepository.existsById(id)) {
            throw new PostNotFoundException("Post not found with id : " + id);
        }
    }

    public Post findPostById(Integer id) {
        return postRepository.findById(id)
            .orElseThrow(() -> new PostNotFoundException("Post not found with id : " + id));
    }

    private PostResponse postResponse(Post post) {
        return PostResponse.of(
            post,
            LikePostResponses(post.getLikePosts()),
            PostImageResponses(post.getPostImages()),
            SimpleUserResponse.of(post.getUser())
        );
    }

    private PostResponses postResponses(Page<Post> pagePost) {
        return PostResponses.of(
            pagePost.getContent().stream()
                .map(this::postResponse)
                .collect(Collectors.toList()),
            pagePost.getTotalElements(),
            pagePost.getTotalPages()
        );
    }

    private List<LikePostResponse> LikePostResponses(Set<LikePost> likePosts) {
        return likePosts.stream()
            .map(LikePostResponse::of)
            .collect(Collectors.toList());
    }

    private List<PostImageResponse> PostImageResponses(List<PostImage> postImages) {
        return postImages.stream()
            .map(PostImageResponse::of)
            .collect(Collectors.toList());
    }

    private List<SimplePostResponse> simplePostResponses(List<Post> posts) {
        return posts.stream()
            .map(SimplePostResponse::of)
            .collect(Collectors.toList());
    }

    private SimplePostResponses simplePostResponses(Page<Post> pagePost) {
        return SimplePostResponses.of(
            simplePostResponses(pagePost.getContent()),
            pagePost.getTotalElements(),
            pagePost.getTotalPages()
        );
    }
}
