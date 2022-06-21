package com.yunhalee.walkerholic.user.controller;

import com.yunhalee.walkerholic.user.dto.UserRequest;
import com.yunhalee.walkerholic.user.dto.UserResponse;
import com.yunhalee.walkerholic.user.dto.UserResponses;
import com.yunhalee.walkerholic.user.dto.UserSearchResponses;
import com.yunhalee.walkerholic.user.dto.UserTokenResponse;
import com.yunhalee.walkerholic.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @GetMapping(value = "/users", params = {"page", "sort"})
    public ResponseEntity<UserResponses> getUsers(@RequestParam("page") Integer page, @RequestParam("sort") String sort) {
        return ResponseEntity.ok(userService.getUsers(page, sort));
    }

    @GetMapping(value = "/users", params = "keyword")
    public ResponseEntity<UserSearchResponses> searchUser(@RequestParam("keyword") String keyword) {
        return ResponseEntity.ok(userService.searchUser(keyword));
    }

    @PostMapping("/users/images")
    public ResponseEntity<String> saveImage(@RequestParam("multipartFile") MultipartFile multipartFile) {
        return ResponseEntity.ok(userService.uploadImage(multipartFile));
    }

    @PostMapping("/users")
    public ResponseEntity<UserTokenResponse> create(@RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.create(request));
    }

    @PostMapping("/users/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable("id") Integer id, @RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.update(id, request));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity delete(@PathVariable("id") Integer id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/users", params = "email")
    public ResponseEntity sendForgotPassword(@RequestParam("email") String email) {
        userService.sendForgotPassword(email);
        return ResponseEntity.noContent().build();
    }


}
