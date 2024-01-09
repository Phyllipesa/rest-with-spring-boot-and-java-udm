package com.phyllipesa.erudio.integrationTests.controller.withYaml.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.restassured.mapper.ObjectMapper;
import io.restassured.mapper.ObjectMapperDeserializationContext;
import io.restassured.mapper.ObjectMapperSerializationContext;

public class YMLMapper implements ObjectMapper {

  private final com.fasterxml.jackson.databind.ObjectMapper objectMapper;
  protected TypeFactory typeFactory;

  public YMLMapper(TypeFactory typeFactory) {
    this.objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    typeFactory = TypeFactory.defaultInstance();
  }

  @SuppressWarnings("rawtypes")
  @Override
  public Object deserialize(ObjectMapperDeserializationContext context) {
    try {
      String dataToDeserialize = context.getDataToDeserialize().asString();
      Class type = (Class) context.getType();

      return objectMapper.readValue(dataToDeserialize, typeFactory.constructType(type));
    }
    catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public Object serialize(ObjectMapperSerializationContext context) {
    try {
      return objectMapper.writeValueAsString(context.getObjectToSerialize());
    }
    catch (com.fasterxml.jackson.core.JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }
}