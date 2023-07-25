package com.example.book.service.Impl;

import com.example.book.dto.ResponseDto;
import com.example.book.dto.book.BookCreateDto;
import com.example.book.dto.book.BookDto;
import com.example.book.dto.book.BookUpdateDto;
import com.example.book.enums.Theme;
import com.example.book.model.Author;
import com.example.book.model.Book;
import com.example.book.model.Library;
import com.example.book.model.Searchs;
import com.example.book.repository.AuthorRepository;
import com.example.book.repository.BookRepository;
import com.example.book.repository.LibraryRepository;
import com.example.book.repository.SearchRepository;
import com.example.book.service.BookService;
import com.example.book.service.mapper.BookMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.book.service.validator.AppStatusCodes.*;
import static com.example.book.service.validator.AppStatusMessages.*;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookMapper bookMapper;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final LibraryRepository libraryRepository;
    private final SearchRepository searchRepository;

    @Override
    public ResponseDto<BookDto> add(BookCreateDto bookDto) {
        try {
            Optional<Book> byName = bookRepository.findByName(bookDto.getName());
            if (byName.isPresent()) {
                return ResponseDto.<BookDto>builder()
                        .code(DUPLICATE_ERROR_CODE)
                        .message("Book is already exists")
                        .build();
            }
            Author author = getAuthor(bookDto);
            Library library = libraryRepository.findById(bookDto.getLibraryId()).orElse(null);
            if (author == null || library == null) {
                return ResponseDto.<BookDto>builder()
                        .code(NOT_FOUND_ERROR_CODE)
                        .message(NOT_FOUND)
                        .build();
            }
            if(!Theme.check(bookDto.getTheme())){
                return ResponseDto.<BookDto>builder()
                        .code(NOT_FOUND_ERROR_CODE)
                        .message("Book Theme is Not found")
                        .build();
            }
            Book book = bookMapper.toEntity(bookDto);
            book.setAuthor(author);
            book.setLibrary(library);
            book = bookRepository.save(book);

            return ResponseDto.<BookDto>builder()
                    .success(true)
                    .message(OK)
                    .data(bookMapper.toDto(book))
                    .build();
        } catch (Exception e) {
            return ResponseDto.<BookDto>builder()
                    .code(DATABASE_ERROR_CODE)
                    .message(DATABASE_ERROR + " -> " + e.getMessage())
                    .build();
        }


    }

    private Author getAuthor(BookCreateDto bookDto) {
        return authorRepository.findById(bookDto.getAuthorId()).orElse(null);
    }

    @Override
    public ResponseDto<BookDto> getById(Integer id) {
        return bookRepository.findById(id)
                .map(book -> ResponseDto.<BookDto>builder()
                        .message(OK)
                        .success(true)
                        .data(bookMapper.toDto(book))
                        .build())
                .orElse(ResponseDto.<BookDto>builder()
                        .message(NOT_FOUND)
                        .code(NOT_FOUND_ERROR_CODE)
                        .build());
    }

    public ResponseDto<Page<BookDto>> getAll(Map<String, String> map) {
        try {
            PageRequest pageRequest = PageRequest.of(Integer.parseInt(map.getOrDefault("page", "0")),
                    Integer.parseInt(map.getOrDefault("size", "20")));

            Integer id = map.get("id") != null ? Integer.parseInt(map.get("id")) : null;
            Integer minSize = map.get("minSize") != null ? Integer.parseInt(map.get("minSize")) : null;
            Integer maxSize = map.get("maxSize") != null ? Integer.parseInt(map.get("maxSize")) : null;
            Theme theme = null;
            if(Theme.check(map.get("theme"))){
                theme = Enum.valueOf(Theme.class, map.get("theme"));
            }

            Page<Book> books = bookRepository.universalSearch(pageRequest, id, "%" + map.getOrDefault("name", "") + "%",
                    "%" + map.getOrDefault("author", "") + "%", "%" + map.getOrDefault("library", "") + "%",
                    minSize, maxSize, theme, "%"+map.getOrDefault("city","")+"%");

            return ResponseDto.<Page<BookDto>>builder()
                    .message(OK)
                    .success(true)
                    .data(books.map(bookMapper::toDto))
                    .build();
        } catch (Exception e) {
            return ResponseDto.<Page<BookDto>>builder()
                    .code(DATABASE_ERROR_CODE)
                    .message(DATABASE_ERROR + " -> " + e.getMessage())
                    .data(Page.empty())
                    .build();
        }

    }

    @Override
    public ResponseDto<List<BookDto>> getByKeyword(String[] keywords) {
        try {
            List<Book> books = bookRepository.findByKeywords(keywords);
            books.forEach(book -> book.setSearchedCount(book.getSearchedCount() + 1));
            bookRepository.saveAll(books);
            searchRepository.save(new Searchs(keywords, books.size()));


            return ResponseDto.<List<BookDto>>builder()
                    .success(true)
                    .message(OK)
                    .data(books.stream().map(bookMapper::toDto).toList())
                    .build();
        } catch (Exception e){
            return ResponseDto.<List<BookDto>>builder()
                    .code(DATABASE_ERROR_CODE)
                    .message(DATABASE_ERROR)
                    .build();
        }
    }

    @Override
    public ResponseDto<BookDto> update(BookUpdateDto bookDto) {
        if (bookDto.getId() == null) {
            return ResponseDto.<BookDto>builder()
                    .message(NULL_VALUE)
                    .code(VALIDATION_ERROR_CODE)
                    .build();
        }

        Optional<Book> optional = bookRepository.findById(bookDto.getId());

        if (optional.isEmpty()) {
            return ResponseDto.<BookDto>builder()
                    .code(NOT_FOUND_ERROR_CODE)
                    .message(NOT_FOUND)
                    .build();
        }
        if(!Theme.check(bookDto.getTheme())){
            bookDto.setTheme(null);
        }

        Book book = optional.get();
        Book update = bookMapper.update(bookDto, book);

        book = bookRepository.save(update);
        return ResponseDto.<BookDto>builder()
                .success(true)
                .message(OK)
                .data(bookMapper.toDto(book))
                .build();
    }

    @Override
    public ResponseDto<Void> delete(Integer id) {
        try {
            if (bookRepository.findById(id).isEmpty()) {
                return ResponseDto.<Void>builder()
                        .code(NOT_FOUND_ERROR_CODE)
                        .message(NOT_FOUND)
                        .build();
            }
            bookRepository.deleteById(id);

            return ResponseDto.<Void>builder()
                    .success(true)
                    .message(OK)
                    .build();
        } catch (Exception e) {
            return ResponseDto.<Void>builder()
                    .code(DATABASE_ERROR_CODE)
                    .message(DATABASE_ERROR + " -> " + e.getMessage())
                    .build();
        }

    }
}
