package com.yunhalee.walkerholic.user.service;

import com.yunhalee.walkerholic.util.AmazonS3Utils;
import com.yunhalee.walkerholic.util.FileUploadUtils;
import com.yunhalee.walkerholic.user.dto.UserDTO;
import com.yunhalee.walkerholic.user.dto.UserListDTO;
import com.yunhalee.walkerholic.user.dto.UserRegisterDTO;
import com.yunhalee.walkerholic.user.dto.UserSearchDTO;
import com.yunhalee.walkerholic.user.domain.Level;
import com.yunhalee.walkerholic.user.domain.Role;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.user.exception.UserEmailAlreadyExistException;
import com.yunhalee.walkerholic.user.exception.UserNotFoundException;
import com.yunhalee.walkerholic.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public static final int USER_LIST_PER_PAGE = 10;

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;

    private final AmazonS3Utils amazonS3Utils;


    private void saveProfileFile(MultipartFile multipartFile, User user, boolean isNew)
        throws IOException {
        try {
            String uploadDir = "profileUploads/" + user.getId();
            if (!isNew) {
                amazonS3Utils.removeFolder(uploadDir);
            }
            String imageUrl = amazonS3Utils.uploadFile(uploadDir, multipartFile);
            user.setImageUrl(imageUrl);

        } catch (IOException ex) {
            new IOException("Could not save file : " + multipartFile.getOriginalFilename());
        }

    }

    private boolean isEmailUnique(Integer id, String email) {
        User existingUser = userRepository.findByEmail(email);

        //존재하지 않는 이메일 선택시 true
        if (existingUser == null) {
            return true;
        }
        boolean isCreatingNew = (id == null);

        //이미 존재하는 이메일의 경우 회원의 신입 여부에 따라서 경우를 나눠준다.
        if (isCreatingNew) {        //새로운 가입하려는 경우
            if (existingUser != null) {
                return false;
            }
        } else {                    //기존회원의 이메일변경 확인하는 경우
            if (existingUser.getId() != id) {
                return false;
            }
        }

        return true;

    }

    public UserDTO saveUser(UserRegisterDTO userRegisterDTO, MultipartFile multipartFile)
        throws IOException {

        if (!isEmailUnique(userRegisterDTO.getId(), userRegisterDTO.getEmail())) {
            throw new UserEmailAlreadyExistException(
                "Email already exists : " + userRegisterDTO.getEmail());
        }

        if (userRegisterDTO.getId() != null) {  //기존회원의 프로필 수정하는 경우
            User existingUser = userRepository.findById(userRegisterDTO.getId()).get();
            existingUser.setFirstname(userRegisterDTO.getFirstname());
            existingUser.setLastname(userRegisterDTO.getLastname());
            existingUser.setEmail(userRegisterDTO.getEmail());
            if (userRegisterDTO.getPassword() != null) {
                existingUser.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
            }
            if (userRegisterDTO.isSeller()) {
                existingUser.setRole(Role.SELLER);
            } else {
                existingUser.setRole(Role.USER);
            }
            existingUser.setDescription(userRegisterDTO.getDescription());
            existingUser.setPhoneNumber(userRegisterDTO.getPhoneNumber());

            if (multipartFile != null) {
                saveProfileFile(multipartFile, existingUser, false);
            }

            userRepository.save(existingUser);

            return new UserDTO(existingUser);

        } else {  //새로운회원을 등록하는 경우
            User user = new User();
            user.setFirstname(userRegisterDTO.getFirstname());
            user.setLastname(userRegisterDTO.getLastname());
            user.setEmail(userRegisterDTO.getEmail());
            user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
            user.setDescription(userRegisterDTO.getDescription());
            user.setPhoneNumber(userRegisterDTO.getPhoneNumber());
            if (userRegisterDTO.isSeller()) {
                user.setRole(Role.SELLER);
            } else {
                user.setRole(Role.USER);
            }
            user.setLevel(Level.Starter);

            //새로 생성한 유저의 id를 가져오기 위해서 미리 한번 저장해준다.
            userRepository.save(user);

            if (multipartFile != null) {
                saveProfileFile(multipartFile, user, true);
            }

            userRepository.save(user);

            return new UserDTO(user);
        }
    }

    public UserDTO getUser(Integer id) {
        User user = userRepository.findByUserId(id);
        UserDTO userDTO = new UserDTO(user);

        return new UserDTO(user);
    }

    public HashMap<String, Object> getUsers(Integer page, String sort) {
        Pageable pageable = PageRequest.of(page - 1, USER_LIST_PER_PAGE, Sort.by(sort));
        Page<User> userPage = userRepository.findAllUsers(pageable);
        List<User> users = userPage.getContent();
        List<UserListDTO> userListDTOS = new ArrayList<>();
        users.forEach(user -> userListDTOS.add(new UserListDTO(user)));
        HashMap<String, Object> userList = new HashMap<>();
        userList.put("users", userListDTOS);
        userList.put("totalElement", userPage.getTotalElements());
        userList.put("totalPage", userPage.getTotalPages());
        return userList;
    }

    public Integer deleteUser(Integer id) {
        userRepository.deleteById(id);
        String dir = "profileUploads/" + id;
        FileUploadUtils.deleteDir(dir);
        return id;
    }


    public List<UserSearchDTO> searchUser(String keyword) {
        List<User> users = userRepository.findByKeyword(keyword);
        List<UserSearchDTO> userSearchDTOS = new ArrayList<>();
        users.forEach(user -> userSearchDTOS.add(new UserSearchDTO(user)));
        return userSearchDTOS;
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
            message.setSubject(user.getFullname() + " : New Temporary Password is here!");
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

        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }
        return str;
    }
}
