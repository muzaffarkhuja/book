package com.example.book.service.Impl;

import com.example.book.dto.ResponseDto;
import com.example.book.dto.author.AuthorCreateDto;
import com.example.book.dto.author.AuthorDto;
import com.example.book.model.Author;
import com.example.book.model.Book;
import com.example.book.repository.AuthorRepository;
import com.example.book.repository.BookRepository;
import com.example.book.service.AbstractService;
import com.example.book.service.mapper.AuthorMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.book.service.validator.AppStatusCodes.*;
import static com.example.book.service.validator.AppStatusMessages.*;

@Service
public class AuthorServiceImpl extends AbstractService<AuthorMapper, AuthorRepository, AuthorDto, AuthorCreateDto, AuthorDto> {
    private final BookRepository bookRepository;

    protected AuthorServiceImpl(@Qualifier("authorMapperImpl") AuthorMapper mapper, AuthorRepository repository, BookRepository bookRepository) {
        super(mapper, repository);
        this.bookRepository = bookRepository;
    }

    @Override
    public ResponseDto<AuthorDto> add(AuthorCreateDto authorDto) {
        try {
            Optional<Author> byFullName = repository.findByFullName(authorDto.getFullName());
            if (byFullName.isPresent()){
                return ResponseDto.<AuthorDto>builder()
                        .code(DUPLICATE_ERROR_CODE)
                        .message("Author already exsists")
                        .build();
            }
            Author author = mapper.toEntity(authorDto);
            author = repository.save(author);

            return ResponseDto.<AuthorDto>builder()
                    .success(true)
                    .message(OK)
                    .data(mapper.toDto(author))
                    .build();
        } catch (Exception e) {
            return ResponseDto.<AuthorDto>builder()
                    .code(DATABASE_ERROR_CODE)
                    .message(DATABASE_ERROR + " -> " + e.getCause())
                    .build();
        }
    }

    @Override
    public ResponseDto<AuthorDto> getById(Integer id) {
        return repository.findById(id)
                .map(author -> ResponseDto.<AuthorDto>builder()
                        .success(true)
                        .message(OK)
                        .data(mapper.toDto(author))
                        .build())
                .orElse(ResponseDto.<AuthorDto>builder()
                        .message(NOT_FOUND)
                        .code(NOT_FOUND_ERROR_CODE)
                        .build());
    }

    @Override
    public ResponseDto<List<AuthorDto>> getAll() {
        try {
            return ResponseDto.<List<AuthorDto>>builder()
                    .success(true)
                    .message(OK)
                    .data(repository.findAll().stream().map(mapper::toDto).toList())
                    .build();
        } catch (Exception e) {
            return ResponseDto.<List<AuthorDto>>builder()
                    .code(DATABASE_ERROR_CODE)
                    .message(DATABASE_ERROR + " -> " + e.getMessage())
                    .build();
        }
    }

    @Override
    public ResponseDto<AuthorDto> update(AuthorDto authorDto) {
        if (authorDto.getId() == null) {
            return ResponseDto.<AuthorDto>builder()
                    .code(VALIDATION_ERROR_CODE)
                    .message(NULL_VALUE)
                    .build();
        }

        Optional<Author> optional = repository.findById(authorDto.getId());

        if (optional.isEmpty()) {
            return ResponseDto.<AuthorDto>builder()
                    .code(NOT_FOUND_ERROR_CODE)
                    .message(NOT_FOUND)
                    .build();
        }

        Author update = mapper.update(authorDto, optional.get());

        update = repository.save(update);

        return ResponseDto.<AuthorDto>builder()
                .message(OK)
                .success(true)
                .data(mapper.toDto(update))
                .build();

    }

    @Override
    public ResponseDto<Void> delete(Integer id) {
        try {
            if(repository.findById(id).isEmpty()){
                return ResponseDto.<Void>builder()
                        .code(NOT_FOUND_ERROR_CODE)
                        .message(NOT_FOUND)
                        .build();
            }
            List<Book> byAuthorId = bookRepository.findByAuthorId(id);
            for (Book book : byAuthorId) {
                bookRepository.deleteById(book.getId());
            }
            repository.deleteById(id);
            return ResponseDto.<Void>builder()
                    .success(true)
                    .message(OK)
                    .build();
        } catch (Exception e) {
            return ResponseDto.<Void>builder()
                    .message(DATABASE_ERROR + " -> " + e.getMessage())
                    .code(DATABASE_ERROR_CODE)
                    .build();
        }
    }
}
