package com.example.book.dto.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookUpdateDto {
    private Integer id;
    private String name;
    private Integer authorId;
    private Integer libraryId;
    private Integer size;
    private String keyword;
    private String theme;
}
