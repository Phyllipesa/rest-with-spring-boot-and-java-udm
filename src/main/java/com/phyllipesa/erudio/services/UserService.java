package com.phyllipesa.erudio.services;

import com.phyllipesa.erudio.controllers.PersonController;
import com.phyllipesa.erudio.data.vo.v1.PersonVO;
import com.phyllipesa.erudio.exceptions.RequiredObjectIsNullException;
import com.phyllipesa.erudio.exceptions.ResourceNotFoundException;
import com.phyllipesa.erudio.mapper.EntityMapper;
import com.phyllipesa.erudio.models.Person;
import com.phyllipesa.erudio.models.User;
import com.phyllipesa.erudio.repositories.PersonRepository;
import com.phyllipesa.erudio.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class UserService implements UserDetailsService {

  private Logger logger = Logger.getLogger(UserService.class.getName());

  @Autowired
  UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    logger.info("Finding one user by name " + username + "!");
    User user = userRepository.findByUsername(username);

    if (user != null) {
      return user;
    }
    else {
      throw new UsernameNotFoundException("Username " + username + " not found!");
    }
  }
}
