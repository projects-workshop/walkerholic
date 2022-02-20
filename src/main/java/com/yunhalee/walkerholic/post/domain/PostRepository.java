package com.yunhalee.walkerholic.post.domain;

import com.yunhalee.walkerholic.post.domain.Post;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query(value = "SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.user u LEFT JOIN FETCH p.postImages i LEFT JOIN FETCH p.likePosts l LEFT JOIN FETCH l.likePosts i LEFT JOIN FETCH i.user WHERE p.id=:id")
    Post findByPostId(Integer id);

    @Query(value = "SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.postImages i LEFT JOIN FETCH p.likePosts l LEFT JOIN FETCH l.likePosts i LEFT JOIN FETCH p.user u WHERE u.id=:id")
    List<Post> findByUserId(Integer id);

    @Query(value = "SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.postImages i LEFT JOIN FETCH p.likePosts l LEFT JOIN FETCH l.likePosts i LEFT JOIN FETCH i.user u WHERE u.id=id")
    List<Post> findByLikePostUserId(Integer id);

    @Query(value = "SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.postImages i LEFT JOIN FETCH p.likePosts l LEFT JOIN FETCH l.likePosts LEFT JOIN FETCH p.user u WHERE NOT u.id=:id ORDER BY RAND()",
        countQuery = "SELECT count(DISTINCT p) FROM Post p")
    Page<Post> findByRandom(Pageable pageable, Integer id);
//
//    @Query(value = "SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.postImages i LEFT JOIN FETCH p.likePosts LEFT JOIN FETCH p.user u ORDER BY p.createdAt DESC",
//        countQuery = "SELECT count(DISTINCT p) FROM Post p")
//    Page<Post> findByCreateAt(Pageable pageable);

    @Query(value = "SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.postImages i LEFT JOIN FETCH p.likePosts l LEFT JOIN FETCH l.likePosts i LEFT JOIN FETCH p.user u WHERE u.id in :followings",
        countQuery = "SELECT count(DISTINCT p) FROM Post p")
    Page<Post> findByFollowings(Pageable pageable, List<Integer> followings);

//    @Query(value = "SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.postImages i LEFT JOIN FETCH p.likePosts l LEFT JOIN FETCH p.user u ORDER BY SIZE(l.likePosts) DESC",
//        countQuery = "SELECT count(DISTINCT p) FROM Post p")
//    Page<Post> findByLikePostSize(Pageable pageable);
//
//    @Query(value = "SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.postImages i LEFT JOIN FETCH p.likePosts LEFT JOIN FETCH p.user u WHERE p.title LIKE %:keyword% ORDER BY p.createdAt DESC",
//        countQuery = "SELECT count(DISTINCT p) FROM Post p")
//    Page<Post> findByKeyword(Pageable pageable, String keyword);
//
//    @Query(value = "SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.postImages i LEFT JOIN FETCH p.likePosts l LEFT JOIN FETCH p.user u WHERE p.title LIKE %:keyword%  ORDER BY SIZE(l.likePosts) DESC",
//        countQuery = "SELECT count(DISTINCT p) FROM Post p")
//    Page<Post> findByLikePostSizeAndKeyword(Pageable pageable, String keyword);
}
