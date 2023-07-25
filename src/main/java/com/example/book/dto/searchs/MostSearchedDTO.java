package com.example.book.dto.searchs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class MostSearchedDTO {
    private String[] keywords;
    private Long count;

    public MostSearchedDTO(String[] keywords, Long count) {
        this.keywords = keywords;
        this.count = count;
    }
}
