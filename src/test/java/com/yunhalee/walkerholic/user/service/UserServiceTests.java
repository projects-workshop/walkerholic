package com.yunhalee.walkerholic.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.yunhalee.walkerholic.MockBeans;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.user.domain.UserTest;
import com.yunhalee.walkerholic.user.dto.UserRequest;
import com.yunhalee.walkerholic.user.dto.UserResponse;
import com.yunhalee.walkerholic.user.dto.UserResponses;
import com.yunhalee.walkerholic.user.dto.UserSearchResponses;
import com.yunhalee.walkerholic.user.exception.UserEmailAlreadyExistException;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.data.domain.PageImpl;

class UserServiceTests extends MockBeans {

    private static final Integer ID = 1;

    private static final String DUPLICATED_EMAIL_EXCEPTION = "Email already exists : ";

    private static final UserRequest USER_REQUEST = new UserRequest(UserTest.USER.getFirstname(),
        UserTest.USER.getLastname(),
        UserTest.USER.getEmail(),
        UserTest.USER.getPassword(),
        UserTest.USER.getImageUrl(),
        UserTest.USER.getPhoneNumber(),
        UserTest.USER.getDescription(),
        UserTest.USER.isSeller());


    @InjectMocks
    private UserService userService = new UserService(userRepository, passwordEncoder, s3ImageUploader, cartService);


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
        String keyword = "Name";

        //when
        when(userRepository.findByKeyword(anyString())).thenReturn(Arrays.asList(UserTest.USER, UserTest.SELLER));
        UserSearchResponses userSearchResponses = userService.searchUser(keyword);

        //then
        assertThat(userSearchResponses.getUsers().size()).isEqualTo(2);
    }


    @Test
    public void sing_up() {

        //when
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(UserTest.USER);
        UserResponse userResponse = userService.create(USER_REQUEST);

        //then
        equals(userResponse, UserTest.USER);
    }

    @Test
    public void sing_up_with_already_exists_email_is_invalid() {
        when(userRepository.existsByEmail(any())).thenReturn(true);
        assertThatThrownBy(() -> userService.create(USER_REQUEST))
            .isInstanceOf(UserEmailAlreadyExistException.class)
            .hasMessageContaining(DUPLICATED_EMAIL_EXCEPTION);
    }

    @Test
    public void updateUser() {
        //given
        UserRequest updateRequest = new UserRequest(UserTest.SELLER.getFirstname(),
            UserTest.SELLER.getLastname(),
            UserTest.SELLER.getEmail(),
            UserTest.SELLER.getPassword(),
            UserTest.SELLER.getImageUrl(),
            UserTest.SELLER.getPhoneNumber(),
            UserTest.SELLER.getDescription(),
            UserTest.SELLER.isSeller());

        //when
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(userRepository.findById(any())).thenReturn(Optional.of(UserTest.USER));
        UserResponse userResponse = userService.update(ID, USER_REQUEST);

        //then
        assertThat(userResponse.getId()).isEqualTo(UserTest.USER.getId());
        assertThat(userResponse.getFirstname()).isEqualTo(UserTest.SELLER.getFirstname());
        assertThat(userResponse.getLastname()).isEqualTo(UserTest.SELLER.getLastname());
        assertThat(userResponse.getEmail()).isEqualTo(UserTest.SELLER.getEmail());
        assertThat(userResponse.getRole()).isEqualTo(UserTest.SELLER.getRoleName());
        assertThat(userResponse.getImageUrl()).isEqualTo(UserTest.SELLER.getImageUrl());
        assertThat(userResponse.isSeller()).isEqualTo(UserTest.SELLER.isSeller());
    }


    @Test
    public void deleteUser() {
        //when
        userService.delete(ID);

        //then
        verify(cartService).deleteByUserId(any());
        verify(s3ImageUploader).deleteByFilePath(any());
        verify(userRepository).delete(any());
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
