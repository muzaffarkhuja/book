package com.example.book.repository;

import com.example.book.model.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AuthUser, Integer>, BaseRepository {

    @Query(value = "from AuthUser u where u.username=:username")
    Optional<AuthUser> findByUsername(String username);
}
