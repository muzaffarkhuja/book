package com.example.book.service.mapper;

public interface CommonMapper<Dto, CreateDto, UpdateDto, Model> {
    Dto toDto(Model model);

    Model toEntity(CreateDto dto);
    Model update(UpdateDto dto, Model model);
}