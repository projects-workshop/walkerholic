package com.yunhalee.walkerholic.useractivity.domain;

import com.yunhalee.walkerholic.activity.domain.Activity;
import com.yunhalee.walkerholic.activity.domain.ActivityStatus;
import com.yunhalee.walkerholic.common.domain.BaseTimeEntity;
import com.yunhalee.walkerholic.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "user_activity")
@Getter
@Setter
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

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "from_name")),
        @AttributeOverride(name = "country", column = @Column(name = "from_country")),
        @AttributeOverride(name = "city", column = @Column(name = "from_city")),
        @AttributeOverride(name = "zipcode", column = @Column(name = "from_zipcode")),
        @AttributeOverride(name = "address", column = @Column(name = "from_address")),
        @AttributeOverride(name = "latitude", column = @Column(name = "from_latitude")),
        @AttributeOverride(name = "longitude", column = @Column(name = "from_longitude"))
    })
    private Address from;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "to_name")),
        @AttributeOverride(name = "country", column = @Column(name = "to_country")),
        @AttributeOverride(name = "city", column = @Column(name = "to_city")),
        @AttributeOverride(name = "zipcode", column = @Column(name = "to_zipcode")),
        @AttributeOverride(name = "address", column = @Column(name = "to_address")),
        @AttributeOverride(name = "latitude", column = @Column(name = "to_latitude")),
        @AttributeOverride(name = "longitude", column = @Column(name = "to_longitude"))
    })
    private Address to;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id")
    private Activity activity;


}
