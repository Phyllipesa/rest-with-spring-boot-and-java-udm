package com.phyllipesa.udm.util;

import com.phyllipesa.udm.data.vo.v1.BookVO;
import com.phyllipesa.udm.data.vo.v1.PersonVO;
import com.phyllipesa.udm.models.Book;
import com.phyllipesa.udm.models.Person;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ModelMapperConfig {

  @Bean
  public ModelMapper modelMapper() {
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.typeMap(Person.class, PersonVO.class)
        .addMappings(mapper -> {
          mapper.map(Person::getId, PersonVO::setKey);
        });
    modelMapper.typeMap(Book.class, BookVO.class)
        .addMappings(mapper -> {
          mapper.map(Book::getId, BookVO::setKey);
        });
    return modelMapper;
  }
}
