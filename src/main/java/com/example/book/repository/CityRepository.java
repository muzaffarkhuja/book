package com.example.book.repository;

import com.example.book.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Integer>, BaseRepository {
    Optional<City> findByName(String name);
}
