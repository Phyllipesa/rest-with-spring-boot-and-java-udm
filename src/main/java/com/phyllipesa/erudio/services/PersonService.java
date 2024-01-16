package com.phyllipesa.erudio.services;

import com.phyllipesa.erudio.controllers.PersonController;
import com.phyllipesa.erudio.data.vo.v1.PersonVO;
import com.phyllipesa.erudio.exceptions.RequiredObjectIsNullException;
import com.phyllipesa.erudio.exceptions.ResourceNotFoundException;
import com.phyllipesa.erudio.mapper.EntityMapper;
import com.phyllipesa.erudio.models.Person;
import com.phyllipesa.erudio.repositories.PersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

  public Page<PersonVO> findAll(Pageable pageable) {
    logger.info("Finding all people!");

    var personPage = personRepository.findAll(pageable);
    var personVosPage = personPage.map( p -> entityMapper.parseObject(p, PersonVO.class));
    personVosPage.map(
        p -> p.add(
            linkTo(methodOn(PersonController.class)
                .findById(p.getKey())).withSelfRel()));

    return personVosPage;
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

  @Transactional
  public PersonVO disablePerson(Long id) {
    logger.info("Disabling a person!");
    personRepository.disablePerson(id);

    Person p = personRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("No record found for this ID!"));
    PersonVO person = entityMapper.parseObject(p, PersonVO.class);
    person.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
    return person;
  }

  public void delete(Long id) {
    logger.info("Deleting a person!");
    Person entity = personRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));;
    personRepository.delete(entity);
  }
}
