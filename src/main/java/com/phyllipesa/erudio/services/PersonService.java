package com.phyllipesa.erudio.services;

import com.phyllipesa.erudio.controllers.PersonController;
import com.phyllipesa.erudio.data.vo.v1.PersonVO;
import com.phyllipesa.erudio.exceptions.RequiredObjectIsNullException;
import com.phyllipesa.erudio.exceptions.ResourceNotFoundException;
import com.phyllipesa.erudio.mapper.EntityMapper;
import com.phyllipesa.erudio.models.Person;
import com.phyllipesa.erudio.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonService {

  private Logger logger = Logger.getLogger(PersonService.class.getName());

  @Autowired
  PersonRepository personRepository;

  @Autowired
  EntityMapper entityMapper;

  public List<PersonVO> findAll() {
    logger.info("Finding all people!");
    List<PersonVO> list = entityMapper.parseListObject(personRepository.findAll(), PersonVO.class);
    list.forEach(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));
    return list;
  }

  public PersonVO findById(Long id) {
    logger.info("Finding a person!");
    Person p = personRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("No record found for this ID!"));
    PersonVO person = entityMapper.parseObject(p, PersonVO.class);
    person.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
    return person;
  }

  public PersonVO create(PersonVO personVO) {
    if (personVO == null) throw new RequiredObjectIsNullException();
    logger.info("Creating one person!");
    Person person = entityMapper.parseObject(personVO, Person.class);
    PersonVO newPerson = entityMapper.parseObject(personRepository.save(person), PersonVO.class);
    newPerson.add(linkTo(methodOn(PersonController.class).findById(newPerson.getKey())).withSelfRel());
    return  newPerson;
  }

  public PersonVO update(PersonVO personVO) {
    if (personVO == null) throw new RequiredObjectIsNullException();
    logger.info("Update one person!");

    Person entity = personRepository.findById(personVO.getKey())
        .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

    entity.setFirstName(personVO.getFirstName());
    entity.setLastName(personVO.getLastName());
    entity.setAddress(personVO.getAddress());
    entity.setGender(personVO.getGender());

    PersonVO vo = entityMapper.parseObject(personRepository.save(entity), PersonVO.class);
    vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
    return  vo;
  }

  public void delete(Long id) {
    logger.info("Deleting a person!");
    Person entity = personRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));;
    personRepository.delete(entity);
  }
}
