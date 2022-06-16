package com.yunhalee.walkerholic.user.domain;

import com.yunhalee.walkerholic.security.oauth.domain.ProviderType;
import com.yunhalee.walkerholic.useractivity.domain.UserActivity;
import java.util.Arrays;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class UserLevel {

    @Column(name = "level")
    @Enumerated(EnumType.STRING)
    private Level level = Level.Starter;

    private Integer score = 0;

    public Integer getScore(){
        return score;
    }

    public void updateLevel(UserActivity userActivity) {
        addScore(userActivity);
        changeLevel();
    }

    private void addScore(UserActivity userActivity) {
        if (userActivity.finished()) {
            score += userActivity.getScore();
        }
    }

    private void changeLevel() {
        this.level = Arrays.stream(Level.values())
            .filter(level -> level.getMin() <= score && level.getMax() >= score)
            .findFirst()
            .orElse(this.level);
    }

    public void deleteUserActivity(UserActivity userActivity) {
        minusScore(userActivity);
        changeLevel();
    }

    private void minusScore(UserActivity userActivity){
        if (userActivity.finished()) {
            score -= userActivity.getScore();
        }
    }

    public String getLevelName() {
        return this.level.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserLevel userLevel = (UserLevel) o;
        return level == userLevel.level && Objects.equals(score, userLevel.score);
    }

    @Override
    public int hashCode() {
        return Objects.hash(level, score);
    }
}
