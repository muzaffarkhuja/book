package com.example.book.model;

import com.example.book.enums.Theme;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String name;
    @ManyToOne
    private Author author;
    @ManyToOne
    private Library library;
    private Integer size;
    private String keyword;
    @Enumerated(EnumType.STRING)
    private Theme theme;

}
