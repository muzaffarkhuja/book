package com.example.book.service;

import com.example.book.dto.ResponseDto;
import com.example.book.dto.library.LibraryCreateDto;
import com.example.book.dto.library.LibraryDto;
import com.example.book.dto.library.LibraryUpdateDto;

import java.util.List;

public interface LibraryService {
    ResponseDto<LibraryDto> add(LibraryCreateDto libraryDto);
    ResponseDto<LibraryDto> getById(Integer id);
    ResponseDto<List<LibraryDto>> getAll();
    ResponseDto<LibraryDto> update(LibraryUpdateDto libraryDto);
    ResponseDto<Void> delete(Integer id);
}
