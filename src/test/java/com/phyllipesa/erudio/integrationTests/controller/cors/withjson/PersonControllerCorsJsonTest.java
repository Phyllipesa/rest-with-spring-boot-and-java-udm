package com.phyllipesa.erudio.integrationTests.controller.cors.withjson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phyllipesa.erudio.configs.TestConfigs;
import com.phyllipesa.erudio.integrationTests.testcontainers.AbstractIntegrationTest;
import com.phyllipesa.erudio.integrationTests.vo.AccountCredentialsVO;
import com.phyllipesa.erudio.integrationTests.vo.PersonVO;
import com.phyllipesa.erudio.integrationTests.vo.TokenVO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerCorsJsonTest extends AbstractIntegrationTest {

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
          .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_PHYLLIPESA)
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
    assertNotNull(persistedPerson.getEnabled());

		assertTrue(persistedPerson.getId() > 0);
    assertTrue(persistedPerson.getEnabled());

    assertEquals("Richard", persistedPerson.getFirstName());
    assertEquals("Stallman", persistedPerson.getLastName());
    assertEquals("New York City, New York, US", persistedPerson.getAddress());
    assertEquals("Male", persistedPerson.getGender());
	}

  @Test
  @Order(2)
  public void testCreateWithWrongOrigin() throws JsonProcessingException {
    mockPerson();

    var content =
      given()
        .spec(specification)
        .contentType(TestConfigs.CONTENT_TYPE_JSON)
          .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_SEMPSA)
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

  @Test
  @Order(3)
  public void testFindById() throws JsonProcessingException {
    mockPerson();

    var content =
      given().spec(specification)
        .contentType(TestConfigs.CONTENT_TYPE_JSON)
        .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_PHYLLIPESA)
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

    assertNotNull(persistedPerson.getId());
    assertNotNull(persistedPerson.getFirstName());
    assertNotNull(persistedPerson.getLastName());
    assertNotNull(persistedPerson.getAddress());
    assertNotNull(persistedPerson.getGender());
    assertNotNull(persistedPerson.getEnabled());

    assertTrue(persistedPerson.getId() > 0);
    assertTrue(persistedPerson.getEnabled());

    assertEquals("Richard", persistedPerson.getFirstName());
    assertEquals("Stallman", persistedPerson.getLastName());
    assertEquals("New York City, New York, US", persistedPerson.getAddress());
    assertEquals("Male", persistedPerson.getGender());
  }

  @Test
  @Order(4)
  public void testFindByIdWithWrongOrigin() throws JsonProcessingException {
    mockPerson();

    var content =
        given()
          .spec(specification)
          .contentType(TestConfigs.CONTENT_TYPE_JSON)
          .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_SEMPSA)
          .pathParam("id", person.getId())
        .when()
          .get("{id}")
        .then()
          .statusCode(403)
        .extract()
          .body()
            .asString();

    assertNotNull(content);
    assertEquals("Invalid CORS request", content);
  }

  @Test
  @Order(5)
  public void testDelete() throws JsonProcessingException {
    given().spec(specification)
      .contentType(TestConfigs.CONTENT_TYPE_JSON)
      .pathParam("id", person.getId())
    .when()
      .delete("{id}")
      .then()
      .statusCode(204);
  }

  private void mockPerson() {
    person.setFirstName("Richard");
    person.setLastName("Stallman");
    person.setAddress("New York City, New York, US");
    person.setGender("Male");
    person.setEnabled(true);
  }
}
