package com.example.book.service;

import com.example.book.dto.author.AuthorCreateDto;
import com.example.book.dto.author.AuthorDto;
import com.example.book.repository.AuthorRepository;
import com.example.book.service.mapper.AuthorMapper;

public abstract class AuthorService extends AbstractService<AuthorMapper, AuthorRepository,AuthorDto, AuthorCreateDto, AuthorDto> {

    protected AuthorService(AuthorMapper mapper, AuthorRepository repository) {
        super(mapper, repository);
    }
}
