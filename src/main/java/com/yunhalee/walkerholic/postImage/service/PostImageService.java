package com.yunhalee.walkerholic.postImage.service;

import com.yunhalee.walkerholic.common.service.S3ImageUploader;
import com.yunhalee.walkerholic.post.exception.PostNotFoundException;
import com.yunhalee.walkerholic.post.domain.Post;
import com.yunhalee.walkerholic.post.domain.PostRepository;
import com.yunhalee.walkerholic.postImage.domain.PostImage;
import com.yunhalee.walkerholic.postImage.domain.PostImageRepository;
import com.yunhalee.walkerholic.postImage.dto.PostImageResponse;
import com.yunhalee.walkerholic.postImage.dto.PostImageResponses;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class PostImageService {

    private PostImageRepository postImageRepository;
    private PostRepository postRepository;
    private S3ImageUploader s3ImageUploader;

    public PostImageService(PostImageRepository postImageRepository, PostRepository postRepository, S3ImageUploader s3ImageUploader) {
        this.postImageRepository = postImageRepository;
        this.postRepository = postRepository;
        this.s3ImageUploader = s3ImageUploader;
    }

    public PostImageResponses createImages(Integer id, List<MultipartFile> multipartFiles) {
        Post post = findPostById(id);
        List<PostImage> postImages = uploadImages(post, multipartFiles);
        return PostImageResponses.of(postImageResponses(postImages));
    }

    public List<PostImage> uploadImages(Post post, List<MultipartFile> multipartFiles) {
        List<PostImage> postImages = new ArrayList<>();
        multipartFiles.forEach(multipartFile -> postImages.add(uploadImage(post, multipartFile)));
        return postImages;
    }

    private PostImage uploadImage(Post post, MultipartFile multipartFile) {
        String uploadDir = "postUploads/" + post.getId();
        String imageUrl = s3ImageUploader.uploadImage(uploadDir, multipartFile);
        String fileName = s3ImageUploader.getFileName(imageUrl, uploadDir);
        return createPostImage(post, PostImage.of(fileName, imageUrl, post));
    }

    private PostImage createPostImage(Post post, PostImage postImage) {
        PostImage image = postImageRepository.save(postImage);
        post.addPostImage(image);
        return image;
    }

    public void deleteImages(Integer id, List<String> deletedImages) {
        Post post = findPostById(id);
        deletePostImage(post, deletedImages);
    }

    private void deletePostImage(Post post, List<String> deletedImages) {
        deletedImages.forEach(deletedImage -> {
            postImageRepository.deleteByFilePath(deletedImage);
            s3ImageUploader.deleteByFilePath(deletedImage);
            post.deletePostImage(deletedImages);
        });
    }

    public void deletePost(Integer id) {
        String dir = "postUploads/" + id;
        s3ImageUploader.removeFolder(dir);
    }

    private Post findPostById(Integer id) {
        return postRepository.findById(id)
            .orElseThrow(() -> new PostNotFoundException("Post not found with id : " + id));
    }

    private List<PostImageResponse> postImageResponses(List<PostImage> postImages) {
        return postImages.stream()
            .map(PostImageResponse::of)
            .collect(Collectors.toList());
    }
}
