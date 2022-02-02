package com.yunhalee.walkerholic.useractivity.domain;

import com.yunhalee.walkerholic.activity.domain.FakeActivityRepository;
import com.yunhalee.walkerholic.user.domain.FakeUserRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class FakeUserActivityRepository implements UserActivityRepository{

    private UserActivity userActivity = UserActivity.builder()
        .user(FakeUserRepository.USER)
            .activity(FakeActivityRepository.ACTIVITY)
            .status(ActivityStatus.ONGOING).build();

    public FakeUserActivityRepository() {
    }

    @Override
    public Page<UserActivity> findByUserId(Pageable pageable, Integer id) {
        UserActivity secondUserActivity = UserActivity.builder()
            .user(FakeUserRepository.USER)
            .activity(FakeActivityRepository.ACTIVITY)
            .status(ActivityStatus.FINISHED).build();

        return new PageImpl<>(Arrays.asList(userActivity, secondUserActivity));
    }

    @Override
    public List<UserActivity> findAll() {
        return null;
    }

    @Override
    public List<UserActivity> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<UserActivity> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<UserActivity> findAllById(Iterable<Integer> iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Integer integer) {

    }

    @Override
    public void delete(UserActivity userActivity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> iterable) {

    }

    @Override
    public void deleteAll(Iterable<? extends UserActivity> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends UserActivity> S save(S s) {
        return null;
    }

    @Override
    public <S extends UserActivity> List<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<UserActivity> findById(Integer integer) {
        return Optional.of(userActivity);
    }

    @Override
    public boolean existsById(Integer integer) {
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends UserActivity> S saveAndFlush(S s) {
        return s;
    }

    @Override
    public <S extends UserActivity> List<S> saveAllAndFlush(Iterable<S> iterable) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<UserActivity> iterable) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Integer> iterable) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public UserActivity getOne(Integer integer) {
        return null;
    }

    @Override
    public UserActivity getById(Integer integer) {
        return null;
    }

    @Override
    public <S extends UserActivity> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends UserActivity> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends UserActivity> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends UserActivity> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends UserActivity> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends UserActivity> boolean exists(Example<S> example) {
        return false;
    }
}
