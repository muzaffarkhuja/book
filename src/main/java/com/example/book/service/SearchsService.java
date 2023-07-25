package com.example.book.service;

import com.example.book.dto.ResponseDto;
import com.example.book.dto.searchs.MostSearchedDTO;
import com.example.book.dto.searchs.SearchsDto;

import java.util.List;

public interface SearchsService {
    ResponseDto<List<SearchsDto>> getAll();
    ResponseDto<MostSearchedDTO> getMostSearched();
}
