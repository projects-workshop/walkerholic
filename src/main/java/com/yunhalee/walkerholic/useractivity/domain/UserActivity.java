package com.yunhalee.walkerholic.useractivity.domain;

import com.yunhalee.walkerholic.activity.domain.Activity;
import com.yunhalee.walkerholic.common.domain.BaseTimeEntity;
import com.yunhalee.walkerholic.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@Entity
@Table(name = "user_activity")
@Getter
@NoArgsConstructor
public class UserActivity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_activity_id")
    private Integer id;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ActivityStatus status;

    private Integer distance;

    @Builder
    public UserActivity(@NonNull ActivityStatus status, Integer distance, @NonNull User user, @NonNull Activity activity) {
        this.status = status;
        this.distance = distance;
        this.user = user;
        this.activity = activity;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id")
    private Activity activity;


    // 비지니스 로직
    public UserActivity update(UserActivity requestedUserActivity) {
        updateLevel(requestedUserActivity);
        this.status = requestedUserActivity.status;
        this.distance = requestedUserActivity.distance;
        return this;
    }

    private void updateLevel(UserActivity requestedUserActivity) {
        if (this.status != requestedUserActivity.getStatus()) {
            this.user.updateLevel(requestedUserActivity);
        }
    }

    public boolean finished() {
        return this.status == ActivityStatus.FINISHED;
    }

    @Transient
    public Integer getScore() {
        return this.activity.getScore();
    }
}
