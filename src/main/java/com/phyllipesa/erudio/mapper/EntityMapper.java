package com.phyllipesa.erudio.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EntityMapper {

  @Autowired
  ModelMapper modelMapper;

  public <Origin, Destiny> Destiny parseObject(Origin origin, Class<Destiny> destination) {
    return modelMapper.map(origin, destination);
  }

  public <Origin, Destiny>List<Destiny> parseListObject(List<Origin> originList, Class<Destiny> destinationList) {
    List<Destiny> destinationObjects = new ArrayList<Destiny>();

    for (Origin origin : originList) {
      destinationObjects.add(modelMapper.map(origin, destinationList));
    }
    return destinationObjects;
  }
}
