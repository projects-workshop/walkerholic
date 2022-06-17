package com.yunhalee.walkerholic.user.controller;

import com.yunhalee.walkerholic.user.dto.UserRequest;
import com.yunhalee.walkerholic.user.dto.UserResponse;
import com.yunhalee.walkerholic.user.dto.UserRegisterDTO;
import com.yunhalee.walkerholic.user.dto.UserResponses;
import com.yunhalee.walkerholic.user.dto.UserSearchResponse;
import com.yunhalee.walkerholic.user.dto.UserSearchResponses;
import com.yunhalee.walkerholic.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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
    public ResponseEntity<String> saveImage(@RequestParam("multipartFile") MultipartFile multipartFile){
        return ResponseEntity.ok(userService.uploadImage(multipartFile));
    }

    @PostMapping("/users")
    public ResponseEntity<UserResponse> create(@RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.create(request));
    }

    @PostMapping("/users/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable("id") Integer id, @RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.update(id, request));
    }



    @PostMapping("/user/save")
    public ResponseEntity<UserResponse> saveUser(@RequestParam("id") Integer id,
        @RequestParam("firstname") String firstname,
        @RequestParam("lastname") String lastname,
        @RequestParam("email") String email,
        @RequestParam("password") String password,
        @RequestParam("phoneNumber") String phoneNumber,
        @RequestParam("description") String description,
        @RequestParam("isSeller") boolean isSeller,
        @RequestParam(value = "multipartFile", required = false) MultipartFile multipartFile
    ) throws IOException {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO(id, firstname, lastname, email,
            password, phoneNumber, description, isSeller);
        return new ResponseEntity<UserResponse>(userService.saveUser(userRegisterDTO, multipartFile),
            HttpStatus.OK);
    }


    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteuser(@PathVariable("id") String id) {
        Integer userId = Integer.parseInt(id);
        return new ResponseEntity<Integer>(userService.deleteUser(userId), HttpStatus.OK);
    }

    @PostMapping("/user/forgotPassword/{email}")
    public ResponseEntity<?> sendForgotPassword(@PathVariable("email") String email) {
        return new ResponseEntity<String>(userService.sendForgotPassword(email), HttpStatus.OK);
    }


}
