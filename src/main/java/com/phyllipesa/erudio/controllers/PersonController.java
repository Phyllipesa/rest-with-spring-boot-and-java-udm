package com.phyllipesa.erudio.controllers;

import com.phyllipesa.erudio.data.vo.v1.PersonVO;
import com.phyllipesa.erudio.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/person")
public class PersonController {

  @Autowired
  PersonService service;

  @GetMapping(
      produces = {
          com.phyllipesa.erudio.util.MediaType.APPLICATION_JSON,
          com.phyllipesa.erudio.util.MediaType.APPLICATION_XML
      }
  )
  public ResponseEntity<List<PersonVO>> findAll() {
    return ResponseEntity.ok(service.findAll());
  }

  @GetMapping(
      value = "/{id}",
      produces = {
          com.phyllipesa.erudio.util.MediaType.APPLICATION_JSON,
          com.phyllipesa.erudio.util.MediaType.APPLICATION_XML
      }
  )
  public ResponseEntity<PersonVO> findById(@PathVariable(value = "id") Long id) {
    return ResponseEntity.ok(service.findById(id));
  }

  @PostMapping(
      consumes = {
          com.phyllipesa.erudio.util.MediaType.APPLICATION_JSON,
          com.phyllipesa.erudio.util.MediaType.APPLICATION_XML
      },
      produces = {
          com.phyllipesa.erudio.util.MediaType.APPLICATION_JSON,
          com.phyllipesa.erudio.util.MediaType.APPLICATION_XML
      }
  )
  public ResponseEntity<PersonVO> create(@RequestBody PersonVO person) {
    return ResponseEntity.status(201).body(service.create(person));
  }

  @PutMapping(
      consumes = {
          com.phyllipesa.erudio.util.MediaType.APPLICATION_JSON,
          com.phyllipesa.erudio.util.MediaType.APPLICATION_XML
      },
      produces = {
          com.phyllipesa.erudio.util.MediaType.APPLICATION_JSON,
          com.phyllipesa.erudio.util.MediaType.APPLICATION_XML
      }
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
