package com.phyllipesa.udm.repositories;

import com.phyllipesa.udm.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
