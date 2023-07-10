package com.example.book.repository;

import com.example.book.enums.Theme;
import com.example.book.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findByAuthorId(Integer id);

    Optional<Book> findByName(String name);

    @Query(value = """
            from Book b where b.id=coalesce(:id, b.id) and\s
             b.name ilike :name and\s
             b.author.FullName ilike :author and\s
             b.library.name ilike :library and\s
             b.size >= coalesce(:minSize, b.size) and\s
             b.size <= coalesce(:maxSize, b.size) and\s
             b.keyword ilike :keyword and\s
             b.library.city.name ilike :city and
            b.theme = coalesce(:theme, b.theme)\s
            """)
    Page<Book> universalSearch(Pageable pageRequest, Integer id, String name,
                               String author, String library, Integer minSize,
                               Integer maxSize, String keyword, Theme theme, String city);
}
