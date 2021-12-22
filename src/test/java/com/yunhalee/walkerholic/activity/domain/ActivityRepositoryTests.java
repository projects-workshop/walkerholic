package com.yunhalee.walkerholic.activity.domain;


import com.yunhalee.walkerholic.activity.domain.Activity;
import com.yunhalee.walkerholic.activity.domain.ActivityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class ActivityRepositoryTests {

    @Autowired
    ActivityRepository repo;

    @Test
    public void createActivity() {
        //given
        Activity activity = Activity.builder()
            .name("Plogging90")
            .score(100)
            .description("Picking up trash while jogging for more than 90minutes.").build();

        //when
        Activity activity1 = repo.save(activity);

        //then
        assertThat(activity1.getId()).isGreaterThan(0);
    }

    @Test
    public void updateActivity() {
        //given
        Integer id = 1;
        Activity activity = repo.findById(id).get();
        activity.setScore(3);

        //when
        Activity activity1 = repo.save(activity);

        //then
        assertThat(activity.getScore()).isNotEqualTo(activity1.getScore());
    }

    @Test
    public void getActivityById() {
        //given
        Integer id = 1;

        //when
        Activity activity = repo.findByActivityId(id);

        //then
        assertThat(activity.getId()).isEqualTo(id);
    }

    @Test
    public void getAllActivities() {
        //given

        //when
        List<Activity> list = repo.findAll();

        //then
        assertThat(list.size()).isEqualTo(1);
    }

    @Test
    public void deleteActivity() {
        //given
        Integer id = 1;

        //when
        repo.deleteById(id);

        //then
        assertThat(repo.findById(id)).isNull();
    }


}
