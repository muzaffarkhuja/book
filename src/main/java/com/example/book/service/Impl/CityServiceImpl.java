package com.example.book.service.Impl;

import com.example.book.dto.ResponseDto;
import com.example.book.dto.city.CityCreateDto;
import com.example.book.dto.city.CityDto;
import com.example.book.model.City;
import com.example.book.repository.CityRepository;
import com.example.book.service.CityService;
import com.example.book.service.mapper.CityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.book.service.validator.AppStatusCodes.*;
import static com.example.book.service.validator.AppStatusMessages.*;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {
    private final CityMapper cityMapper;
    private final CityRepository cityRepository;

    @Override
    public ResponseDto<CityDto> add(CityCreateDto cityDto) {
        try{
            Optional<City> byName = cityRepository.findByName(cityDto.getName());
            if(byName.isPresent()){
                return ResponseDto.<CityDto>builder()
                        .code(DUPLICATE_ERROR_CODE)
                        .message("City already exists")
                        .build();
            }
            City city = cityMapper.toEntity(cityDto);
            city = cityRepository.save(city);

            return ResponseDto.<CityDto>builder()
                    .message(OK)
                    .success(true)
                    .data(cityMapper.toDto(city))
                    .build();
        } catch (Exception e){
            return ResponseDto.<CityDto>builder()
                    .code(DATABASE_ERROR_CODE)
                    .message(DATABASE_ERROR + " -> This city already exists")
                    .build();
        }
    }

    @Override
    public ResponseDto<CityDto> getById(Integer id) {
        return cityRepository.findById(id)
                .map(city -> ResponseDto.<CityDto>builder()
                        .success(true)
                        .message(OK)
                        .data(cityMapper.toDto(city))
                        .build())
                .orElse(ResponseDto.<CityDto>builder()
                        .code(NOT_FOUND_ERROR_CODE)
                        .message(NOT_FOUND)
                        .build());
    }

    @Override
    public ResponseDto<List<CityDto>> getAll() {
        try{
            return ResponseDto.<List<CityDto>>builder()
                    .success(true)
                    .message(OK)
                    .data(cityRepository.findAll().stream().map(cityMapper::toDto).toList())
                    .build();
        } catch (Exception e){
            return ResponseDto.<List<CityDto>>builder()
                    .code(DATABASE_ERROR_CODE)
                    .message(DATABASE_ERROR + " -> " + e.getMessage())
                    .build();
        }
    }

    @Override
    public ResponseDto<CityDto> update(CityDto cityDto) {
        if(cityDto.getId() == null){
            return ResponseDto.<CityDto>builder()
                    .code(VALIDATION_ERROR_CODE)
                    .message(NULL_VALUE)
                    .build();
        }

        Optional<City> optional = cityRepository.findById(cityDto.getId());

        if(optional.isEmpty()){
            return ResponseDto.<CityDto>builder()
                    .code(NOT_FOUND_ERROR_CODE)
                    .message(NOT_FOUND)
                    .build();
        }

        City update = cityMapper.update(cityDto, optional.get());
        update = cityRepository.save(update);

        return ResponseDto.<CityDto>builder()
                .success(true)
                .message(OK)
                .data(cityMapper.toDto(update))
                .build();
    }

    @Override
    public ResponseDto<Void> delete(Integer id) {
        try{
            if(cityRepository.findById(id).isEmpty()){
                return ResponseDto.<Void>builder()
                        .code(NOT_FOUND_ERROR_CODE)
                        .message(NOT_FOUND)
                        .build();
            }
            cityRepository.deleteById(id);

            return ResponseDto.<Void>builder()
                    .success(true)
                    .message(OK)
                    .build();
        } catch (Exception e){
            return ResponseDto.<Void>builder()
                    .code(DATABASE_ERROR_CODE)
                    .message(DATABASE_ERROR + " -> " + e.getMessage())
                    .build();
        }
    }
}
