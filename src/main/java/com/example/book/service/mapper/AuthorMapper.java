package com.example.book.service.mapper;

import com.example.book.dto.author.AuthorCreateDto;
import com.example.book.dto.author.AuthorDto;
import com.example.book.model.Author;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AuthorMapper extends CommonMapper<AuthorDto, AuthorCreateDto, AuthorDto, Author>,BaseMapper {
    @Override
    Author update(AuthorDto authorDto, @MappingTarget Author author);
}
