package com.logsoluprobl.appbank.repository;

import java.util.List;
import java.util.Optional;

public interface JsonRepository<T> {

    List<T> findAll();
    Optional<T> findById(String id);
    void save(T entity);
    void update(T entity);
    void delete(String id);
}
