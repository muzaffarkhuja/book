package com.example.book.service;

import com.example.book.dto.ResponseDto;
import com.example.book.dto.book.BookCreateDto;
import com.example.book.dto.book.BookDto;
import com.example.book.dto.book.BookUpdateDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface BookService {
    ResponseDto<BookDto> add(BookCreateDto bookDto);
    ResponseDto<BookDto> getById(Integer id);
    ResponseDto<Page<BookDto>> getAll(Map<String, String> map);

    ResponseDto<BookDto> update(BookUpdateDto bookDto);
    ResponseDto<Void> delete(Integer id);

    ResponseDto<List<BookDto>> getByKeyword(String[] keywords);
}
