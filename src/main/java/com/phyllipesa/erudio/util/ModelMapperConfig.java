package com.phyllipesa.erudio.util;

import com.phyllipesa.erudio.data.vo.v1.PersonVO;
import com.phyllipesa.erudio.models.Person;
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
    return modelMapper;
  }
}
