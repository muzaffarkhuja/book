package com.example.book.service.mapper;

import com.example.book.dto.user.UserCreateDto;
import com.example.book.dto.user.UserDto;
import com.example.book.dto.user.UserUpdateDto;
import com.example.book.model.AuthUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public abstract class UserMapper implements CommonMapper<UserDto, UserCreateDto, UserUpdateDto, AuthUser> {

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Override
    @Mapping(target = "password", expression = "java(passwordEncoder.encode(dto.getPassword()))")
    public abstract AuthUser toEntity(UserCreateDto dto);
}
