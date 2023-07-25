package com.example.book.rest;

import com.example.book.dto.ResponseDto;
import com.example.book.dto.searchs.MostSearchedDTO;
import com.example.book.dto.searchs.SearchsDto;
import com.example.book.service.Impl.SearchsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("searchs")
@RequiredArgsConstructor
public class SearchResources {
    private final SearchsServiceImpl searchsService;

    @GetMapping()
    public ResponseDto<List<SearchsDto>> getAll(){
        return searchsService.getAll();
    }

    @GetMapping("most-searched")
    public ResponseDto<MostSearchedDTO> getMostSearched(){
        return searchsService.getMostSearched();
    }
}
