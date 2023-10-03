package com.example.book.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "searches")
@Getter
@Setter
public class Searches {
    @Id
    private String id;
    private String[] keywords;
    private int bookCount;
    private LocalDateTime time;

    public Searches() {
        this.time = LocalDateTime.now();
    }
    public Searches(String[] keywords, int bookCount) {
        this.keywords = keywords;
        this.bookCount = bookCount;
        this.time = LocalDateTime.now();
    }
}
