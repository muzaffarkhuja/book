package com.example.book.service.mapper;

/**
 *
 * @param <D> DTO
 * @param <CD> CreateDTO
 * @param <UD> UpdateDTO
 * @param <E> Entity
 */
public interface CommonMapper<D , CD, UD, E> extends BaseMapper {
    D toDto(E model);

    E toEntity(CD dto);
    E update(UD dto, E model);
}