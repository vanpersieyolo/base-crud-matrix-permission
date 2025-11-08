package org.example.basespringbootproject.application.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.basespringbootproject.application.mapper.BaseMapper;
import org.example.basespringbootproject.application.service.IBaseService;
import org.example.basespringbootproject.domain.repository.BaseRepository;
import org.example.basespringbootproject.domain.repository.GenericSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public abstract class BaseServiceImpl<T, ID, D, R extends BaseRepository<T, ID>, M extends BaseMapper<T, D>>
        implements IBaseService<D, ID> {
    protected final R repository;
    protected final M mapper;
    private final Class<T> entityClass;

    protected BaseServiceImpl(R repository, M mapper, Class<T> entityClass) {
        this.repository = repository;
        this.mapper = mapper;
        this.entityClass = entityClass;
    }

    // --- CRUD ---
    @Override
    public D create(D dto) {
        T entity = mapper.toEntity(dto);
        T saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    @Transactional
    public D update(ID id, D dto) {
        T existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(entityClass.getSimpleName() + " not found with id " + id));

        mapper.updateEntityFromDto(dto, existing);
        T saved = repository.save(existing);
        return mapper.toDto(saved);
    }

    @Override
    public void delete(ID id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<D> findById(ID id) {
        return repository.findById(id).map(mapper::toDto);
    }

    @Override
    public List<D> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    // --- SEARCH ---
    @Override
    public Page<D> search(Map<String, Object> filters, Pageable pageable) {
        GenericSpecification<T> spec = new GenericSpecification<>(filters);
        return repository.findAll(spec, pageable).map(mapper::toDto);
    }

    @Override
    public long count(Map<String, Object> filters) {
        GenericSpecification<T> spec = new GenericSpecification<>(filters);
        return repository.count(spec);
    }

    @Override
    public boolean exists(ID id) {
        return repository.existsById(id);
    }
}
