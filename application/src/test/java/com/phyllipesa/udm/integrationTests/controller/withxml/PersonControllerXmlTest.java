package com.phyllipesa.udm.integrationTests.controller.withxml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.phyllipesa.udm.configs.TestConfigs;
import com.phyllipesa.udm.integrationTests.testcontainers.AbstractIntegrationTest;
import com.phyllipesa.udm.integrationTests.vo.AccountCredentialsVO;
import com.phyllipesa.udm.integrationTests.vo.PersonVO;
import com.phyllipesa.udm.integrationTests.vo.TokenVO;
import com.phyllipesa.udm.integrationTests.vo.pagedmodels.PagedModelPerson;
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
public class PersonControllerXmlTest extends AbstractIntegrationTest {

  private static RequestSpecification specification;
  private static XmlMapper objectMapper;
  private static PersonVO person;

  private void mockPerson() {
    person.setFirstName("Nelson");
    person.setLastName("Piquet");
    person.setAddress("Brasilia - DF, Brasil");
    person.setGender("Male");
    person.setEnabled(true);
  }

  @BeforeAll
  public static void setup() {
    objectMapper = new XmlMapper();
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
            .contentType(TestConfigs.CONTENT_TYPE_XML)
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
            .contentType(TestConfigs.CONTENT_TYPE_XML)
            .accept(TestConfigs.CONTENT_TYPE_XML)
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
    assertNotNull(persistedPerson.getId());
    assertNotNull(persistedPerson.getFirstName());
    assertNotNull(persistedPerson.getLastName());
    assertNotNull(persistedPerson.getAddress());
    assertNotNull(persistedPerson.getGender());
    assertNotNull(persistedPerson.getEnabled());

    assertTrue(persistedPerson.getId() > 0);
    assertTrue(persistedPerson.getEnabled());

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
            .contentType(TestConfigs.CONTENT_TYPE_XML)
            .accept(TestConfigs.CONTENT_TYPE_XML)
            .body(person)
            .when()
            .put()
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

    assertEquals("Nelson", persistedPerson.getFirstName());
    assertEquals("Piquet Souto Maior", persistedPerson.getLastName());
    assertEquals("Brasilia - DF, Brasil", persistedPerson.getAddress());
    assertEquals("Male", persistedPerson.getGender());
  }

  @Test
  @Order(3)
  public void disablePerson() throws JsonProcessingException {
    var content =
        given().spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_XML)
            .accept(TestConfigs.CONTENT_TYPE_XML)
            .pathParam("id", person.getId())
            .when()
            .patch("{id}")
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
    assertFalse(persistedPerson.getEnabled());

