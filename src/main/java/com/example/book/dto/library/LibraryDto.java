package com.example.book.dto.library;

import com.example.book.dto.city.CityDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LibraryDto {
    private Integer id;
    private String name;
    private CityDto city;
}
