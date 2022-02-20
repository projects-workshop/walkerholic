package com.yunhalee.walkerholic.follow.domain;


import com.yunhalee.walkerholic.user.domain.User;
import java.util.Objects;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "follow")
@Getter
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id")
    private User fromUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id")
    private User toUser;

    public Follow() {
    }

    private Follow(User fromUser, User toUser) {
        this.fromUser = fromUser;
        this.toUser = toUser;
    }

    public static Follow of(User fromUser, User toUser) {
        return new Follow(fromUser, toUser);
    }

    public Integer getToUserId(){
        return toUser.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Follow follow = (Follow) o;
        return Objects.equals(id, follow.id) && Objects
            .equals(fromUser, follow.fromUser) && Objects.equals(toUser, follow.toUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fromUser, toUser);
    }
}
