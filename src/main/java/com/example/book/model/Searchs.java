package com.example.book.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Searchs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "keywords", columnDefinition = "character varying[]")
    private String[] keywords;
    private int bookCount;
    private final LocalDateTime time;

    public Searchs() {
        this.time = LocalDateTime.now();
    }
    public Searchs(String[] keywords, int bookCount) {
        this.keywords = keywords;
        this.bookCount = bookCount;
        this.time = LocalDateTime.now();
    }
}
