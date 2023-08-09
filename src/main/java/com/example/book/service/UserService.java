package com.example.book.service;

import com.example.book.dto.ResponseDto;
import com.example.book.dto.user.LoginDto;
import com.example.book.dto.user.UserCreateDto;
import com.example.book.dto.user.UserDto;
import com.example.book.dto.user.UserUpdateDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService, GenericService<UserCreateDto, UserUpdateDto, UserDto> {

    ResponseDto<String> login(LoginDto loginDto);
}
