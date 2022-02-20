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
    public ResponseEntity<SimplePostResponses> getPostsByRandom(@RequestParam("page") Integer page, @PathVariable("id") Integer id) {
        return ResponseEntity.ok(postService.getPostsByRandom(page, id));
    }

    @GetMapping("/users/{id}/posts/follow")
    public ResponseEntity<PostResponses> getPostsByFollowings(@RequestParam("page") Integer page, @PathVariable("id") Integer id) {
        return ResponseEntity.ok(postService.getPostsByFollowings(page, id));
    }
//
//    @GetMapping("/posts/home/{page}/{sort}")
//    public HashMap<String, Object> getHomePosts(@PathVariable("page") String page,
//        @PathVariable("sort") String sort) {
//        Integer pageNumber = Integer.parseInt(page);
//        return postService.getHomePosts(pageNumber, sort);
//    }
//
//    @DeleteMapping("/post/{id}")
//    public String deletePost(@PathVariable("id") String id) {
//        Integer postId = Integer.parseInt(id);
//        return postService.deletePost(postId);
//    }
//
//    @GetMapping("/posts/search/{page}/{sort}/{keyword}")
//    public ResponseEntity<?> getSearchPosts(@PathVariable("page") String page,
//        @PathVariable(value = "keyword") String keyword, @PathVariable("sort") String sort) {
//        Integer pageNumber = Integer.parseInt(page);
//        return new ResponseEntity<HashMap<String, Object>>(
//            postService.getSearchPosts(pageNumber, sort, keyword), HttpStatus.OK);
//    }
}
