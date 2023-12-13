package com.phyllipesa.erudio.controllers;

import com.phyllipesa.erudio.data.vo.v1.PersonVO;
import com.phyllipesa.erudio.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/person")
public class PersonController {

  @Autowired
  PersonService service;

  @GetMapping
  public List<PersonVO> findAll() {
    return service.findAll();
  }

  @GetMapping("/{id}")
  public PersonVO findById(@PathVariable(value = "id") Long id) {
    return service.findById(id);
  }

  @PostMapping
  public PersonVO create(@RequestBody PersonVO person) {
    return service.create(person);
  }

  @PutMapping
  public PersonVO update(@RequestBody PersonVO person) {
    return service.update(person);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
