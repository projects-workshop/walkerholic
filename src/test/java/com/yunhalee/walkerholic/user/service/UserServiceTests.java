package com.yunhalee.walkerholic.user.service;

import com.yunhalee.walkerholic.user.dto.UserResponse;
import com.yunhalee.walkerholic.user.dto.UserListDTO;
import com.yunhalee.walkerholic.user.dto.UserRegisterDTO;
import com.yunhalee.walkerholic.user.dto.UserSearchDTO;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.user.domain.UserRepository;
import com.yunhalee.walkerholic.security.jwt.JwtTokenUtil;
import com.yunhalee.walkerholic.security.jwt.service.JwtUserDetailsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserServiceTests {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    JwtUserDetailsService userDetailsService;


    @Test
    public void singUp() throws IOException {
        //given
        String firstname = "test";
        String lastname = "register";
        String email = "test@example.com";
        String password = "123456";
        String phoneNumber = "01000000000";
        String description = "This is test.";

        UserRegisterDTO userRegisterDTO = new UserRegisterDTO(firstname, lastname, email, password,
            phoneNumber, description, false);

        MultipartFile multipartFile = new MockMultipartFile("uploaded-file",
            "sampleFile.txt",
            "text/plain",
            "This is the file content".getBytes());

        //when
        UserResponse userResponse = userService.saveUser(userRegisterDTO, multipartFile);

        //then
        assertNotNull(userResponse.getId());
        assertEquals(firstname, userResponse.getFirstname());
        assertEquals(lastname, userResponse.getLastname());
        assertEquals(email, userResponse.getEmail());
        assertTrue(passwordEncoder
            .matches(password, userRepository.findById(userResponse.getId()).get().getPassword()));
        assertEquals(phoneNumber, userResponse.getPhoneNumber());
        assertEquals(description, userResponse.getDescription());
        assertEquals("/profileUploads/" + userResponse.getId() + "/" + "sampleFile.txt",
            userResponse.getImageUrl());
    }

    @Test
    public void signIn() throws Exception {
        //given
        String email = "test@example.com";
        String password = "123456";

        //when
        try {
            Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(email, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        final String token = jwtTokenUtil.generateToken(userDetails.getUsername());

        User user = userRepository.findByEmail(userDetails.getUsername());
        UserResponse userResponse = new UserResponse(user);

        //then
        assertNotNull(userResponse.getId());
        assertEquals(userResponse.getEmail(), email);
        assertTrue(passwordEncoder
            .matches(password, userRepository.findById(userResponse.getId()).get().getPassword()));
        assertNotNull(token);
    }

    @Test
    public void getUserByToken() {
        //given
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjYwZTZhN2VlOWE5YzM0NmFiY2UwOGZjNyIsImlhdCI6MTYyNTk4Njk1OCwiZXhwIjoxNjI4NTc4OTU4fQ.CEF6_3eb1tXcv8m8xMSIvUG5VY8dJ2bC8ke9pZaTXog";

        //when
        String email = jwtTokenUtil.getUsernameFromToken(token);
        User user = userRepository.findByEmail(email);

        //then
        assertNotNull(email);
        assertNotNull(user.getId());
        assertEquals(email, user.getEmail());
    }

    @Test
    public void isEmailUnique() throws IOException {
        //given
        String firstname = "test";
        String lastname = "duplicate";
        String email = "test@example.com";
        String password = "123456";
        String phoneNumber = "01000000000";
        String description = "This is test.";

        UserRegisterDTO userRegisterDTO = new UserRegisterDTO(firstname, lastname, email, password,
            phoneNumber, description, false);

        //when
        UserResponse userResponse = userService.saveUser(userRegisterDTO, null);

        //then
        fail("User Email is Duplicated");

    }

    @Test
    public void updateUser() throws IOException {
        //given
        Integer id = 17;
        String firstname = "test";
        String lastname = "update";
        String email = "test@example.com";
        String password = "123456";
        String phoneNumber = "01000000000";
        String description = "This is test.";

        UserRegisterDTO userRegisterDTO = new UserRegisterDTO(id, firstname, lastname, email,
            password, phoneNumber, description, false);

        //when
        UserResponse userResponse = userService.saveUser(userRegisterDTO, null);

        //then
        assertNotEquals(lastname, userResponse.getLastname());
    }

    @Test
    public void getUser() {
        //given
        Integer id = 17;

        //when
        UserResponse userResponse = userService.getUser(id);

        //then
        assertNotNull(userResponse.getId());
        assertEquals(userResponse.getId(), id);
    }

    @Test
    public void getUsersByPage() {
        //given
        Integer page = 1;
        String sort = "id";

        //when
        HashMap<String, Object> response = userService.getUsers(page, sort);
        List<UserListDTO> userListDTOS = (List<UserListDTO>) response.get("users");

        //then
        assertEquals(9, userListDTOS.size());
        for (UserListDTO userListDTO : userListDTOS) {
            System.out.println(userListDTO.getId());
        }
    }

    @Test
    public void getUserByKeyword() {
        //given
        String keyword = "lee";

        //when
        List<UserSearchDTO> userSearchDTOS = userService.searchUser(keyword);

        //then
        assertNotEquals(userSearchDTOS.size(), 0);
        userSearchDTOS.forEach(userSearchDTO -> System.out
            .println(userSearchDTO.getFirstname() + userSearchDTO.getLastname()));
    }

    @Test
    public void deleteUser() {
        //given
        Integer id = 17;

        //when
        userService.deleteUser(id);

        //then
        assertNull(userRepository.findById(id));
    }


}
