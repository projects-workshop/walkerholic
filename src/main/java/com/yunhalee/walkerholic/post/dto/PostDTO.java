package com.yunhalee.walkerholic.post.dto;

import com.yunhalee.walkerholic.likepost.domain.LikePost;
import com.yunhalee.walkerholic.post.domain.Post;
import com.yunhalee.walkerholic.user.domain.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class PostDTO {

    private Integer id;

    private String title;

    private String content;

    private PostUser user;

    private List<PostLike> postLikes;

    private List<PostImage> postImages;

    private LocalDateTime createdAt;


    public PostDTO(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.user = new PostUser(post.getUser());
        this.postLikes = PostLike.likeList(post.getLikePosts());
        this.postImages = PostImage.imageList(post.getPostImages());
        this.createdAt = post.getCreatedAt();
    }

    public PostDTO(Post post, List<com.yunhalee.walkerholic.post.domain.PostImage> postImageList) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.user = new PostUser(post.getUser());
        this.postLikes = PostLike.likeList(post.getLikePosts());
        this.postImages = PostImage.imageList(postImageList);
        this.createdAt = post.getCreatedAt();
    }

    @Getter
    static class PostUser {

        private Integer id;
        private String fullname;
        private String email;
        private String imageUrl;
        private String description;

        public PostUser(User user) {
            this.id = user.getId();
            this.fullname = user.getFullname();
            this.email = user.getEmail();
            this.imageUrl = user.getImageUrl();
            this.description = user.getDescription();
        }
    }

    @Getter
    static class PostLike {

        private Integer id;
        private Integer userId;
        private String fullname;
        private String imageUrl;

        static List<PostLike> likeList(Set<LikePost> likePosts) {
            List<PostLike> postLikeList = new ArrayList<>();
            likePosts.forEach(likePost -> postLikeList.add(new PostLike(likePost)));
            return postLikeList;
        }

        public PostLike(LikePost likePost) {
            this.id = likePost.getId();
            this.userId = likePost.getUser().getId();
            this.fullname = likePost.getUser().getFullname();
            this.imageUrl = likePost.getUser().getImageUrl();
        }

    }

    @Getter
    static class PostImage {

        private Integer id;
        private String imageUrl;

        static List<PostImage> imageList(
            List<com.yunhalee.walkerholic.post.domain.PostImage> postImages) {
            List<PostImage> postImageList = new ArrayList<>();
            postImages.forEach(postImage -> postImageList.add(new PostImage(postImage)));
            return postImageList;
        }

        public PostImage(com.yunhalee.walkerholic.post.domain.PostImage postImage) {
            this.id = postImage.getId();
            this.imageUrl = postImage.getFilePath();
        }
    }
}
