package com.phyllipesa.erudio.repositories;

import com.phyllipesa.erudio.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
