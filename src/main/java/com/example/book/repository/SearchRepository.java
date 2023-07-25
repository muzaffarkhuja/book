package com.example.book.repository;

import com.example.book.dto.searchs.MostSearchedDTO;
import com.example.book.model.Searchs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchRepository extends JpaRepository<Searchs, Integer>, BaseRepository {
    @Query(value = "SELECT new com.example.book.dto.searchs.MostSearchedDTO(s.keywords, COUNT(s)) FROM Searchs s GROUP BY s.keywords ORDER BY COUNT(s) DESC LIMIT 1")
    MostSearchedDTO findMostSearched();
}
