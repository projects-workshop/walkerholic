package com.yunhalee.walkerholic.post.domain;

import com.yunhalee.walkerholic.likepost.domain.LikePost;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class LikePosts {

    @OneToMany(mappedBy = "post")
    private Set<LikePost> likePosts = new HashSet<>();

    public LikePosts() {
    }

    private LikePosts(Set<LikePost> likePosts) {
        this.likePosts = likePosts;
    }

    public static LikePosts of(LikePost... likePosts) {
        return new LikePosts(new HashSet<>(Set.of(likePosts)));
    }

    public void addLikePost(LikePost likePost) {
        likePosts.add(likePost);
    }

    public Set<LikePost> getLikePosts() {
        return Collections.unmodifiableSet(likePosts);
    }
}
