package com.yunhalee.walkerholic.activity.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class FakeActivityRepository implements ActivityRepository {

    public static Activity ACTIVITY = Activity.builder()
        .name("testActivity")
        .score(1)
        .description("This is test Activity.")
        .imageUrl("http://testActivity/imageURL").build();

    public FakeActivityRepository() {
    }

    @Override
    public Activity findByActivityId(Integer id) {
        return ACTIVITY;
    }

    @Override
    public List<Activity> findAll() {
        return Arrays.asList(ACTIVITY);
    }

    @Override
    public List<Activity> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Activity> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Activity> findAllById(Iterable<Integer> iterable) {
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
    public void delete(Activity activity) {
        activity = null;
    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> iterable) {

    }

    @Override
    public void deleteAll(Iterable<? extends Activity> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Activity> S save(S s) {
        return (S) ACTIVITY;
    }

    @Override
    public <S extends Activity> List<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<Activity> findById(Integer integer) {
        return Optional.of(ACTIVITY);
    }

    @Override
    public boolean existsById(Integer integer) {
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Activity> S saveAndFlush(S s) {
        return null;
    }

    @Override
    public <S extends Activity> List<S> saveAllAndFlush(Iterable<S> iterable) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Activity> iterable) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Integer> iterable) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Activity getOne(Integer integer) {
        return null;
    }

    @Override
    public Activity getById(Integer integer) {
        return null;
    }

    @Override
    public <S extends Activity> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Activity> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Activity> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Activity> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Activity> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Activity> boolean exists(Example<S> example) {
        return false;
    }
}
