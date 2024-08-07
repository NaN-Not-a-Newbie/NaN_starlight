package com.nan.boilerplate.springboot.repository;

import com.nan.boilerplate.springboot.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

}
