package com.yunhalee.walkerholic.post.service;

import com.yunhalee.walkerholic.common.service.S3ImageUploader;
import com.yunhalee.walkerholic.util.FileUploadUtils;
import com.yunhalee.walkerholic.post.dto.PostCreateDTO;
import com.yunhalee.walkerholic.post.dto.PostDTO;
import com.yunhalee.walkerholic.user.dto.UserPostDTO;
import com.yunhalee.walkerholic.follow.domain.Follow;
import com.yunhalee.walkerholic.post.domain.Post;
import com.yunhalee.walkerholic.post.domain.PostImage;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.follow.domain.FollowRepository;
import com.yunhalee.walkerholic.post.domain.PostImageRepository;
import com.yunhalee.walkerholic.post.domain.PostRepository;
import com.yunhalee.walkerholic.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    private final PostImageRepository postImageRepository;

    private final FollowRepository followRepository;

    public static final int POST_PER_PAGE = 9;

    private final S3ImageUploader s3ImageUploader;

    @Value("${AWS_S3_BUCKET_URL}")
    private String AWS_S3_BUCKET_URL;

    private void deletePostImage(List<String> deletedImages) {
        for (String deletedImage : deletedImages) {
            postImageRepository.deleteByFilePath(deletedImage);
            String fileName = deletedImage.substring(AWS_S3_BUCKET_URL.length() + 1);
            s3ImageUploader.deleteFile(fileName);
        }
    }

    private List<PostImage> savePostImage(Post post, List<MultipartFile> multipartFiles) {
        List<PostImage> postImageList = new ArrayList<>();
        multipartFiles.forEach(multipartFile -> {
            PostImage postImage = new PostImage();

            try {
                String uploadDir = "postUploads/" + post.getId();
                String imageUrl = s3ImageUploader.uploadFile(uploadDir, multipartFile);
                String fileName = imageUrl
                    .substring(AWS_S3_BUCKET_URL.length() + uploadDir.length() + 2);
                postImage.setName(fileName);
                postImage.setFilePath(imageUrl);
                postImage.setPost(post);
                PostImage postImage1 = postImageRepository.save(postImage);
                postImageList.add(postImage1);

            } catch (IOException ex) {
                new IOException("Could not save file : " + multipartFile.getOriginalFilename());
            }
        });
        return postImageList;
    }

    public PostDTO savePost(PostCreateDTO postCreateDTO, List<MultipartFile> multipartFiles,
        List<String> deletedImages) {
        if (postCreateDTO.getId() != null) {
            Post existingPost = postRepository.findById(postCreateDTO.getId()).get();
            existingPost.setTitle(postCreateDTO.getTitle());
            existingPost.setContent(postCreateDTO.getContent());
            if (deletedImages != null && deletedImages.size() != 0) {
                deletePostImage(deletedImages);
            }
            if (multipartFiles != null) {
                List<PostImage> postImageList = savePostImage(existingPost, multipartFiles);
                postRepository.save(existingPost);
                return new PostDTO(existingPost, postImageList);

            }
            postRepository.save(existingPost);
            return new PostDTO(existingPost);
        } else {
            Post post = new Post();
            User user = userRepository.findById(postCreateDTO.getUserId()).get();
            post.setTitle(postCreateDTO.getTitle());
            System.out.println(postCreateDTO.getTitle());
            post.setContent(postCreateDTO.getContent());
            post.setUser(user);
            postRepository.save(post);
            if (multipartFiles != null) {
                List<PostImage> postImageList = savePostImage(post, multipartFiles);
                postRepository.save(post);
                return new PostDTO(post, postImageList);
            }
            postRepository.save(post);
            return new PostDTO(post);

        }

    }

    public PostDTO getPost(Integer id) {
        Post post = postRepository.findByPostId(id);
        return new PostDTO(post);
    }

    public HashMap<String, Object> getUserPosts(Integer id) {
        List<Post> posts = postRepository.findByUserId(id);
        List<UserPostDTO> userPostDTOS = new ArrayList<>();
        posts.forEach(post -> userPostDTOS.add(new UserPostDTO(post)));

        List<Post> likePosts = postRepository.findByLikePostUserId(id);
        List<UserPostDTO> userLikePostDTOS = new ArrayList<>();
        likePosts.forEach(likePost -> userLikePostDTOS.add(new UserPostDTO(likePost)));

        HashMap<String, Object> userPosts = new HashMap<>();
        userPosts.put("posts", userPostDTOS);
        userPosts.put("likePosts", userLikePostDTOS);
        return userPosts;
    }

    public HashMap<String, Object> getPostsByRandom(Integer page, Integer userId) {
        Pageable pageable = PageRequest.of(page - 1, POST_PER_PAGE);
        Page<Post> pagePost = postRepository.findByRandom(pageable, userId);
        List<Post> posts = pagePost.getContent();
        List<UserPostDTO> userPostDTOList = new ArrayList<>();
        posts.forEach(post -> userPostDTOList.add(new UserPostDTO(post)));
        HashMap<String, Object> randomPosts = new HashMap<>();
        randomPosts.put("posts", userPostDTOList);
        randomPosts.put("totalElement", pagePost.getTotalElements());
        randomPosts.put("totalPage", pagePost.getTotalPages());
        return randomPosts;
    }

    public HashMap<String, Object> getHomePosts(Integer page, String sort) {
        Pageable pageable = PageRequest.of(page - 1, POST_PER_PAGE);
        Page<Post> pagePost = postRepository.findByLikePostSize(pageable);

        if (sort.equals("newest")) {
            pagePost = postRepository.findByCreateAt(pageable);
        }
        List<Post> posts = pagePost.getContent();
        List<UserPostDTO> userPostDTOList = new ArrayList<>();
        posts.forEach(post -> userPostDTOList.add(new UserPostDTO(post)));

        HashMap<String, Object> homePosts = new HashMap<>();
        homePosts.put("posts", userPostDTOList);
        homePosts.put("totalElement", pagePost.getTotalElements());
        homePosts.put("totalPage", pagePost.getTotalPages());

        return homePosts;
    }

    public HashMap<String, Object> getPostsByFollowings(Integer page, Integer userId) {
        List<Follow> follows = followRepository.findAllByFromUserId(userId);
        List<Integer> followings = new ArrayList<>();
        follows.forEach(follow -> followings.add(follow.getId()));
        followings.add(userId);

        Pageable pageable = PageRequest
            .of(page - 1, POST_PER_PAGE, Sort.by("createdAt").descending());

        Page<Post> pagePost = postRepository.findByFollowings(pageable, followings);
        List<Post> posts = pagePost.getContent();
        List<PostDTO> postDTOS = new ArrayList<>();
        posts.forEach(post -> postDTOS.add(new PostDTO(post)));

        HashMap<String, Object> followingPosts = new HashMap<>();
        followingPosts.put("posts", postDTOS);
        followingPosts.put("totalElement", pagePost.getTotalElements());
        followingPosts.put("totalPage", pagePost.getTotalPages());
        return followingPosts;
    }

    public String deletePost(Integer id) {
        String dir = "/productUploads/" + id;
        FileUploadUtils.deleteDir(dir);

        Post post = postRepository.findByPostId(id);
        for (PostImage postImage : post.getPostImages()) {
            postImageRepository.deleteById(postImage.getId());
        }

        postRepository.deleteById(id);
        return "Post Deleted Successfully.";
    }

    public HashMap<String, Object> getSearchPosts(Integer page, String sort, String keyword) {
        Pageable pageable = PageRequest.of(page - 1, POST_PER_PAGE);
        Page<Post> postPage = postRepository.findByLikePostSizeAndKeyword(pageable, keyword);

        if (sort.equals("newest")) {
            postPage = postRepository.findByKeyword(pageable, keyword);
        }

        List<Post> posts = postPage.getContent();
        List<UserPostDTO> userPostDTOS = new ArrayList<>();
        posts.forEach(post -> userPostDTOS.add(new UserPostDTO(post)));

        HashMap<String, Object> response = new HashMap<>();
        response.put("posts", userPostDTOS);
        response.put("totalElement", postPage.getTotalElements());
        response.put("totalPage", postPage.getTotalPages());

        return response;
    }
}
