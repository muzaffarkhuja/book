package com.example.book.controllers;

import com.example.book.dto.ResponseDto;
import com.example.book.dto.library.LibraryCreateDto;
import com.example.book.dto.library.LibraryDto;
import com.example.book.dto.library.LibraryUpdateDto;
import com.example.book.service.Impl.LibraryServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("library")
@SecurityRequirement(name = "Authorization")
@RequiredArgsConstructor
public class LibraryController {
    private final LibraryServiceImpl libraryService;

    @Operation(
            method = "Add new Library",
            description = "Need to send LibraryDto(String name, Integer cityId) " +
                    "to this endpoint to create new Library"
    )
    @PostMapping("add")
    public ResponseDto<LibraryDto> add(@RequestBody LibraryCreateDto libraryDto) {
        return libraryService.add(libraryDto);
    }

    @GetMapping("{id}")
    public ResponseDto<LibraryDto> getById(@PathVariable Integer id) {
        return libraryService.getById(id);
    }

    @Operation(
            description = "Getting List of Libraries"
    )
    @GetMapping()
    public ResponseDto<List<LibraryDto>> getAll() {
        return libraryService.getAll();
    }

    @Operation(
            method = "Update Library with id",
            description = "Need to send LibraryDto(Integer id, String name, Integer cityId) " +
                    "to update Library. You should send a Library's id which you want to update and params which you want update. Example{id:1, name:New Name} in this example " +
                    "Library's name which with id 1 will be update to New Name"
    )
    @PatchMapping("update")
    public ResponseDto<LibraryDto> update(@RequestBody LibraryUpdateDto libraryDto) {
        return libraryService.update(libraryDto);
    }

    @DeleteMapping("delete")
    public ResponseDto<Void> delete(@RequestParam Integer id) {
        return libraryService.delete(id);
    }
}
