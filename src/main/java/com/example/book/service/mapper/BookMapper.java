package com.example.book.service.mapper;

import com.example.book.dto.book.BookCreateDto;
import com.example.book.dto.book.BookDto;
import com.example.book.dto.book.BookUpdateDto;
import com.example.book.model.Book;
import com.example.book.repository.AuthorRepository;
import com.example.book.repository.LibraryRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", uses = {AuthorMapper.class, LibraryMapper.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class BookMapper implements CommonMapper<BookDto, BookCreateDto, BookUpdateDto, Book> {
    @Autowired
    protected AuthorRepository authorRepository;
    @Autowired
    protected LibraryRepository libraryRepository;

    @Override
    @Mapping(target = "author",expression = "java(bookDto.getAuthorId()!=null?authorRepository.findById(bookDto.getAuthorId()).orElse(book.getAuthor()):book.getAuthor())")
    @Mapping(target = "library",expression = "java(bookDto.getLibraryId()!=null?libraryRepository.findById(bookDto.getLibraryId()).orElse(book.getLibrary()):book.getLibrary())")
    public abstract Book update(BookUpdateDto bookDto, @MappingTarget Book book);

    @Override
    public abstract BookDto toDto(Book book);

    @Override
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "library", ignore = true)
    public abstract Book toEntity(BookCreateDto bookDto);
}
