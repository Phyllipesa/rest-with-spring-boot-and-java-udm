package com.phyllipesa.erudio.integrationTests.controller;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.type.TypeReference;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.specification.RequestSpecification;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.phyllipesa.erudio.integrationTests.vo.AccountCredentialsVO;
import com.phyllipesa.erudio.integrationTests.vo.TokenVO;
import com.phyllipesa.erudio.configs.TestConfigs;
import com.phyllipesa.erudio.integrationTests.testcontainers.AbstractIntegrationTest;
import com.phyllipesa.erudio.integrationTests.vo.PersonVO;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerJsonTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;
	private static ObjectMapper objectMapper;
	private static PersonVO person;

  private void mockPerson() {
    person.setFirstName("Nelson");
    person.setLastName("Piquet");
    person.setAddress("Brasilia - DF, Brasil");
    person.setGender("Male");
  }

	@BeforeAll
	public static void setup() {
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		person = new PersonVO();
	}

  @Test
  @Order(0)
  public void authorization() throws JsonProcessingException {
    AccountCredentialsVO user = new AccountCredentialsVO("phyllipe", "admin123");

    var accessToken =
      given()
        .basePath("/auth/signin")
          .port(TestConfigs.SERVER_PORT)
          .contentType(TestConfigs.CONTENT_TYPE_JSON)
        .body(user)
          .when()
        .post()
          .then()
            .statusCode(200)
              .extract()
              .body()
                .as(TokenVO.class)
              .getAccessToken();

    specification = new RequestSpecBuilder()
      .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
      .setBasePath("/api/person/v1")
      .setPort(TestConfigs.SERVER_PORT)
        .addFilter(new RequestLoggingFilter(LogDetail.ALL))
        .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
      .build();
  }

	@Test
	@Order(1)
	public void testCreate() throws JsonProcessingException {
		mockPerson();

		var content =
      given()
        .spec(specification)
        .contentType(TestConfigs.CONTENT_TYPE_JSON)
          .body(person)
        .when()
          .post()
        .then()
          .statusCode(201)
        .extract()
          .body()
            .asString();

    PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
    person = persistedPerson;

    assertNotNull(persistedPerson);

    assertNotNull(persistedPerson.getId());
    assertNotNull(persistedPerson.getFirstName());
    assertNotNull(persistedPerson.getLastName());
    assertNotNull(persistedPerson.getAddress());
    assertNotNull(persistedPerson.getGender());

		assertTrue(persistedPerson.getId() > 0);

    assertEquals("Nelson", persistedPerson.getFirstName());
    assertEquals("Piquet", persistedPerson.getLastName());
    assertEquals("Brasilia - DF, Brasil", persistedPerson.getAddress());
    assertEquals("Male", persistedPerson.getGender());
	}

  @Test
  @Order(2)
  public void testUpdate() throws JsonProcessingException {
    person.setLastName("Piquet Souto Maior");

    var content =
        given()
            .spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .body(person)
            .when()
            .post()
            .then()
            .statusCode(201)
            .extract()
            .body()
            .asString();

    PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
    person = persistedPerson;

    assertNotNull(persistedPerson);

    assertNotNull(persistedPerson.getId());
    assertNotNull(persistedPerson.getFirstName());
    assertNotNull(persistedPerson.getLastName());
    assertNotNull(persistedPerson.getAddress());
    assertNotNull(persistedPerson.getGender());

    assertEquals(person.getId(), persistedPerson.getId());

    assertEquals("Nelson", persistedPerson.getFirstName());
    assertEquals("Piquet Souto Maior", persistedPerson.getLastName());
    assertEquals("Brasilia - DF, Brasil", persistedPerson.getAddress());
    assertEquals("Male", persistedPerson.getGender());
  }

  @Test
  @Order(3)
  public void testFindById() throws JsonProcessingException {
    var content =
      given().spec(specification)
        .contentType(TestConfigs.CONTENT_TYPE_JSON)
        .pathParam("id", person.getId())
        .when()
        .get("{id}")
      .then()
        .statusCode(200)
          .extract()
          .body()
            .asString();

    PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
    person = persistedPerson;

    assertNotNull(persistedPerson);

    assertNotNull(persistedPerson.getId());
    assertNotNull(persistedPerson.getFirstName());
    assertNotNull(persistedPerson.getLastName());
    assertNotNull(persistedPerson.getAddress());
    assertNotNull(persistedPerson.getGender());

    assertEquals(person.getId(), persistedPerson.getId());

    assertEquals("Nelson", persistedPerson.getFirstName());
    assertEquals("Piquet Souto Maior", persistedPerson.getLastName());
    assertEquals("Brasilia - DF, Brasil", persistedPerson.getAddress());
    assertEquals("Male", persistedPerson.getGender());
  }

  @Test
  @Order(4)
  public void testDelete() throws JsonProcessingException {
      given().spec(specification)
          .contentType(TestConfigs.CONTENT_TYPE_JSON)
          .pathParam("id", person.getId())
        .when()
          .delete("{id}")
        .then()
          .statusCode(204);
  }

  @Test
  @Order(5)
  public void testFindAll() throws JsonProcessingException {

    var content =
      given()
          .spec(specification)
          .contentType(TestConfigs.CONTENT_TYPE_JSON)
        .when()
          .get()
        .then()
          .statusCode(200)
            .extract()
              .body()
                .asString();

    List<PersonVO> people = objectMapper.readValue(content, new TypeReference<List<PersonVO>>() {});
    PersonVO foundPersonOne = people.get(0);

    assertNotNull(foundPersonOne.getId());
    assertNotNull(foundPersonOne.getFirstName());
    assertNotNull(foundPersonOne.getLastName());
    assertNotNull(foundPersonOne.getAddress());
    assertNotNull(foundPersonOne.getGender());

    assertEquals(1, foundPersonOne.getId());

    assertEquals("Ayrton", foundPersonOne.getFirstName());
    assertEquals("Senna", foundPersonOne.getLastName());
    assertEquals("São Paulo", foundPersonOne.getAddress());
    assertEquals("Male", foundPersonOne.getGender());

    PersonVO foundPersonSix = people.get(5);

    assertNotNull(foundPersonSix.getId());
    assertNotNull(foundPersonSix.getFirstName());
    assertNotNull(foundPersonSix.getLastName());
    assertNotNull(foundPersonSix.getAddress());
    assertNotNull(foundPersonSix.getGender());

    assertEquals(6, foundPersonSix.getId());

    assertEquals("Nelson", foundPersonSix.getFirstName());
    assertEquals("Mandela", foundPersonSix.getLastName());
    assertEquals("Mvezo - South Africa", foundPersonSix.getAddress());
    assertEquals("Male", foundPersonSix.getGender());
  }

  @Test
  @Order(6)
  public void testFindAllWithoutToken() throws JsonProcessingException {

    RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
      .setBasePath("/api/person/v1")
      .setPort(TestConfigs.SERVER_PORT)
        .addFilter(new RequestLoggingFilter(LogDetail.ALL))
        .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
      .build();

    given()
      .spec(specificationWithoutToken)
        .contentType(TestConfigs.CONTENT_TYPE_JSON)
      .when()
        .get()
      .then()
        .statusCode(403);
  }
}
