package com.phyllipesa.erudio.controllers;

import com.phyllipesa.erudio.data.vo.v1.PersonVO;
import com.phyllipesa.erudio.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
