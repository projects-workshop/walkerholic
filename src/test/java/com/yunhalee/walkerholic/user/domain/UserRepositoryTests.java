package com.yunhalee.walkerholic.user.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.yunhalee.walkerholic.RepositoryTest;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


public class UserRepositoryTests extends RepositoryTest {

    public static final int USER_LIST_PER_PAGE = 4;

    private User firstUser;
    private User secondUser;
    private User thirdUser;
    private User fourthUser;
    private User fifthUser;

    @Before
    public void setUp() {
        firstUser = save("first", "user", "firstUser@example.com", "123456", Role.USER);
        secondUser = save("second", "user", "secondUser@example.com", "123456", Role.USER);
        thirdUser = save("third", "user", "thirdUser@example.com", "123456", Role.USER);
        fourthUser = save("fourth", "user", "fourthUser@example.com", "123456", Role.SELLER);
        fifthUser = save("fifth", "user", "fifthUser@example.com", "123456", Role.SELLER);
    }

    @Test
    public void get_by_id() {
        // when
        User user = userRepository.findById(firstUser.getId()).get();

        // then
        assertThat(user.equals(firstUser)).isTrue();
    }

    @Test
    public void get_users_by_page_and_sort() {
        // when
        Pageable pageable = PageRequest.of(0, USER_LIST_PER_PAGE, Sort.by("id"));
        Page<User> userPage = userRepository.findAllUsers(pageable);
        List<User> users = userPage.getContent();

        // then
        assertThat(userPage.getTotalPages()).isEqualTo(2);
        assertThat(userPage.getTotalElements()).isEqualTo(5);
        assertThat(users.size()).isEqualTo(4);
        Integer id = users.get(0).getId();
        for (User user : users) {
            assertThat(user.getId()).isGreaterThanOrEqualTo(id);
            id = user.getId();
        }
    }

    @Test
    public void find_users_by_keyword() {
        // given
        String keyword = "f";

        // when
        List<User> users = userRepository.findByKeyword(keyword);

        // then
        assertThat(users.size()).isEqualTo(3);
        assertThat(users.containsAll(Arrays.asList(firstUser, fourthUser, fifthUser))).isTrue();
    }

    @Test
    public void check_existence_of_user_by_email() {
        // when
        boolean existence = userRepository.existsByUserAuthEmail(firstUser.getEmail());

        // then
        assertThat(existence).isTrue();
    }


    private User save(String firstname, String lastname, String email, String password, Role role) {
        User user = User.builder()
            .firstname(firstname)
            .lastname(lastname)
            .email(email)
            .password(password)
            .role(role).build();
        return userRepository.save(user);
    }


}
