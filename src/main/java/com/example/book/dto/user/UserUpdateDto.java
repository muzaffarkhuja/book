package com.example.book.dto.user;

import com.example.book.dto.BaseDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDto implements BaseDTO {
    private Integer id;
    private String fullName;
    private String username;
    private String password;
    private String role;
}
