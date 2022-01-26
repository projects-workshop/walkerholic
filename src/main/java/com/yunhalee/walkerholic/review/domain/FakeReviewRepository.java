package com.yunhalee.walkerholic.review.domain;

import com.yunhalee.walkerholic.product.domain.Category;
import com.yunhalee.walkerholic.product.domain.Product;
import com.yunhalee.walkerholic.user.domain.Role;
import com.yunhalee.walkerholic.user.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class FakeReviewRepository implements ReviewRepository {

    private User user = new User("testFirstName",
        "TestLastName",
        "test@example.com",
        "12345678",
        Role.USER);

    private Product product = new Product("testProduct", "testBrand", Category.TUMBLER, 12, 1000f);

    private Review review = Review.builder()
        .rating(2)
        .comment("testReview")
        .user(user)
        .product(product).build();


    public FakeReviewRepository() {
        product.addReview(review);
    }

    @Override
    public Review findByReviewId(Integer id) {
        return null;
    }

    @Override
    public List<Review> findAll() {
        return null;
    }

    @Override
    public List<Review> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Review> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Review> findAllById(Iterable<Integer> iterable) {
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
    public void delete(Review review) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> iterable) {

    }

    @Override
    public void deleteAll(Iterable<? extends Review> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Review> S save(S s) {
        return null;
    }

    @Override
    public <S extends Review> List<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<Review> findById(Integer integer) {
        if (integer > 10) {
            return Optional.empty();
        }
        return Optional.of(review);
    }

    @Override
    public boolean existsById(Integer integer) {
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Review> S saveAndFlush(S s) {
        return null;
    }

    @Override
    public <S extends Review> List<S> saveAllAndFlush(Iterable<S> iterable) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Review> iterable) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Integer> iterable) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Review getOne(Integer integer) {
        return null;
    }

    @Override
    public Review getById(Integer integer) {
        return null;
    }

    @Override
    public <S extends Review> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Review> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Review> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Review> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Review> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Review> boolean exists(Example<S> example) {
        return false;
    }

}
