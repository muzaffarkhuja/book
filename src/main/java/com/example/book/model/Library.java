package com.example.book.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Library {
    @Id
    @GeneratedValue(generator = "LibraryIdSeq")
    @SequenceGenerator(name = "LibraryIdSeq", sequenceName = "library_id_seq", allocationSize = 1)
    private Integer id;
    @Column(unique = true)
    private String name;
    @ManyToOne
    private City city;
}