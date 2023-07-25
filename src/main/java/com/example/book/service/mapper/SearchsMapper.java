package com.example.book.service.mapper;

import com.example.book.dto.searchs.SearchsDto;
import com.example.book.model.Searchs;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SearchsMapper {
    SearchsDto toDto(Searchs searchs);
    Searchs toEntity(SearchsDto dto);
}