    assertEquals("Nelson", persistedPerson.getFirstName());
    assertEquals("Piquet Souto Maior", persistedPerson.getLastName());
    assertEquals("Brasilia - DF, Brasil", persistedPerson.getAddress());
    assertEquals("Male", persistedPerson.getGender());
  }

  @Test
  @Order(4)
  public void testFindById() throws JsonProcessingException {
    var content =
        given().spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_XML)
            .accept(TestConfigs.CONTENT_TYPE_XML)
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
    assertFalse(persistedPerson.getEnabled());

    assertEquals("Nelson", persistedPerson.getFirstName());
    assertEquals("Piquet Souto Maior", persistedPerson.getLastName());
    assertEquals("Brasilia - DF, Brasil", persistedPerson.getAddress());
    assertEquals("Male", persistedPerson.getGender());
  }

  @Test
  @Order(5)
  public void testDelete() throws JsonProcessingException {
    given().spec(specification)
        .contentType(TestConfigs.CONTENT_TYPE_XML)
        .accept(TestConfigs.CONTENT_TYPE_XML)
        .pathParam("id", person.getId())
        .when()
        .delete("{id}")
        .then()
        .statusCode(204);
  }

  @Test
  @Order(6)
  public void testFindAll() throws JsonProcessingException {
    var content =
        given()
            .spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_XML)
            .accept(TestConfigs.CONTENT_TYPE_XML)
            .queryParams("page", 3, "size", 10, "direction", "asc")
            .when()
            .get()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString();

    PagedModelPerson wrapper = objectMapper.readValue(content, PagedModelPerson.class);
    var people = wrapper.getContent();

    PersonVO foundPersonOne = people.get(0);
    assertNotNull(foundPersonOne.getId());
    assertNotNull(foundPersonOne.getFirstName());
    assertNotNull(foundPersonOne.getLastName());
    assertNotNull(foundPersonOne.getAddress());
    assertNotNull(foundPersonOne.getGender());
    assertNotNull(foundPersonOne.getEnabled());

    assertTrue(foundPersonOne.getEnabled());

    assertEquals(676, foundPersonOne.getId());
    assertEquals("Alic", foundPersonOne.getFirstName());
    assertEquals("Terbrug", foundPersonOne.getLastName());
    assertEquals("3 Eagle Crest Court", foundPersonOne.getAddress());
    assertEquals("Male", foundPersonOne.getGender());


    PersonVO foundPersonSix = people.get(5);
    assertNotNull(foundPersonSix.getId());
    assertNotNull(foundPersonSix.getFirstName());
    assertNotNull(foundPersonSix.getLastName());
    assertNotNull(foundPersonSix.getAddress());
    assertNotNull(foundPersonSix.getGender());
    assertNotNull(foundPersonSix.getEnabled());

    assertTrue(foundPersonSix.getEnabled());

    assertEquals(910, foundPersonSix.getId());
    assertEquals("Allegra", foundPersonSix.getFirstName());
    assertEquals("Dome", foundPersonSix.getLastName());
    assertEquals("57 Roxbury Pass", foundPersonSix.getAddress());
    assertEquals("Female", foundPersonSix.getGender());
  }

  @Test
  @Order(7)
  public void testFindByName() throws JsonProcessingException {
    var content =
        given()
            .spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_XML)
            .accept(TestConfigs.CONTENT_TYPE_XML)
            .pathParam("firstName", "ayr")
            .queryParams("page", 0, "size", 6, "direction", "asc")
            .when()
            .get("findPersonsByName/{firstName}")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString();

    PagedModelPerson wrapper = objectMapper.readValue(content, PagedModelPerson.class);
    var people = wrapper.getContent();

    PersonVO foundPersonOne = people.get(0);
    assertNotNull(foundPersonOne.getId());
    assertNotNull(foundPersonOne.getFirstName());
    assertNotNull(foundPersonOne.getLastName());
    assertNotNull(foundPersonOne.getAddress());
    assertNotNull(foundPersonOne.getGender());
    assertNotNull(foundPersonOne.getEnabled());

    assertTrue(foundPersonOne.getEnabled());

    assertEquals(1, foundPersonOne.getId());
    assertEquals("Ayrton", foundPersonOne.getFirstName());
    assertEquals("Senna", foundPersonOne.getLastName());
    assertEquals("São Paulo", foundPersonOne.getAddress());
    assertEquals("Male", foundPersonOne.getGender());
  }

  @Test
  @Order(8)
  public void testHATEAOS() throws JsonProcessingException {
    var content =
        given()
            .spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_XML)
            .accept(TestConfigs.CONTENT_TYPE_XML)
            .queryParams("page", 0, "size", 10, "direction", "asc")
            .when()
            .get()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString();

    assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8888/api/person/v1/700</href></links>"));
    assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8888/api/person/v1/379</href></links>"));
    assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8888/api/person/v1/159</href></links>"));
    assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8888/api/person/v1/997</href></links>"));

    assertTrue(content.contains("<links><rel>first</rel><href>http://localhost:8888/api/person/v1?direction=asc&amp;page=0&amp;size=10&amp;sort=firstName,asc</href></links>"));
    assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8888/api/person/v1?page=0&amp;size=10&amp;direction=asc</href></links>"));
    assertTrue(content.contains("<links><rel>next</rel><href>http://localhost:8888/api/person/v1?direction=asc&amp;page=1&amp;size=10&amp;sort=firstName,asc</href></links>"));
    assertTrue(content.contains("<links><rel>last</rel><href>http://localhost:8888/api/person/v1?direction=asc&amp;page=100&amp;size=10&amp;sort=firstName,asc</href></links>"));

    assertTrue(content.contains("<page><size>10</size><totalElements>1009</totalElements><totalPages>101</totalPages><number>0</number></page>"));
  }

  @Test
  @Order(9)
  public void testFindAllWithoutToken() throws JsonProcessingException {
    RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
        .setBasePath("/api/person/v1")
        .setPort(TestConfigs.SERVER_PORT)
        .addFilter(new RequestLoggingFilter(LogDetail.ALL))
        .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
        .build();

    given()
        .spec(specificationWithoutToken)
        .contentType(TestConfigs.CONTENT_TYPE_XML)
        .accept(TestConfigs.CONTENT_TYPE_XML)
        .when()
        .get()
        .then()
        .statusCode(403);
  }
}
