package com.example.book.service.Impl;

import com.example.book.dto.ResponseDto;
import com.example.book.dto.searchs.MostSearchedDTO;
import com.example.book.dto.searchs.SearchsDto;
import com.example.book.repository.SearchRepository;
import com.example.book.service.SearchsService;
import com.example.book.service.mapper.SearchsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.book.service.validator.AppStatusCodes.*;
import static com.example.book.service.validator.AppStatusMessages.*;

@Service
@RequiredArgsConstructor
public class SearchesServiceImpl implements SearchsService {

    private final SearchRepository searchRepository;
    private final SearchsMapper searchsMapper;

    @Override
    public ResponseDto<List<SearchsDto>> getAll() {
        try{
            return ResponseDto.<List<SearchsDto>>builder()
                    .message(OK)
                    .success(true)
                    .data(searchRepository.findAll().stream().map(searchsMapper::toDto).toList())
                    .build();
        } catch (Exception e){
            return ResponseDto.<List<SearchsDto>>builder()
                    .code(DATABASE_ERROR_CODE)
                    .message(DATABASE_ERROR + " -> " + e.getMessage())
                    .build();
        }
    }

    @Override
    public ResponseDto<MostSearchedDTO> getMostSearched() {
        try{
            return ResponseDto.<MostSearchedDTO>builder()
                    .message(OK)
                    .success(true)
                    .data(searchRepository.findMostSearched())
                    .build();
        } catch (Exception e){
            return ResponseDto.<MostSearchedDTO>builder()
                    .code(DATABASE_ERROR_CODE)
                    .message(DATABASE_ERROR)
                    .build();
        }
    }
}
