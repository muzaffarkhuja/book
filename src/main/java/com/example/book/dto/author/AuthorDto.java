package com.example.book.dto.author;

import com.example.book.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDto implements BaseDTO {
    private Integer id;
    private String FullName;
}
