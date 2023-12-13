package com.phyllipesa.erudio.services;

import com.phyllipesa.erudio.data.vo.v1.PersonVO;
import com.phyllipesa.erudio.mapper.EntityMapper;
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
}
