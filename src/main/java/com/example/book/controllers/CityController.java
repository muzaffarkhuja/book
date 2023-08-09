package com.example.book.controllers;

import com.example.book.dto.ResponseDto;
import com.example.book.dto.city.CityCreateDto;
import com.example.book.dto.city.CityDto;
import com.example.book.service.Impl.CityServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("city")
@SecurityRequirement(name = "Authorization")
@RequiredArgsConstructor
public class CityController {
    private final CityServiceImpl cityService;

    @Operation(
            method = "Add new City",
            description = "Need to send CityDto(String name) " +
                    "to this endpoint to create new City"
    )
    @PostMapping("add")
    public ResponseDto<CityDto> add(@RequestBody CityCreateDto cityDto){
        return cityService.add(cityDto);
    }

    @GetMapping("{id}")
    public ResponseDto<CityDto> getById(@PathVariable Integer id){
        return cityService.getById(id);
    }

    @Operation(
            description = "Getting List of Cities"
    )
    @GetMapping()
    public ResponseDto<List<CityDto>> getAll(){
        return cityService.getAll();
    }

    @Operation(
            method = "Update city with id",
            description = "Need to send CityDto(Integer id, String name) " +
                    "to update City. You should send a City's id which you want to update and params which you want update. Example{id:1, name:New Name} in this example " +
                    "city's name which with id 1 will be update to New Name"
    )
    @PatchMapping("update")
    public ResponseDto<CityDto> update(@RequestBody CityDto cityDto){
        return cityService.update(cityDto);
    }

    @DeleteMapping("delete")
    public ResponseDto<Void> delete(@RequestParam Integer id) {
        return cityService.delete(id);
    }
}
