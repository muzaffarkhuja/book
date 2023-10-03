package com.example.book;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@OpenAPIDefinition
@EnableMongoRepositories
public class BooksApplication {
    public static void main(String[] args) {

        SpringApplication.run(BooksApplication.class, args);
    }

}
