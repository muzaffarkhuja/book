package com.example.book.service.mapper;

import com.example.book.dto.city.CityCreateDto;
import com.example.book.dto.city.CityDto;
import com.example.book.model.City;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CityMapper extends CommonMapper<CityDto, CityCreateDto, CityDto, City> {
    @Override
    City update(CityDto cityDto, @MappingTarget City city);
}
