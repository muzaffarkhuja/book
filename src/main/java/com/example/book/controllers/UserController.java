package com.example.book.controllers;

import com.example.book.dto.ResponseDto;
import com.example.book.dto.user.LoginDto;
import com.example.book.dto.user.UserCreateDto;
import com.example.book.dto.user.UserDto;
import com.example.book.dto.user.UserUpdateDto;
import com.example.book.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
@SecurityRequirement(name = "Authorization")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("registration")
    public ResponseDto<UserDto> add(@RequestBody UserCreateDto userCreateDto) {
        return userService.add(userCreateDto);
    }

    @PostMapping("login-with-username")
    public ResponseDto<String> login(@RequestBody LoginDto loginDto) throws NoSuchMethodException {
        return userService.login(loginDto);
    }

    @GetMapping()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ResponseDto<List<UserDto>> getAll() {
        return userService.getAll();
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ResponseDto<UserDto> getById(@PathVariable Integer id) {
        return userService.getById(id);
    }

    @PatchMapping("update")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ResponseDto<UserDto> update(@RequestBody UserUpdateDto userUpdateDto) {
        return userService.update(userUpdateDto);
    }

    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_SUPER_ADMIN')")
    public ResponseDto<Void> delete(@PathVariable Integer id){
        return userService.delete(id);
    }
}