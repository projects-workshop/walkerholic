package com.yunhalee.walkerholic.activity.domain;


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

    private Activity activity;

    @Test
    public void createActivity() {
        //given
        createSetUp();

        //when
        Activity createdActivity = repo.save(activity);

        //then
        checkEqual(createdActivity);
    }

    @Test
    public void updateActivity() {
        //given
        createSetUp();
        Activity requestActivity = updateSetUp();
        activity.update(requestActivity);

        //when
        Activity updatedActivity = repo.save(activity);

        //then
        assertThat(activity.getId()).isEqualTo(updatedActivity.getId());
        checkEqual(updatedActivity);
    }

    @Test
    public void getActivityById() {
        //given
        createSetUp();
        Integer id = activity.getId();

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
        assertThat(list.size()).isGreaterThan(0);
    }

    @Test
    public void deleteActivity() {
        //given
        createSetUp();
        Integer id = activity.getId();

        //when
        repo.deleteById(id);

        //then
        assertThat(repo.findById(id)).isNull();
    }


    private void createSetUp() {
        activity = Activity.builder()
            .name("Plogging90")
            .score(100)
            .description("Picking up trash while jogging for more than 90minutes.").build();
    }

    private Activity updateSetUp() {
        return Activity.builder()
            .name("test")
            .description("testUpdate")
            .score(2)
            .imageUrl("testImageUrl").build();
    }

    private void checkEqual(Activity changedActivity) {
        assertThat(changedActivity.getId()).isNotNull();
        assertThat(changedActivity.getName()).isEqualTo(activity.getName());
        assertThat(changedActivity.getDescription()).isEqualTo(activity.getDescription());
        assertThat(changedActivity.getScore()).isEqualTo(activity.getScore());
        assertThat(changedActivity.getImageUrl()).isEqualTo(activity.getImageUrl());
    }


}
