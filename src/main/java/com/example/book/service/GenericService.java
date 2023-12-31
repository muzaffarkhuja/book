package com.example.book.service;

import com.example.book.dto.ResponseDto;

import java.util.List;

/**
 *
 * @param <CD> CreateDTO
 * @param <UD> UpdateDTO
 * @param <D> DTO
 */
public interface GenericService<CD, UD, D> {
    ResponseDto<D> add(CD authorDto);

    ResponseDto<D> getById(Integer id);

    ResponseDto<List<D>> getAll();

    ResponseDto<D> update(UD authorDto);

    ResponseDto<Void> delete(Integer id);
}
