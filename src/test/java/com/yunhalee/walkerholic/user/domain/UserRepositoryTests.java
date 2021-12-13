package com.yunhalee.walkerholic.user.domain;

import com.yunhalee.walkerholic.user.domain.Level;
import com.yunhalee.walkerholic.user.domain.Role;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.user.domain.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Rollback(false)
@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
public class UserRepositoryTests {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public static final int USER_LIST_PER_PAGE = 10;

    @Test
    public void createUser() {
        //given
        String firstName = "testFirstName";
        String lastName = "testLastName";
        String email = "test@example.com";
        String password = "123456";
        Role role = Role.USER;
        Level level = Level.Starter;

        User user = new User();
        user.setFirstname(firstName);
        user.setLastname(lastName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user.setLevel(level);

        //when
        User user1 = userRepository.save(user);

        //then
        assertThat(user1.getId()).isNotNull();
        assertThat(user1.getFirstname()).isEqualTo(firstName);
        assertThat(user1.getLastname()).isEqualTo(lastName);
        assertThat(user1.getEmail()).isEqualTo(email);
        assertThat(passwordEncoder.matches(password, user1.getPassword()));
        assertThat(user1.getRole()).isEqualTo(role);
        assertThat(user1.getLevel()).isEqualTo(level);
    }

    @Test
    public void updateUser() {
        //given
        Integer id = 1;
        String firstName = "testUpdateFirstName";
        String lastName = "testUpdateLastName";
        String email = "testUpdate@example.com";
        String password = "12345678";
        Role role = Role.SELLER;
        Level level = Level.Bronze;

        User user = userRepository.findById(id).get();
        user.setFirstname(firstName);
        user.setLastname(lastName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user.setLevel(level);
        user.setSeller(true);

        //when
        User user1 = userRepository.save(user);

        //then
        assertThat(user1.getId()).isEqualTo(id);
        assertThat(user1.getFirstname()).isEqualTo(firstName);
        assertThat(user1.getLastname()).isEqualTo(lastName);
        assertThat(user1.getEmail()).isEqualTo(email);
        assertThat(passwordEncoder.matches(password, user1.getPassword()));
        assertThat(user1.getRole()).isEqualTo(role);
        assertThat(user1.getLevel()).isEqualTo(level);
    }

    @Test
    public void getUserByEmail() {
        //given
        String email = "test@example.com";

        //when
        User user = userRepository.findByEmail(email);

        //then
        assertThat(user.getEmail()).isEqualTo(email);
    }

    @Test
    public void getUserById() {
        //given
        Integer id = 1;

        //when
        User user = userRepository.findByUserId(1);

        //then
        assertThat(user.getId()).isEqualTo(id);
    }

    @Test
    public void getAllUsers() {
        //given
        Integer page = 1;

        //when
        Pageable pageable = PageRequest.of(page - 1, USER_LIST_PER_PAGE);
        Page<User> userPage = userRepository.findAllUsers(pageable);
        List<User> users = userPage.getContent();

        //then
        assertThat(users.size()).isGreaterThan(0);
    }

    @Test
    public void getUserByKeyword() {
        //given
        String keyword = "lee";

        //when
        List<User> users = userRepository.findByKeyword(keyword);

        //then
        assertThat(users.size()).isGreaterThan(0);
        users.forEach(user -> System.out.println(user.getFirstname() + user.getLastname()));
    }

    @Test
    public void deleteUser() {
        //given
        Integer id = 1;

        //when
        userRepository.deleteById(id);

        //then
        assertThat(userRepository.findById(id)).isNull();
    }


}
