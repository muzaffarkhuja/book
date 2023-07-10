package com.example.book.rest.thymeleaf;

import com.example.book.dto.ResponseDto;
import com.example.book.dto.book.BookCreateDto;
import com.example.book.dto.book.BookDto;
import com.example.book.dto.book.BookUpdateDto;
import com.example.book.service.Impl.BookServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("book")
@RequiredArgsConstructor
public class BookResourcesThymeleaf {
    private final BookServiceImpl bookService;

    @PostMapping("/add")
    public String add(@ModelAttribute BookCreateDto bookDto){
        ResponseDto<BookDto> add = bookService.add(bookDto);
        return "redirect:/book";
    }

    @GetMapping("{id}")
    public String getById(@PathVariable Integer id, Model model){
        model.addAttribute("book", List.of(bookService.getById(id).getData()));
        return "redirect:/book";
    }

    @GetMapping()
    public String getAll(@RequestParam Map<String, String> map, Model model){
        model.addAttribute("books", bookService.getAll(map).getData().stream().toList());
        return "books";
    }

    @GetMapping("update")
    public String update(@ModelAttribute BookUpdateDto bookDto){
        bookService.update(bookDto);
        return "redirect:/book";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable Integer id){
        bookService.delete(id);
        return "redirect:/book";
    }
}
