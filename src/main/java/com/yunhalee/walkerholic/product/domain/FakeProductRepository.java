package com.yunhalee.walkerholic.product.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class FakeProductRepository implements ProductRepository {

    private Product product = new Product("testProduct", "testBrand", Category.TUMBLER, 12, 1000f);

    public FakeProductRepository() {
    }

    @Override
    public Product findByProductId(Integer id) {
        return null;
    }

    @Override
    public Page<Product> findByUserId(Integer id, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Product> findAllByCategory(Pageable pageable, Category category, String keyword) {
        return null;
    }

    @Override
    public Page<Product> findAllByKeyword(Pageable pageable, String keyword) {
        return null;
    }

    @Override
    public Page<Product> findAllBySellerAndCategory(Pageable pageable, Integer id,
        Category category, String keyword) {
        return null;
    }

    @Override
    public Page<Product> findAllBySellerAndKeyword(Pageable pageable, Integer id, String keyword) {
        return null;
    }

    @Override
    public Page<Product> findAllProductList(Pageable pageable) {
        return null;
    }

    @Override
    public List<Product> findAll() {
        return null;
    }

    @Override
    public List<Product> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Product> findAllById(Iterable<Integer> iterable) {
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
    public void delete(Product product) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> iterable) {

    }

    @Override
    public void deleteAll(Iterable<? extends Product> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Product> S save(S s) {
        return null;
    }

    @Override
    public <S extends Product> List<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<Product> findById(Integer integer) {
        return Optional.of(product);
    }

    @Override
    public boolean existsById(Integer integer) {
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Product> S saveAndFlush(S s) {
        return null;
    }

    @Override
    public <S extends Product> List<S> saveAllAndFlush(Iterable<S> iterable) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Product> iterable) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Integer> iterable) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Product getOne(Integer integer) {
        return null;
    }

    @Override
    public Product getById(Integer integer) {
        return null;
    }

    @Override
    public <S extends Product> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Product> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Product> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Product> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Product> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Product> boolean exists(Example<S> example) {
        return false;
    }

}
