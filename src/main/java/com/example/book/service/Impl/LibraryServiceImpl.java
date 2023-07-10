package com.example.book.service.Impl;

import com.example.book.dto.ResponseDto;
import com.example.book.dto.library.LibraryCreateDto;
import com.example.book.dto.library.LibraryDto;
import com.example.book.dto.library.LibraryUpdateDto;
import com.example.book.model.City;
import com.example.book.model.Library;
import com.example.book.repository.CityRepository;
import com.example.book.repository.LibraryRepository;
import com.example.book.service.LibraryService;
import com.example.book.service.mapper.LibraryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.book.service.validator.AppStatusCodes.*;
import static com.example.book.service.validator.AppStatusMessages.*;

@Service
@RequiredArgsConstructor
public class LibraryServiceImpl implements LibraryService {
    private final LibraryMapper libraryMapper;
    private final LibraryRepository libraryRepository;
    private final CityRepository cityRepository;

    @Override
    public ResponseDto<LibraryDto> add(LibraryCreateDto libraryDto) {
        try {
            Optional<Library> byName = libraryRepository.findByName(libraryDto.getName());
            if(byName.isPresent()){
                return ResponseDto.<LibraryDto>builder()
                        .code(DUPLICATE_ERROR_CODE)
                        .message("Library already exists")
                        .build();
            }
            City city = cityRepository.findById(libraryDto.getCityId()).orElse(null);
            if(city == null){
                return ResponseDto.<LibraryDto>builder()
                        .code(NOT_FOUND_ERROR_CODE)
                        .message(NOT_FOUND)
                        .build();
            }
            Library library = libraryMapper.toEntity(libraryDto);
            library.setCity(city);
            library = libraryRepository.save(library);

            return ResponseDto.<LibraryDto>builder()
                    .success(true)
                    .message(OK)
                    .data(libraryMapper.toDto(library))
                    .build();
        } catch (Exception e){
            return ResponseDto.<LibraryDto>builder()
                    .code(DATABASE_ERROR_CODE)
                    .message(DATABASE_ERROR + " -> " + e.getMessage())
                    .build();
        }
    }

    @Override
    public ResponseDto<LibraryDto> getById(Integer id) {
        return libraryRepository.findById(id)
                .map(library -> ResponseDto.<LibraryDto>builder()
                        .message(OK)
                        .success(true)
                        .data(libraryMapper.toDto(library))
                        .build())
                .orElse(ResponseDto.<LibraryDto>builder()
                        .code(NOT_FOUND_ERROR_CODE)
                        .message(NOT_FOUND)
                        .build());
    }

    @Override
    public ResponseDto<List<LibraryDto>> getAll() {
        try{
            return ResponseDto.<List<LibraryDto>>builder()
                    .success(true)
                    .message(OK)
                    .data(libraryRepository.findAll().stream().map(libraryMapper::toDto).toList())
                    .build();
        } catch (Exception e){
            return ResponseDto.<List<LibraryDto>>builder()
                    .code(DATABASE_ERROR_CODE)
                    .message(DATABASE_ERROR + " -> " + e.getMessage())
                    .build();
        }
    }

    @Override
    public ResponseDto<LibraryDto> update(LibraryUpdateDto libraryDto) {
        if(libraryDto.getId() == null){
            return ResponseDto.<LibraryDto>builder()
                    .code(VALIDATION_ERROR_CODE)
                    .message(NULL_VALUE)
                    .build();
        }

        Optional<Library> optional = libraryRepository.findById(libraryDto.getId());

        if (optional.isEmpty()){
            return ResponseDto.<LibraryDto>builder()
                    .code(NOT_FOUND_ERROR_CODE)
                    .message(NOT_FOUND)
                    .build();
        }

        Library update = libraryMapper.update(libraryDto, optional.get());
        update = libraryRepository.save(update);

        return ResponseDto.<LibraryDto>builder()
                .message(OK)
                .success(true)
                .data(libraryMapper.toDto(update))
                .build();
    }

    @Override
    public ResponseDto<Void> delete(Integer id) {
        try{
            if(libraryRepository.findById(id).isEmpty()){
                return ResponseDto.<Void>builder()
                        .code(NOT_FOUND_ERROR_CODE)
                        .message(NOT_FOUND)
                        .build();
            }
            libraryRepository.deleteById(id);

            return ResponseDto.<Void>builder()
                    .success(true)
                    .message(OK)
                    .build();
        } catch (Exception e){
            return ResponseDto.<Void>builder()
                    .code(DATABASE_ERROR_CODE)
                    .message(DATABASE_ERROR + " -> " + e.getMessage())
                    .build();
        }
    }
}
