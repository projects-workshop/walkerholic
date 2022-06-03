package com.yunhalee.walkerholic.follow.domain;

import com.yunhalee.walkerholic.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Integer> {

    @Query(value = "SELECT DISTINCT f FROM Follow f LEFT JOIN FETCH f.fromUser LEFT JOIN FETCH f.toUser t WHERE t.id=:id")
    List<Follow> findAllByToUserId(Integer id);

    @Query(value = "SELECT DISTINCT f FROM Follow f LEFT JOIN FETCH f.toUser t LEFT JOIN FETCH f.fromUser u WHERE u.id=:id")
    List<Follow> findAllByFromUserId(Integer id);

    @Query(value = "SELECT DISTINCT f FROM Follow f LEFT JOIN FETCH f.toUser t LEFT JOIN FETCH f.fromUser u WHERE u.id=:id OR t.id=:id")
    List<Follow> findAllByUserId(Integer id);

    boolean existsByFromUserAndToUser(User fromUser, User toUser);

}
