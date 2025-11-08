package org.example.basespringbootproject.application.mapper;

import org.mapstruct.MappingTarget;

import java.util.List;

public interface BaseMapper<E, D> {

    /**
     * Convert từ entity sang DTO.
     */
    D toDto(E entity);

    /**
     * Convert từ DTO sang entity.
     */
    E toEntity(D dto);

    /**
     * Convert list entity sang list DTO.
     */
    List<D> toDtoList(List<E> entities);

    /**
     * Convert list DTO sang list entity.
     */
    List<E> toEntityList(List<D> dtos);

    /**
     * Update entity hiện có bằng dữ liệu từ DTO.
     */
    void updateEntityFromDto(D dto, @MappingTarget E entity);
}
