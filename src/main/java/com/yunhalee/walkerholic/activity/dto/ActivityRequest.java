package com.yunhalee.walkerholic.activity.dto;

import com.yunhalee.walkerholic.activity.domain.Activity;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class ActivityRequest {

    @NotNull
    private String name;

    @NotNull
    private Integer score;

    @NotNull
    private String description;

    @Builder
    public ActivityRequest(@NonNull String name, @NonNull Integer score,
        @NonNull String description) {
        this.name = name;
        this.score = score;
        this.description = description;
    }

    public Activity toActivity() {
        return Activity.builder()
            .name(name)
            .score(score)
            .description(description).build();
    }

}
