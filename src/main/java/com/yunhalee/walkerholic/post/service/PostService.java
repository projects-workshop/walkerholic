package com.yunhalee.walkerholic.post.service;

import com.yunhalee.walkerholic.common.service.S3ImageUploader;
import com.yunhalee.walkerholic.likepost.domain.LikePost;
import com.yunhalee.walkerholic.likepost.dto.LikePostResponse;
import com.yunhalee.walkerholic.post.PostNotFoundException;
import com.yunhalee.walkerholic.post.dto.PostImageResponse;
import com.yunhalee.walkerholic.post.dto.PostResponses;
import com.yunhalee.walkerholic.post.dto.SimplePostResponses;
import com.yunhalee.walkerholic.post.dto.SimplePostResponse;
import com.yunhalee.walkerholic.post.dto.SimpleUserResponse;
import com.yunhalee.walkerholic.post.dto.UserPostResponse;
import com.yunhalee.walkerholic.user.exception.UserNotFoundException;
import com.yunhalee.walkerholic.post.dto.PostRequest;
import com.yunhalee.walkerholic.post.dto.PostResponse;
import com.yunhalee.walkerholic.follow.domain.Follow;
import com.yunhalee.walkerholic.post.domain.Post;
import com.yunhalee.walkerholic.post.domain.PostImage;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.follow.domain.FollowRepository;
import com.yunhalee.walkerholic.post.domain.PostImageRepository;
import com.yunhalee.walkerholic.post.domain.PostRepository;
import com.yunhalee.walkerholic.user.domain.UserRepository;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class PostService {

    public static final int POST_PER_PAGE = 9;

    private PostRepository postRepository;

    private UserRepository userRepository;

    private PostImageRepository postImageRepository;

    private FollowRepository followRepository;

    private S3ImageUploader s3ImageUploader;

    private String bucketUrl;

    public PostService(PostRepository postRepository,
        UserRepository userRepository,
        PostImageRepository postImageRepository,
        FollowRepository followRepository,
        S3ImageUploader s3ImageUploader,
        @Value("${AWS_S3_BUCKET_URL}") String bucketUrl) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postImageRepository = postImageRepository;
        this.followRepository = followRepository;
        this.s3ImageUploader = s3ImageUploader;
        this.bucketUrl = bucketUrl;
    }

    public PostResponse createPost(PostRequest request, List<MultipartFile> multipartFiles) {
        User user = findUserById(request.getUserId());
        Post post = postRepository.save(request.toPost(user));
        savePostImage(post, multipartFiles);
        return postResponse(post);
    }

    public PostResponse updatePost(PostRequest request, List<MultipartFile> multipartFiles, List<String> deletedImages) {
        Post post = findPostById(request.getId());
        post.update(request.getTitle(), request.getContent());
        deletePostImage(deletedImages);
        savePostImage(post, multipartFiles);
        return postResponse(post);
    }

    private User findUserById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id : " + id));
    }

    private Post findPostById(Integer id) {
        return postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("Post not found with id : " + id));
    }

    private void deletePostImage(List<String> deletedImages) {
        Optional.ofNullable(deletedImages).orElseGet(Collections::emptyList)
            .forEach(deletedImage -> {
                postImageRepository.deleteByFilePath(deletedImage);
                String fileName = deletedImage.substring(bucketUrl.length() + 1);
                s3ImageUploader.deleteFile(fileName);
            });
    }

    private void savePostImage(Post post, List<MultipartFile> multipartFiles) {
        Optional.ofNullable(multipartFiles).orElseGet(Collections::emptyList)
            .forEach(multipartFile -> {
                try {
                    String uploadDir = "postUploads/" + post.getId();
                    String imageUrl = s3ImageUploader.uploadFile(uploadDir, multipartFile);
                    String fileName = imageUrl.substring(bucketUrl.length() + uploadDir.length() + 2);
                    PostImage postImage = postImageRepository.save(PostImage.of(fileName, imageUrl, post));
                    post.addPostImage(postImage);
                } catch (IOException ex) {
                    new IOException("Could not save file : " + multipartFile.getOriginalFilename());
                }
            });
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
        List<Follow> follows = followRepository.findAllByFromUserId(userId);
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

    public void deletePost(Integer id) {
        String dir = "/productUploads/" + id;
        s3ImageUploader.removeFolder(dir);
        postRepository.deleteById(id);
    }

    private void checkPostExists(Integer id) {
        if (!postRepository.existsById(id)) {
            throw new PostNotFoundException("Post not found with id : " + id);
        }
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
