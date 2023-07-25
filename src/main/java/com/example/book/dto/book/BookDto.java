package com.example.book.dto.book;

import com.example.book.dto.author.AuthorDto;
import com.example.book.dto.library.LibraryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private Integer id;
    private String name;
    private AuthorDto author;
    private LibraryDto library;
    private Integer size;
    private String[] keywords;
    private int searchedCount;
    private String theme;
}
