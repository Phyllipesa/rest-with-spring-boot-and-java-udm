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

  @GetMapping(
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<List<PersonVO>> findAll() {
    return ResponseEntity.ok(service.findAll());
  }

  @GetMapping(
      value = "/{id}",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<PersonVO> findById(@PathVariable(value = "id") Long id) {
    return ResponseEntity.ok(service.findById(id));
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<PersonVO> create(@RequestBody PersonVO person) {
    return ResponseEntity.status(201).body(service.create(person));
  }

  @PutMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<PersonVO> update(@RequestBody PersonVO person) {
    return ResponseEntity.status(200).body(service.update(person));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
