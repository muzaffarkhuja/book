package com.example.book.dto.searchs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchsDto {
    private Integer id;
    private String[] keywords;
    private int bookCount;
    private LocalDateTime time;
}
