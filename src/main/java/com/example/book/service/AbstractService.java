package com.example.book.service;

import com.example.book.dto.BaseDTO;
import com.example.book.repository.BaseRepository;
import com.example.book.service.mapper.BaseMapper;

/**
 *
 * @param <M> Mapper
 * @param <R> Repository
 * @param <D> DTO
 * @param <CD> CreateDTO
 * @param <UD> UpdateDTO
 */
public abstract class AbstractService<M extends BaseMapper, R extends BaseRepository, D extends BaseDTO,
        CD extends BaseDTO, UD extends BaseDTO> implements GenericService<CD, UD, D> {
    protected M mapper;
    protected R repository;

    protected AbstractService(M mapper, R repository) {
        this.mapper = mapper;
        this.repository = repository;
    }


}
