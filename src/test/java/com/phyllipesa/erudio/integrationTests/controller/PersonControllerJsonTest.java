package com.phyllipesa.erudio.integrationTests.controller;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.phyllipesa.erudio.configs.TestConfigs;
import com.phyllipesa.erudio.integrationTests.testcontainers.AbstractIntegrationTest;
import com.phyllipesa.erudio.integrationTests.vo.PersonVO;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerJsonTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;
	private static ObjectMapper objectMapper;
	private static PersonVO person;

	@BeforeAll
	public static void setup() {
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		person = new PersonVO();
	}
	@Test
	@Order(1)
	public void testCreate() throws JsonProcessingException {
		mockPerson();

		specification = new RequestSpecBuilder()
      .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_PHYLLIPESA)
      .setBasePath("/api/person")
      .setPort(TestConfigs.SERVER_PORT)
        .addFilter(new RequestLoggingFilter(LogDetail.ALL))
        .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
      .build();

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

    PersonVO createPerson = objectMapper.readValue(content, PersonVO.class);
    person = createPerson;

    assertNotNull(createPerson);

    assertNotNull(createPerson.getId());
    assertNotNull(createPerson.getFirstName());
    assertNotNull(createPerson.getLastName());
    assertNotNull(createPerson.getAddress());
    assertNotNull(createPerson.getGender());

		assertTrue(createPerson.getId() > 0);

    assertEquals("Richard", createPerson.getFirstName());
    assertEquals("Stallman", createPerson.getLastName());
    assertEquals("New York City, New York, US", createPerson.getAddress());
    assertEquals("Male", createPerson.getGender());
	}

  @Test
  @Order(2)
  public void testCreateWithWrongOrigin() throws JsonProcessingException {
    mockPerson();

    specification = new RequestSpecBuilder()
      .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_SEMPSA)
      .setBasePath("/api/person")
      .setPort(TestConfigs.SERVER_PORT)
        .addFilter(new RequestLoggingFilter(LogDetail.ALL))
        .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
      .build();

    var content =
      given()
        .spec(specification)
        .contentType(TestConfigs.CONTENT_TYPE_JSON)
          .body(person)
        .when()
          .post()
        .then()
          .statusCode(403)
        .extract()
            .body()
              .asString();

    assertNotNull(content);
    assertEquals("Invalid CORS request", content);
  }

	private void mockPerson() {
		person.setFirstName("Richard");
		person.setLastName("Stallman");
		person.setAddress("New York City, New York, US");
		person.setGender("Male");
	}
}
