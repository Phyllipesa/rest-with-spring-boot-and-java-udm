package com.phyllipesa.udm.integrationTests.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phyllipesa.udm.integrationTests.testcontainers.AbstractIntegrationTest;
import com.phyllipesa.udm.models.Person;
import com.phyllipesa.udm.repositories.PersonRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonRepositoryTest  extends AbstractIntegrationTest {

  @Autowired
  PersonRepository repository;

  private static Person person;

  @BeforeAll
  public static void setup() {
    person = new Person();
  }

  @Test
  @Order(0)
  public void testFindByName() throws JsonProcessingException {
    Pageable pageable = PageRequest.of(0, 3, Sort.by(Sort.Direction.ASC, "firstName"));
    person = repository.findPersonsByName("ayr", pageable).getContent().get(0);

    assertNotNull(person.getId());
    assertNotNull(person.getFirstName());
    assertNotNull(person.getLastName());
    assertNotNull(person.getAddress());
    assertNotNull(person.getGender());
    assertNotNull(person.getEnabled());

    assertTrue(person.getEnabled());

    assertEquals(1, person.getId());
    assertEquals("Ayrton", person.getFirstName());
    assertEquals("Senna", person.getLastName());
    assertEquals("São Paulo", person.getAddress());
    assertEquals("Male", person.getGender());
  }

  @Test
  @Order(1)
  public void disablePerson() throws JsonProcessingException {
    repository.disablePerson(person.getId());

    Pageable pageable = PageRequest.of(0, 3, Sort.by(Sort.Direction.ASC, "id"));
    person = repository.findPersonsByName("ayr", pageable).getContent().get(0);

    assertNotNull(person.getId());
    assertNotNull(person.getFirstName());
    assertNotNull(person.getLastName());
    assertNotNull(person.getAddress());
    assertNotNull(person.getGender());
    assertNotNull(person.getEnabled());

    assertFalse(person.getEnabled());

    assertEquals(1, person.getId());
    assertEquals("Ayrton", person.getFirstName());
    assertEquals("Senna", person.getLastName());
    assertEquals("São Paulo", person.getAddress());
    assertEquals("Male", person.getGender());
  }
}
