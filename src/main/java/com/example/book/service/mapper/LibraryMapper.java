package com.example.book.service.mapper;

import com.example.book.dto.library.LibraryCreateDto;
import com.example.book.dto.library.LibraryDto;
import com.example.book.dto.library.LibraryUpdateDto;
import com.example.book.model.Library;
import com.example.book.repository.CityRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring",uses = {CityMapper.class},nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class LibraryMapper implements CommonMapper<LibraryDto, LibraryCreateDto, LibraryUpdateDto, Library> {

    @Autowired
    protected CityRepository cityRepository;
    @Override
    @Mapping(target = "city", expression = "java(libraryDto.getCityId() != null ? cityRepository.findById(libraryDto.getCityId()).orElse(library.getCity()) : library.getCity())")
    public abstract Library  update(LibraryUpdateDto libraryDto, @MappingTarget Library library);

    @Mapping(target = "city")
    @Override
    public abstract LibraryDto toDto(Library library);

    @Mapping(target = "city", ignore = true)
    @Override
    public abstract Library toEntity(LibraryCreateDto libraryDto);
}
