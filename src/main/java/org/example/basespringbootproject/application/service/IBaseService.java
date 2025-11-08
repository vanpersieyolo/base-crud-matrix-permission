package org.example.basespringbootproject.application.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IBaseService<D, ID> {

    D create(D dto);

    D update(ID id, D dto);

    void delete(ID id);

    Optional<D> findById(ID id);

    List<D> findAll();

    Page<D> search(Map<String, Object> filters, Pageable pageable);

    long count(Map<String, Object> filters);

    boolean exists(ID id);
}