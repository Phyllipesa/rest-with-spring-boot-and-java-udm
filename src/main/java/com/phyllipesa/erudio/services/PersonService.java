package com.phyllipesa.erudio.services;

import com.phyllipesa.erudio.models.Person;
import com.phyllipesa.erudio.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class PersonService {

  private Logger logger = Logger.getLogger(PersonService.class.getName());

  @Autowired
  PersonRepository personRepository;

  public List<Person> findAll() {
    logger.info("Findinf all people!");
    return personRepository.findAll();
  }
}
