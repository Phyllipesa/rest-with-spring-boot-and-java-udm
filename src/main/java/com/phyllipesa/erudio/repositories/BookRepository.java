package com.phyllipesa.erudio.repositories;

import com.phyllipesa.erudio.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
