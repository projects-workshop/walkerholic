package com.yunhalee.walkerholic.post.controller;

import com.yunhalee.walkerholic.post.dto.PostRequest;
import com.yunhalee.walkerholic.post.dto.PostResponse;
import com.yunhalee.walkerholic.post.dto.PostResponses;
import com.yunhalee.walkerholic.post.dto.SimplePostResponses;
import com.yunhalee.walkerholic.post.dto.UserPostResponse;
import com.yunhalee.walkerholic.post.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class PostController {

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/posts")
    public ResponseEntity<PostResponse> createPost(@RequestPart("postRequest") PostRequest request,
        @RequestPart(value = "multipartFile") List<MultipartFile> multipartFiles) {
        return new ResponseEntity<PostResponse>(postService.createPost(request, multipartFiles), HttpStatus.CREATED);
    }

    @PatchMapping("/posts")
    public ResponseEntity<PostResponse> updatePost(@RequestPart("postRequest") PostRequest request,
        @RequestPart(value = "multipartFile", required = false) List<MultipartFile> multipartFiles,
        @RequestParam(value = "deletedImages", required = false) List<String> deletedImages) {
        return ResponseEntity.ok(postService.updatePost(request, multipartFiles, deletedImages));
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(postService.getPost(id));
    }

    @GetMapping("/users/{id}/posts")
    public ResponseEntity<UserPostResponse> getUserPosts(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(postService.getUserPosts(id));
    }

    @GetMapping("/users/{id}/posts/discover")
    public ResponseEntity<SimplePostResponses> getPostsByRandom(@PathVariable("id") Integer id, @RequestParam("page") Integer page) {
        return ResponseEntity.ok(postService.getPostsByRandom(page, id));
    }

    @GetMapping("/users/{id}/posts/follow")
    public ResponseEntity<PostResponses> getPostsByFollowings(@PathVariable("id") Integer id, @RequestParam("page") Integer page) {
        return ResponseEntity.ok(postService.getPostsByFollowings(page, id));
    }

    @GetMapping("/posts")
    public ResponseEntity<SimplePostResponses> getHomePosts(@RequestParam("page") Integer page, @RequestParam("sort") String sort) {
        return ResponseEntity.ok(postService.getHomePosts(page, sort));
    }

    @GetMapping("/posts/search")
    public ResponseEntity<SimplePostResponses> getSearchPosts(@RequestParam("page") Integer page, @RequestParam("sort") String sort, @RequestParam(value = "keyword") String keyword) {
        return ResponseEntity.ok(postService.getSearchPosts(page, sort, keyword));
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity deletePost(@PathVariable("id") Integer id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
