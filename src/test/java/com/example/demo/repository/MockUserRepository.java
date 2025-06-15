package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MockUserRepository implements UserRepository {
    
    @Override
    public List<User> findAllWithOrders() {
        // Return empty list for testing
        return List.of();
    }
    
    @Override
    public List<User> findAll() {
        // Return empty list for testing
        return List.of();
    }
    
    @Override
    public List<User> findAll(Sort sort) {
        return List.of();
    }
    
    @Override
    public <S extends User> List<S> findAll(Example<S> example) {
        return List.of();
    }
    
    @Override
    public <S extends User> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }
    
    @Override
    public Page<User> findAll(Pageable pageable) {
        return Page.empty(pageable);
    }
    
    @Override
    public Optional<User> findById(Long id) {
        return Optional.empty();
    }
    
    @Override
    public boolean existsById(Long id) {
        return false;
    }
    
    @Override
    public long count() {
        return 0;
    }
    
    @Override
    public void deleteById(Long id) {
        // Do nothing
    }
    
    @Override
    public void delete(User entity) {
        // Do nothing
    }
    
    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        // Do nothing
    }
    
    @Override
    public void deleteAll(Iterable<? extends User> entities) {
        // Do nothing
    }
    
    @Override
    public void deleteAll() {
        // Do nothing
    }
    
    @Override
    public <S extends User> S save(S entity) {
        return entity;
    }
    
    @Override
    public <S extends User> List<S> saveAll(Iterable<S> entities) {
        return List.copyOf((List<S>) entities);
    }
    
    @Override
    public List<User> findAllById(Iterable<Long> ids) {
        return List.of();
    }
    
    @Override
    public void flush() {
        // Do nothing
    }
    
    @Override
    public <S extends User> S saveAndFlush(S entity) {
        return entity;
    }
    
    @Override
    public <S extends User> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.copyOf((List<S>) entities);
    }
    
    @Override
    public void deleteAllInBatch(Iterable<User> entities) {
        // Do nothing
    }
    
    @Override
    public void deleteAllByIdInBatch(Iterable<Long> ids) {
        // Do nothing
    }
    
    @Override
    public void deleteAllInBatch() {
        // Do nothing
    }
    
    @Override
    public User getOne(Long id) {
        return null;
    }
    
    @Override
    public User getById(Long id) {
        return null;
    }
    
    @Override
    public User getReferenceById(Long id) {
        return null;
    }
    
    @Override
    public <S extends User> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }
    
    @Override
    public <S extends User> Page<S> findAll(Example<S> example, Pageable pageable) {
        return Page.empty(pageable);
    }
    
    @Override
    public <S extends User> long count(Example<S> example) {
        return 0;
    }
    
    @Override
    public <S extends User> boolean exists(Example<S> example) {
        return false;
    }
    
    @Override
    public <S extends User, R> R findBy(Example<S> example, 
                              java.util.function.Function<org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
