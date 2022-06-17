package com.yunhalee.walkerholic.user.service;

import com.yunhalee.walkerholic.common.service.S3ImageUploader;
import com.yunhalee.walkerholic.user.dto.UserRequest;
import com.yunhalee.walkerholic.user.dto.UserResponses;
import com.yunhalee.walkerholic.user.dto.UserSearchResponses;
import com.yunhalee.walkerholic.util.FileUploadUtils;
import com.yunhalee.walkerholic.user.dto.UserResponse;
import com.yunhalee.walkerholic.user.dto.UserListResponse;
import com.yunhalee.walkerholic.user.dto.UserRegisterDTO;
import com.yunhalee.walkerholic.user.dto.UserSearchResponse;
import com.yunhalee.walkerholic.user.domain.Level;
import com.yunhalee.walkerholic.user.domain.Role;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.user.exception.UserEmailAlreadyExistException;
import com.yunhalee.walkerholic.user.exception.UserNotFoundException;
import com.yunhalee.walkerholic.user.domain.UserRepository;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class UserService {

    private static final String UPLOAD_DIR = "profileUploads";

    public static final int USER_LIST_PER_PAGE = 10;

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
//    private JavaMailSender mailSender;
//    private String sender;
    private S3ImageUploader s3ImageUploader;

    public UserService(UserRepository userRepository,
        PasswordEncoder passwordEncoder,
//        JavaMailSender mailSender,
//        @Value("${spring.mail.username}") String sender,
        S3ImageUploader s3ImageUploader) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
//        this.mailSender = mailSender;
//        this.sender = sender;
        this.s3ImageUploader = s3ImageUploader;
    }

    public UserResponse getUser(Integer id) {
        return UserResponse.of(userRepository.findByUserId(id));
    }


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

    public UserSearchResponses searchUser(String keyword) {
        List<User> users = userRepository.findByKeyword(keyword);
        return UserSearchResponses.of(userSearchResponses(users));
    }

    private List<UserSearchResponse> userSearchResponses(List<User> users) {
        return users.stream()
            .map(UserSearchResponse::of)
            .collect(Collectors.toList());
    }

    public String uploadImage(MultipartFile multipartFile){
        String imageUrl = s3ImageUploader.uploadImage(UPLOAD_DIR, multipartFile);
        return imageUrl;
    }

    public UserResponse create(UserRequest request) {
        checkEmail(request.getEmail());
        User user = request.toUser(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return UserResponse.of(user);
    }

    private void checkEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserEmailAlreadyExistException( "Email already exists : " + email);
        }
    }

    public UserResponse update(Integer id, UserRequest request) {
        checkEmail(id, request.getEmail());
        User user = findUserById(id);
        user.update(updateUser(request));
        return UserResponse.of(user);
    }

    private void checkEmail(Integer id, String email) {
        if (userRepository.existsByEmail(email) && (findUserByEmail(email).getId() != id)) {
            throw new UserEmailAlreadyExistException( "Email already exists : " + email);
        }
    }

    private User updateUser(UserRequest request) {
        if (request.getPassword().isBlank() || request.getPassword().isEmpty()){
            return request.toUser();
        }
        return request.toUser(passwordEncoder.encode(request.getPassword()));
    }

    public Integer deleteUser(Integer id) {
        userRepository.deleteById(id);
        String dir = "profileUploads/" + id;
        FileUploadUtils.deleteDir(dir);
        return id;
    }


    public String sendForgotPassword(String email) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UserNotFoundException("User not found with email : " + email);
        } else {
            String tempPassword = getTempPassword();
            user.setPassword(passwordEncoder.encode(tempPassword));
            userRepository.save(user);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setFrom(sender);
            message.setSubject(user.getFullName() + " : New Temporary Password is here!");
            message.setText("Hello" + user.getFirstname()
                + "! We send your temporary password here. \nBut this is not secured so please change password once you sign into our site. \nPassword : "
                + tempPassword);
            mailSender.send(message);
            return "Temporary password sent to your email.";
        }
    }

    public String getTempPassword() {
        char[] charSet = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C',
            'D', 'E', 'F',
            'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y', 'Z'};
        String str = "";
        for (int i = 0; i < 10; i++) {
            str += charSet[(int) (charSet.length * Math.random())];
        }
        return str;
    }

    public User findUserById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id : " + id));
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found with email : " + email));
    }
}
