package com.yunhalee.walkerholic.postImage.controller;

import com.yunhalee.walkerholic.postImage.dto.PostImageResponses;
import com.yunhalee.walkerholic.postImage.service.PostImageService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/posts/{id}/post-images")
public class PostImageController {

    private final PostImageService postImageService;

    public PostImageController(PostImageService postImageService) {
        this.postImageService = postImageService;
    }

    @PostMapping
    public ResponseEntity<PostImageResponses> createImages(@PathVariable("id") Integer id, @RequestParam(value = "multipartFile") List<MultipartFile> multipartFiles) {
        return ResponseEntity.ok(postImageService.createImages(id, multipartFiles));
    }

    @DeleteMapping
    public ResponseEntity deleteImages(@PathVariable("id") Integer id, @RequestParam(value = "deletedImages") List<String> deletedImages) {
        postImageService.deleteImages(id, deletedImages);
        return ResponseEntity.noContent().build();
    }
}
