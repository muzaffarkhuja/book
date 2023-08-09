package com.example.book.controllers;

import com.example.book.dto.ResponseDto;
import com.example.book.dto.book.BookCreateDto;
import com.example.book.dto.book.BookDto;
import com.example.book.dto.book.BookUpdateDto;
import com.example.book.service.Impl.BookServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("book-controller")
@SecurityRequirement(name = "Authorization")
@RequiredArgsConstructor
public class BookController {
    private final BookServiceImpl bookService;

    @Operation(
            method = "Add new book",
            description = "Need to send BookDto(String name, Integer authorId, Integer libraryId, Integer size, String[] keyword, String theme) " +
                    "to this endpoint to create new book"
    )
    @PostMapping("/add")
    public ResponseDto<BookDto> add(BookCreateDto bookDto){
        return  bookService.add(bookDto);
    }

    @GetMapping("{id}")
    public ResponseDto<BookDto> getById(@PathVariable Integer id){
        return bookService.getById(id);
    }

    @Operation(
            method = "Get All books",
            description = "Need to send Params to search books, and you can add page and size. " +
                    "Params to search are -> (Integer id, String name, Integer authorId, Integer libraryId, Integer minSize, Integer maxSize, String theme)"
    )
    @GetMapping()
    public ResponseDto<Page<BookDto>> getAll(@RequestParam Map<String, String> map){
        return bookService.getAll(map);
    }

    @GetMapping("by-keyword")
    public ResponseDto<List<BookDto>> getByKeyword(@RequestParam String[] keywords){
        ResponseDto<List<BookDto>> response = bookService.getByKeyword(keywords);
        response.setMessage(response.getMessage() + ", Thread -> " + Thread.currentThread().getName());
        return response;
    }

    @Operation(
            method = "Update book with id",
            description = "Need to send BookDto(Integer id, String name, Integer authorId, Integer libraryId, Integer minSize, Integer maxSize, String keyword, String theme) " +
                    "to update book. You should send a book's id which you want to update and params which you want update. Example{id:1, name:New Name} in this example " +
                    "book's name which with id 1 will be update to New Name"
    )
    @PatchMapping("update")
    public ResponseDto<BookDto> update(BookUpdateDto bookDto){
        return bookService.update(bookDto);
    }

    @DeleteMapping("delete/{id}")
    public ResponseDto<Void> delete(@PathVariable Integer id){
        return bookService.delete(id);
    }
}
