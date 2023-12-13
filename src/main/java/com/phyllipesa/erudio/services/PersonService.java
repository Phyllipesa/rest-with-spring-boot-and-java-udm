package com.phyllipesa.erudio.services;

import com.phyllipesa.erudio.data.vo.v1.PersonVO;
import com.phyllipesa.erudio.mapper.EntityMapper;
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

  @Autowired
  EntityMapper entityMapper;

  public List<PersonVO> findAll() {
    logger.info("Finding all people!");
    List<PersonVO> list = entityMapper.parseListObject(personRepository.findAll(), PersonVO.class);
    return list;
  }

  public PersonVO findById(Long id) {
    logger.info("Finding a person!");
    PersonVO person = entityMapper.parseObject(personRepository.findById(id), PersonVO.class);
    return person;
  }

  public PersonVO create(PersonVO personVO) {
    logger.info("Creating one person!");
    Person person = entityMapper.parseObject(personVO, Person.class);
    PersonVO newPerson = entityMapper.parseObject(personRepository.save(person), PersonVO.class);
    return  newPerson;
  }

  public PersonVO update(PersonVO personVO) {
    logger.info("Update one person!");

    Person entity = personRepository.findById(personVO.getKey()).orElseThrow();

    entity.setFirstName(personVO.getFirstName());
    entity.setLastName(personVO.getLastName());
    entity.setAddress(personVO.getAddress());
    entity.setGender(personVO.getGender());

    PersonVO vo = entityMapper.parseObject(personRepository.save(entity), PersonVO.class);
    return  vo;
  }

  public void delete(Long id) {
    logger.info("Deleting a person!");
    Person entity = personRepository.findById(id).orElseThrow();
    personRepository.delete(entity);
  }
}
