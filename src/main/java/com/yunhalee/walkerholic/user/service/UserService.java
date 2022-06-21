package com.yunhalee.walkerholic.user.service;

import com.yunhalee.walkerholic.cart.domain.CartRepository;
import com.yunhalee.walkerholic.cart.service.CartService;
import com.yunhalee.walkerholic.common.notification.mapper.NotificationMapper;
import com.yunhalee.walkerholic.common.service.S3ImageUploader;
import com.yunhalee.walkerholic.security.jwt.JwtTokenUtil;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.user.domain.UserRepository;
import com.yunhalee.walkerholic.user.dto.UserRequest;
import com.yunhalee.walkerholic.user.dto.UserResponse;
import com.yunhalee.walkerholic.user.dto.UserResponses;
import com.yunhalee.walkerholic.user.dto.UserSearchResponse;
import com.yunhalee.walkerholic.user.dto.UserSearchResponses;
import com.yunhalee.walkerholic.user.dto.UserTokenResponse;
import com.yunhalee.walkerholic.user.exception.UserEmailAlreadyExistException;
import com.yunhalee.walkerholic.user.exception.UserNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class UserService {

    private static final String UPLOAD_DIR = "profileUploads";

    public static final int USER_LIST_PER_PAGE = 10;

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private S3ImageUploader s3ImageUploader;
    private CartRepository cartRepository;
    private JwtTokenUtil jwtTokenUtil;

    public UserService(UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        S3ImageUploader s3ImageUploader,
        CartRepository cartRepository,
        JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.s3ImageUploader = s3ImageUploader;
        this.cartRepository = cartRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Transactional(readOnly = true)
    public UserResponse getUser(Integer id) {
        return UserResponse.of(findUserById(id));
    }

    @Transactional(readOnly = true)
    public UserResponses getUsers(Integer page, String sort) {
        Pageable pageable = PageRequest.of(page - 1, USER_LIST_PER_PAGE, Sort.by(sort));
        Page<User> userPage = userRepository.findAllUsers(pageable);
        return UserResponses.of(userResponses(userPage.getContent()),
            userPage.getTotalElements(),
            userPage.getTotalPages());
    }

    private List<UserResponse> userResponses(List<User> users) {
        return users.stream()
            .map(UserResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserSearchResponses searchUser(String keyword) {
        List<User> users = userRepository.findByKeyword(keyword);
        return UserSearchResponses.of(userSearchResponses(users));
    }

    private List<UserSearchResponse> userSearchResponses(List<User> users) {
        return users.stream()
            .map(UserSearchResponse::of)
            .collect(Collectors.toList());
    }

    public String uploadImage(MultipartFile multipartFile) {
        return s3ImageUploader.uploadImage(UPLOAD_DIR, multipartFile);
    }

    public UserTokenResponse create(UserRequest request) {
        checkEmail(request.getEmail());
        User user = request.toUser(passwordEncoder.encode(request.getPassword()));
        final String token = jwtTokenUtil.generateToken(request.getEmail());
        return UserTokenResponse.of(UserResponse.of(userRepository.save(user)), token);
    }

    private void checkEmail(String email) {
        if (userRepository.existsByUserAuthEmail(email)) {
            throw new UserEmailAlreadyExistException("Email already exists : " + email);
        }
    }

    public UserResponse update(Integer id, UserRequest request) {
        checkEmail(id, request.getEmail());
        User user = findUserById(id);
        user.update(updateUser(request));
        return UserResponse.of(user);
    }

    private void checkEmail(Integer id, String email) {
        if (userRepository.existsByUserAuthEmail(email) && (findUserByEmail(email).getId() != id)) {
            throw new UserEmailAlreadyExistException("Email already exists : " + email);
        }
    }

    private User updateUser(UserRequest request) {
        if (request.getPassword().isBlank() || request.getPassword().isEmpty()) {
            return request.toUser();
        }
        return request.toUser(passwordEncoder.encode(request.getPassword()));
    }

    public void delete(Integer id) {
        User user = findUserById(id);
        cartRepository.deleteByUserId(id);
        deleteImage(user);
        userRepository.delete(user);
    }

    private void deleteImage(User user) {
        if (!user.isDefaultImage()) {
            s3ImageUploader.deleteByFilePath(user.getImageUrl());
        }
    }

    public void sendForgotPassword(String email) {
        User user = findUserByEmail(email);
        String tempPassword = getTempPassword();
        user.changePassword(passwordEncoder.encode(tempPassword));
        NotificationMapper.of(user.getNotificationType()).sendForgotPasswordNotification(user, tempPassword);
    }


    public String getTempPassword() {
        char[] charSet = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C',
            'D', 'E', 'F',
            'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y', 'Z'};
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            str.append(charSet[(int) (charSet.length * Math.random())]);
        }
        return str.toString();
    }

    public User findUserById(Integer id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User not found with id : " + id));
    }

    public User findUserByEmail(String email) {
        return userRepository.findByUserAuthEmail(email)
            .orElseThrow(() -> new UserNotFoundException("User not found with email : " + email));
    }
}
