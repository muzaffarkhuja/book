package com.example.book.rest;

import com.example.book.dto.ResponseDto;
import com.example.book.dto.author.AuthorCreateDto;
import com.example.book.dto.author.AuthorDto;
import com.example.book.service.Impl.AuthorServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("author")
@RequiredArgsConstructor
public class AuthorResources {
    private final AuthorServiceImpl authorService;

    @Operation(
            method = "Add new Author",
            description = "Need to send AuthorDto(String fullName) to create new Author "
    )
    @PostMapping("add")
    public ResponseDto<AuthorDto> add(@RequestBody AuthorCreateDto authorDto){
        return authorService.add(authorDto);
    }

    @GetMapping("{id}")
    public ResponseDto<AuthorDto> getById(@PathVariable Integer id){
        return authorService.getById(id);
    }

    @Operation(
            description = "Get List of Authors"
    )
    @GetMapping()
    public ResponseDto<List<AuthorDto>> getAll(){
        return authorService.getAll();
    }

    @Operation(
            method = "Update Author with id",
            description = "Need to send AuthorDto(Integer id, String fullName) " +
                    "to update Author. You should send a Author's id which you want to update and params which you want update. Example{id:1, fulName:New Name} in this example " +
                    "author's name which with id 1 will be update to New Name"
    )
    @PatchMapping("update")
    public ResponseDto<AuthorDto> update(@RequestBody AuthorDto authorDto){
        return authorService.update(authorDto);
    }

    @DeleteMapping("delete")
    public ResponseDto<Void> delete(@RequestParam Integer id) {
        return authorService.delete(id);
    }
}
