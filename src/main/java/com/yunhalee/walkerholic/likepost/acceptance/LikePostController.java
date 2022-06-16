package com.yunhalee.walkerholic.likepost.acceptance;

import com.yunhalee.walkerholic.likepost.dto.LikePostResponse;
import com.yunhalee.walkerholic.likepost.dto.LikePostRequest;
import com.yunhalee.walkerholic.likepost.service.LikePostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/like-posts")
public class LikePostController {

    private final LikePostService likePostService;

    public LikePostController(LikePostService likePostService) {
        this.likePostService = likePostService;
    }

    @PostMapping
    public ResponseEntity<LikePostResponse> likePost(@RequestBody LikePostRequest request) {
        return new ResponseEntity(likePostService.likePost(request), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity unlikePost(@PathVariable("id") Integer id) {
        likePostService.unlikePost(id);
        return ResponseEntity.noContent().build();
    }
}
