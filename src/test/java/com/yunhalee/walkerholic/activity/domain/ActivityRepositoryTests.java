package com.yunhalee.walkerholic.activity.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ActivityRepositoryTests {

    @Autowired
    private ActivityRepository repo;

    private Activity activity;

    @ParameterizedTest
    @CsvSource({"test1, 50, test-activity", "test3, 100, test-activity-create"})
    @DisplayName("주어진 정보대로 액티비티를 생성한다.")
    public void createActivity(String name, int score, String description) {
        //given
        activity = Activity.builder()
            .name(name)
            .score(score)
            .description(description).build();

        //when
        Activity createdActivity = repo.save(activity);

        //then
        Assertions.assertAll(
            () -> assertThat(createdActivity.getId()).isNotNull(),
            () -> assertThat(createdActivity.getName()).isEqualTo(activity.getName()),
            () -> assertThat(createdActivity.getDescription()).isEqualTo(activity.getDescription()),
            () -> assertThat(createdActivity.getScore()).isEqualTo(activity.getScore()),
            () -> assertThat(createdActivity.getImageUrl()).isEqualTo(activity.getImageUrl())
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    @DisplayName("아이디를 이용해서 액티비티를 조회한다.")
    public void find_activity_with_id(Integer id) {
        //given

        //when
        Activity activity = repo.findByActivityId(id);

        //then
        assertThat(activity.getId()).isEqualTo(id);
    }


}
