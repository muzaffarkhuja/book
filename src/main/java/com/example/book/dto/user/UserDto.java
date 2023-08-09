package com.example.book.dto.user;

import com.example.book.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements BaseDTO {
    private Integer id;
    private String fullName;
    private String username;
    private String password;
    private String role;
}
