package com.example.book;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Driver;

@SpringBootApplication
@OpenAPIDefinition
public class BooksApplication {
	public static void main(String[] args) {

		SpringApplication.run(BooksApplication.class, args);
	}

}
