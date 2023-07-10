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
public class City {
    @Id
    @GeneratedValue(generator = "CityIdSeq")
    @SequenceGenerator(name = "CityIdSeq", sequenceName = "city_id_seq", allocationSize = 1)
    private Integer id;
    @Column(unique = true)
    private String name;
}
