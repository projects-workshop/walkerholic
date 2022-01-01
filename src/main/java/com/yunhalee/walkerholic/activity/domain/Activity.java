package com.yunhalee.walkerholic.activity.domain;

import com.yunhalee.walkerholic.useractivity.domain.UserActivity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;

@Entity
@Table(name = "activity")
@Getter
@Setter
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_id")
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer score;

    @Column(nullable = false)
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt DESC")
    private Set<UserActivity> userActivities = new HashSet<>();

    private static final String DEFAULT_IMAGE_URL =
        "https://walkerholic-with-you.s3.ap-northeast-2.amazonaws.com/globe-asia-solid.svg";

    public Activity() {
    }

    @Builder
    public Activity(@NonNull String name, @NonNull Integer score, @NonNull String description,
        String imageUrl) {
        this.name = name;
        this.score = score;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    // 비지니스 로직
    public Activity update(Activity requestActivity) {
        this.name = requestActivity.name;
        this.description = requestActivity.description;
        this.score = requestActivity.score;
        this.imageUrl = requestActivity.imageUrl;
        return this;
    }

    public void changeImageUrl(String imageUrl) {
        this.imageUrl = (imageUrl == null ? DEFAULT_IMAGE_URL : imageUrl);
    }


}
