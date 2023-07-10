package com.example.book.repository;

import com.example.book.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer>, BaseRepository {

    @Query(value = "select a from Author a where a.FullName = :fullName")
    Optional<Author> findByFullName(String fullName);
}
