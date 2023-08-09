package com.example.book.service.Impl;

import com.example.book.dto.ResponseDto;
import com.example.book.dto.user.LoginDto;
import com.example.book.dto.user.UserCreateDto;
import com.example.book.dto.user.UserDto;
import com.example.book.dto.user.UserUpdateDto;
import com.example.book.model.AuthUser;
import com.example.book.repository.UserRepository;
import com.example.book.security.JwtService;
import com.example.book.service.UserService;
import com.example.book.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.book.service.validator.AppStatusCodes.*;
import static com.example.book.service.validator.AppStatusMessages.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public AuthUser loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("user is not valid"));
    }

    @Override
    public ResponseDto<UserDto> add(UserCreateDto dto) {
        try{
            AuthUser entity = userMapper.toEntity(dto);
            entity = userRepository.save(entity);

            return ResponseDto.<UserDto>builder()
                    .success(true)
                    .message(OK)
                    .data(userMapper.toDto(entity))
                    .build();
        } catch (Exception e){
            return ResponseDto.<UserDto>builder()
                    .code(DATABASE_ERROR_CODE)
                    .message(DATABASE_ERROR + " -> " + e.getMessage())
                    .build();
        }
    }

    @Override
    public ResponseDto<UserDto> getById(Integer id) {
        return userRepository.findById(id)
                .map(entity -> ResponseDto.<UserDto>builder()
                        .success(true)
                        .message(OK)
                        .data(userMapper.toDto(entity))
                        .build())
                .orElse(ResponseDto.<UserDto>builder()
                        .message(NOT_FOUND)
                        .code(NOT_FOUND_ERROR_CODE)
                        .build());
    }

    @Override
    public ResponseDto<List<UserDto>> getAll() {
        try{
            return ResponseDto.<List<UserDto>>builder()
                    .success(true)
                    .message(OK)
                    .data(userRepository.findAll().stream().map(userMapper::toDto).toList())
                    .build();
        } catch (Exception e){
            return ResponseDto.<List<UserDto>>builder()
                    .code(DATABASE_ERROR_CODE)
                    .message(DATABASE_ERROR + " -> " + e.getMessage())
                    .build();
        }
    }

    @Override
    public ResponseDto<UserDto> update(UserUpdateDto dto) {
        if(dto.getId() == null){
            return ResponseDto.<UserDto>builder()
                    .code(VALIDATION_ERROR_CODE)
                    .message(NULL_VALUE)
                    .build();
        }

        Optional<AuthUser> optional = userRepository.findById(dto.getId());

        if(optional.isEmpty()){
            return ResponseDto.<UserDto>builder()
                    .code(NOT_FOUND_ERROR_CODE)
                    .message(NOT_FOUND)
                    .build();
        }

        AuthUser update = userMapper.update(dto, optional.get());

        update = userRepository.save(update);

        return ResponseDto.<UserDto>builder()
                .message(OK)
                .success(true)
                .data(userMapper.toDto(update))
                .build();
    }

    @Override
    public ResponseDto<Void> delete(Integer id) {
        try {
            if (userRepository.findById(id).isEmpty()){
                return ResponseDto.<Void>builder()
                        .code(NOT_FOUND_ERROR_CODE)
                        .message(NOT_FOUND)
                        .build();
            }

            userRepository.deleteById(id);
            return ResponseDto.<Void>builder()
                    .success(true)
                    .message(OK)
                    .build();
        } catch (Exception e){
            return ResponseDto.<Void>builder()
                    .code(DATABASE_ERROR_CODE)
                    .message(DATABASE_ERROR + " -> " + e.getMessage())
                    .build();
        }    }

    @Override
    public ResponseDto<String> login(LoginDto loginDto) {
        AuthUser authUser = loadUserByUsername(loginDto.getUsername());
        if (!passwordEncoder.matches(loginDto.getPassword(), authUser.getPassword())){
            return ResponseDto.<String>builder()
                    .code(VALIDATION_ERROR_CODE)
                    .message("Password is not correct")
                    .build();
        }

        return ResponseDto.<String>builder()
                .success(true)
                .message(OK)
                .data(jwtService.generateToken(authUser))
                .build();
    }
}
