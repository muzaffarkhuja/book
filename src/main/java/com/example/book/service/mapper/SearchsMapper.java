package com.example.book.service.mapper;

import com.example.book.dto.searchs.SearchsDto;
import com.example.book.model.Searches;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SearchsMapper {
    SearchsDto toDto(Searches searches);
    Searches toEntity(SearchsDto dto);
}
