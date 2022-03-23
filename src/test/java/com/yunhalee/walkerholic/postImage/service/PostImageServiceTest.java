package com.yunhalee.walkerholic.postImage.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.yunhalee.walkerholic.MockBeans;
import com.yunhalee.walkerholic.post.domain.Post;
import com.yunhalee.walkerholic.post.domain.PostTest;
import com.yunhalee.walkerholic.postImage.domain.PostImage;
import com.yunhalee.walkerholic.postImage.dto.PostImageResponse;
import com.yunhalee.walkerholic.postImage.dto.PostImageResponses;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

class PostImageServiceTest extends MockBeans {

    private static final String NAME = "testImage";
    private static final String FILE_PATH = "https://walkerholic-test-you.s3.ap-northeast10.amazonaws.com/postUploads/testImageUrl";
    private static final MultipartFile MULTIPART_FILE = new MockMultipartFile(
        "uploaded-file",
        "sampleFile.txt",
        "text/plain",
        "This is the file content".getBytes());

    @InjectMocks
    private PostImageService postImageService = new PostImageService(
        postImageRepository,
        postRepository,
        s3ImageUploader);

    private PostImage postImage;
    private Post post;

    @BeforeEach
    public void setUp() {
        post = PostTest.POST;
        postImage =  PostImage.builder()
            .id(1)
            .name(NAME)
            .filePath(FILE_PATH)
            .post(post).build();
    }


    @Test
    public void createImages() {
        //given

        //when
        when(postRepository.findById(any())).thenReturn(Optional.of(post));
        when(s3ImageUploader.uploadImage(any(), any())).thenReturn(FILE_PATH);
        when(s3ImageUploader.getFileName(any(), any())).thenReturn(NAME);
        when(postImageRepository.save(any())).thenReturn(postImage);

        PostImageResponses response = postImageService.createImages(1, Arrays.asList(MULTIPART_FILE));
        PostImageResponse postImageResponse = response.getPostImages().get(0);

        //then
        assertThat(response.getPostImages().size()).isEqualTo(1);
        assertThat(postImageResponse.getId()).isEqualTo(postImage.getId());
        assertThat(postImageResponse.getImageUrl()).isEqualTo(FILE_PATH);
    }

    @Test
    public void deleteImages() {
        //given
        post.addPostImage(postImage);

        //when
        when(postRepository.findById(any())).thenReturn(Optional.of(post));
        when(postImageRepository.deleteByFilePath(any())).thenReturn(1);
        postImageService.deleteImages(1, Arrays.asList(FILE_PATH));

        //then
        verify(s3ImageUploader).deleteByFilePath(any());
        assertThat(post.getPostImages().size()).isEqualTo(0);
    }

    @Test
    public void deletePost() {
        //when
        postImageService.deletePost(1);

        //then
        verify(s3ImageUploader).removeFolder(any());
    }


}