package com.yunhalee.walkerholic.activity.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.yunhalee.walkerholic.RepositoryTest;
import org.junit.Before;
import org.junit.Test;

public class ActivityRepositoryTests extends RepositoryTest {


    private Activity firstActivity;
    private Activity secondActivity;

    @Before
    public void setUp() {
        firstActivity = save("firstActivity", 50, "This is first activity.");
        secondActivity = save("secondActivity", 20, "This is second activity.");
    }

    @Test
    public void find_activity_with_id() {
        //when
        Activity activity = activityRepository.findByActivityId(secondActivity.getId());

        //then
        assertThat(activity.getId()).isEqualTo(secondActivity.getId());
    }

    private Activity save(String name, Integer score, String description) {
        Activity activity = Activity.builder()
            .name(name)
            .score(score)
            .description(description).build();
        return activityRepository.save(activity);
    }


}
