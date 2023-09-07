package com.example.book.controllers;

import com.example.book.dto.ResponseDto;
import com.example.book.dto.book.BookCreateDto;
import com.example.book.dto.book.BookDto;
import com.example.book.dto.book.BookUpdateDto;
import com.example.book.service.Impl.BookServiceImpl;
import com.example.book.service.fileService.BookExcelExporter;
import com.example.book.service.fileService.BookPdfExporter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("books")
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
        return bookService.getByKeyword(keywords);
    }

    @GetMapping("export/{type}")
    public void exportToExcel(HttpServletResponse response, @RequestParam String[] keywords, @PathVariable String type) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());

        if(type.equals("excel")){
            type = ".xlsx";
        } else {
            type = ".pdf";
        }
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=books_" + currentDateTime + type;
        response.setHeader(headerKey, headerValue);

        List<BookDto> list = bookService.getByKeyword(keywords).getData();
        if(type.equals(".xlsx")) {
            BookExcelExporter excelExporter = new BookExcelExporter(list);
            excelExporter.export(response);
        } else {
            BookPdfExporter pdfExporter = new BookPdfExporter(list, response);
            pdfExporter.export();
        }
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
