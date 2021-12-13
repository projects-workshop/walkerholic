package com.yunhalee.walkerholic.user.controller;

import com.yunhalee.walkerholic.user.dto.UserDTO;
import com.yunhalee.walkerholic.user.dto.UserRegisterDTO;
import com.yunhalee.walkerholic.user.dto.UserSearchDTO;
import com.yunhalee.walkerholic.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping("/user/save")
    public ResponseEntity<UserDTO> saveUser(@RequestParam("id") Integer id,
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
        return new ResponseEntity<UserDTO>(userService.saveUser(userRegisterDTO, multipartFile),
            HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable("id") String id) {
        Integer userId = Integer.parseInt(id);
        return new ResponseEntity<UserDTO>(userService.getUser(userId), HttpStatus.OK);
    }

    @GetMapping("/userlist/{page}/{sort}")
    public ResponseEntity<?> getUsers(@PathVariable("page") String page,
        @PathVariable("sort") String sort) {
        Integer pageNumber = Integer.parseInt(page);
        return new ResponseEntity<HashMap<String, Object>>(userService.getUsers(pageNumber, sort),
            HttpStatus.OK);
    }

    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity<?> deleteuser(@PathVariable("id") String id) {
        Integer userId = Integer.parseInt(id);
        return new ResponseEntity<Integer>(userService.deleteUser(userId), HttpStatus.OK);
    }

    @GetMapping("/user/search/{keyword}")
    public ResponseEntity<?> searchUser(@PathVariable("keyword") String keyword) {
        return new ResponseEntity<List<UserSearchDTO>>(userService.searchUser(keyword),
            HttpStatus.OK);
    }

    @PostMapping("/user/forgotPassword/{email}")
    public ResponseEntity<?> sendForgotPassword(@PathVariable("email") String email) {
        return new ResponseEntity<String>(userService.sendForgotPassword(email), HttpStatus.OK);
    }


}
