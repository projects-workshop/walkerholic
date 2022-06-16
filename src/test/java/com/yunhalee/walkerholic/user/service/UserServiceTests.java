package com.yunhalee.walkerholic.user.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.yunhalee.walkerholic.MockBeans;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.user.domain.UserTest;
import com.yunhalee.walkerholic.user.dto.UserListResponse;
import com.yunhalee.walkerholic.user.dto.UserRegisterDTO;
import com.yunhalee.walkerholic.user.dto.UserResponse;
import com.yunhalee.walkerholic.user.dto.UserResponses;
import com.yunhalee.walkerholic.user.dto.UserSearchDTO;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.data.domain.PageImpl;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceTests extends MockBeans {

    private static final Integer ID = 1;
    private static final String FIRST_NAME = "testFirstName";
    private static final String LAST_NAME = "TestLastName";
    private static final String EMAIL = "test@example.com";
    private static final String PASSWORD = "123456";
    private static final String PHONE_NUMBER = "";
    private static final String DESCRIPTION = "";
    private static final boolean IS_SELLER = false;

    @InjectMocks
    private UserService userService = new UserService(userRepository, passwordEncoder, s3ImageUploader);


    @Test
    void getUser() {
        //when
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(UserTest.USER));
        UserResponse userResponse = userService.getUser(ID);

        //then
        equals(userResponse, UserTest.USER);
    }

    @Test
    public void getUsersByPage() {
        //given
        Integer page = 1;
        String sort = "id";

        //when
        doReturn(new PageImpl<>(Arrays.asList(UserTest.USER, UserTest.SELLER))).when(userRepository).findAllUsers(any());
        UserResponses userResponses= userService.getUsers(page, sort);

        //then
        assertThat(userResponses.getTotalPage()).isEqualTo(1);
        assertThat(userResponses.getTotalElement()).isEqualTo(2);
        assertThat(userResponses.getUsers().size()).isEqualTo(2);
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
    public void deleteUser() {
        //given
        Integer id = 17;

        //when
        userService.deleteUser(id);

        //then
        assertNull(userRepository.findById(id));
    }


    private void equals(UserResponse userResponse, User user) {
        assertThat(userResponse.getId()).isEqualTo(user.getId());
        assertThat(userResponse.getFirstname()).isEqualTo(user.getFirstname());
        assertThat(userResponse.getLastname()).isEqualTo(user.getLastname());
        assertThat(userResponse.getEmail()).isEqualTo(user.getEmail());
        assertThat(userResponse.getRole()).isEqualTo(user.getRoleName());
        assertThat(userResponse.getImageUrl()).isEqualTo(user.getImageUrl());
        assertThat(userResponse.getPhoneNumber()).isEqualTo(user.getPhoneNumber());
        assertThat(userResponse.getLevel()).isEqualTo(user.getLevelName());
        assertThat(userResponse.getDescription()).isEqualTo(user.getDescription());
        assertThat(userResponse.isSeller()).isEqualTo(user.isSeller());
    }

}
