package com.example.book.repository;

import com.example.book.model.Library;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LibraryRepository extends JpaRepository<Library, Integer> {
    Optional<Library> findByName(String name);
}
