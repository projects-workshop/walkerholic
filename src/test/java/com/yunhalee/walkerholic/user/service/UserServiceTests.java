package com.yunhalee.walkerholic.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.yunhalee.walkerholic.MockBeans;
import com.yunhalee.walkerholic.common.notification.mapper.NotificationMapper;
import com.yunhalee.walkerholic.common.notification.sender.DefaultNotificationSender;
import com.yunhalee.walkerholic.user.domain.Role;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.user.domain.UserTest;
import com.yunhalee.walkerholic.user.dto.UserRequest;
import com.yunhalee.walkerholic.user.dto.UserResponse;
import com.yunhalee.walkerholic.user.dto.UserResponses;
import com.yunhalee.walkerholic.user.dto.UserSearchResponses;
import com.yunhalee.walkerholic.user.dto.UserTokenResponse;
import com.yunhalee.walkerholic.user.exception.UserEmailAlreadyExistException;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.springframework.boot.test.mock.mockito.MockBean;
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

    private static final UserRequest UPDATE_REQUEST = new UserRequest(UserTest.SELLER.getFirstname(),
        UserTest.SELLER.getLastname(),
        UserTest.SELLER.getEmail(),
        UserTest.SELLER.getPassword(),
        UserTest.SELLER.getImageUrl(),
        UserTest.SELLER.getPhoneNumber(),
        UserTest.SELLER.getDescription(),
        UserTest.SELLER.isSeller());

    private static final User USER = User.builder()
        .id(UserTest.USER.getId())
        .firstname(UserTest.USER.getFirstname())
        .lastname(UserTest.USER.getLastname())
        .email(UserTest.USER.getEmail())
        .password(UserTest.USER.getPassword())
        .imageUrl(UserTest.USER.getImageUrl())
        .role(Role.USER).build();


    private static final MockedStatic<NotificationMapper> notificationMapper = mockStatic(NotificationMapper.class);

    @MockBean
    protected DefaultNotificationSender defaultNotificationSender;

    @InjectMocks
    private UserService userService = new UserService(userRepository, passwordEncoder, s3ImageUploader, cartRepository, jwtTokenUtil);


    @Test
    void get_user() {
        //when
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(UserTest.USER));
        UserResponse userResponse = userService.getUser(ID);

        //then
        equals(userResponse, UserTest.USER);
    }

    @Test
    public void get_users_by_page() {
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
    public void get_user_by_keyword() {
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
        when(passwordEncoder.encode(any())).thenReturn(UserTest.USER.getPassword());
        when(userRepository.existsByUserAuthEmail(any())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(USER);
        when(jwtTokenUtil.generateToken(anyString())).thenReturn("token");
        UserTokenResponse response = userService.create(USER_REQUEST);

        //then
        equals(response.getUser(), USER);
    }

    @Test
    public void sing_up_with_already_exists_email_is_invalid() {
        when(userRepository.existsByUserAuthEmail(any())).thenReturn(true);
        assertThatThrownBy(() -> userService.create(USER_REQUEST))
            .isInstanceOf(UserEmailAlreadyExistException.class)
            .hasMessageContaining(DUPLICATED_EMAIL_EXCEPTION);
    }

    @Test
    public void update_user() {
        //when
        when(passwordEncoder.encode(any())).thenReturn(UserTest.SELLER.getPassword());
        when(userRepository.existsByUserAuthEmail(any())).thenReturn(false);
        when(userRepository.findById(any())).thenReturn(Optional.of(USER));
        UserResponse userResponse = userService.update(ID, UPDATE_REQUEST);

        //then
        assertThat(userResponse.getId()).isEqualTo(USER.getId());
        assertThat(userResponse.getFirstname()).isEqualTo(UserTest.SELLER.getFirstname());
        assertThat(userResponse.getLastname()).isEqualTo(UserTest.SELLER.getLastname());
        assertThat(userResponse.getEmail()).isEqualTo(UserTest.SELLER.getEmail());
        assertThat(userResponse.getRole()).isEqualTo(UserTest.SELLER.getRoleName());
        assertThat(userResponse.getImageUrl()).isEqualTo(UserTest.SELLER.getImageUrl());
        assertThat(userResponse.isSeller()).isEqualTo(UserTest.SELLER.isSeller());
        assertThat(USER.getPassword()).isEqualTo(UserTest.SELLER.getPassword());
    }

    @Test
    public void update_with_already_exists_email_is_invalid() {
        when(userRepository.existsByUserAuthEmail(any())).thenReturn(true);
        when(userRepository.findByUserAuthEmail(any())).thenReturn(Optional.of(UserTest.SELLER));
        assertThatThrownBy(() -> userService.update(ID, USER_REQUEST))
            .isInstanceOf(UserEmailAlreadyExistException.class)
            .hasMessageContaining(DUPLICATED_EMAIL_EXCEPTION);
    }


    @Test
    public void delete_user() {
        //when
        when(userRepository.findById(any())).thenReturn(Optional.of(USER));
        userService.delete(ID);

        //then
        verify(cartRepository).deleteByUserId(any());
        verify(s3ImageUploader).deleteByFilePath(any());
        verify(userRepository).delete(any());
    }

    @Test
    public void send_forgot_password() {
        //given
        String tempPassword = "tempPassword";

        //when
        when(userRepository.findByUserAuthEmail(any())).thenReturn(Optional.of(USER));
        when(passwordEncoder.encode(any())).thenReturn(tempPassword);
        when(NotificationMapper.of(any())).thenReturn(defaultNotificationSender);
        userService.sendForgotPassword(USER.getEmail());

        //then
        verify(defaultNotificationSender).sendForgotPasswordNotification(any(), any());
        assertThat(USER.getPassword()).isEqualTo(tempPassword);
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
