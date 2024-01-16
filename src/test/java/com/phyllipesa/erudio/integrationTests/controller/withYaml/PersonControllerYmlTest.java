package com.phyllipesa.erudio.integrationTests.controller.withYaml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phyllipesa.erudio.configs.TestConfigs;
import com.phyllipesa.erudio.integrationTests.controller.withYaml.mapper.YMLMapper;
import com.phyllipesa.erudio.integrationTests.testcontainers.AbstractIntegrationTest;
import com.phyllipesa.erudio.integrationTests.vo.AccountCredentialsVO;
import com.phyllipesa.erudio.integrationTests.vo.PersonVO;
import com.phyllipesa.erudio.integrationTests.vo.TokenVO;
import com.phyllipesa.erudio.integrationTests.vo.pagedmodels.PagedModelPerson;
import com.phyllipesa.erudio.integrationTests.vo.wrappers.WrapperPersonVO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerYmlTest extends AbstractIntegrationTest {
  private static RequestSpecification specification;
  private static YMLMapper objectMapper;
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
    objectMapper = new YMLMapper();
    person = new PersonVO();
  }

  @Test
  @Order(0)
  public void authorization() throws JsonProcessingException {
    AccountCredentialsVO user = new AccountCredentialsVO("phyllipe", "admin123");

    var accessToken =
        given()
            .config(
                RestAssuredConfig
                    .config()
                    .encoderConfig(EncoderConfig.encoderConfig()
                        .encodeContentTypeAs(
                            TestConfigs.CONTENT_TYPE_YML,
                            ContentType.TEXT
                        )))
            .basePath("/auth/signin")
            .port(TestConfigs.SERVER_PORT)
            .contentType(TestConfigs.CONTENT_TYPE_YML)
            .accept(TestConfigs.CONTENT_TYPE_YML)
            .body(user, objectMapper)
            .when()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .as(TokenVO.class, objectMapper)
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

    var persistedPerson =
        given()
            .spec(specification)
            .config(
                RestAssuredConfig
                    .config()
                    .encoderConfig(EncoderConfig.encoderConfig()
                        .encodeContentTypeAs(
                            TestConfigs.CONTENT_TYPE_YML,
                            ContentType.TEXT
                        )))
            .contentType(TestConfigs.CONTENT_TYPE_YML)
            .accept(TestConfigs.CONTENT_TYPE_YML)
            .body(person, objectMapper)
            .when()
            .post()
            .then()
            .statusCode(201)
            .extract()
            .body()
            .as(PersonVO.class, objectMapper);

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

    var persistedPerson =
        given()
            .spec(specification)
            .config(
                RestAssuredConfig
                    .config()
                    .encoderConfig(EncoderConfig.encoderConfig()
                        .encodeContentTypeAs(
                            TestConfigs.CONTENT_TYPE_YML,
                            ContentType.TEXT
                        )))
            .contentType(TestConfigs.CONTENT_TYPE_YML)
            .accept(TestConfigs.CONTENT_TYPE_YML)
            .body(person, objectMapper)
            .when()
            .put()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .as(PersonVO.class, objectMapper);

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
    var persistedPerson =
        given().spec(specification)
            .config(
                RestAssuredConfig
                    .config()
                    .encoderConfig(EncoderConfig.encoderConfig()
                        .encodeContentTypeAs(
                            TestConfigs.CONTENT_TYPE_YML,
                            ContentType.TEXT
                        )))
            .contentType(TestConfigs.CONTENT_TYPE_YML)
            .accept(TestConfigs.CONTENT_TYPE_YML)
            .pathParam("id", person.getId())
            .when()
            .patch("{id}")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .as(PersonVO.class, objectMapper);

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
    var persistedPerson =
        given().spec(specification)
            .config(
                RestAssuredConfig
                    .config()
                    .encoderConfig(EncoderConfig.encoderConfig()
                        .encodeContentTypeAs(
                            TestConfigs.CONTENT_TYPE_YML,
                            ContentType.TEXT
                        )))
            .contentType(TestConfigs.CONTENT_TYPE_YML)
            .accept(TestConfigs.CONTENT_TYPE_YML)
            .pathParam("id", person.getId())
            .when()
            .get("{id}")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .as(PersonVO.class, objectMapper);

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
        .config(
            RestAssuredConfig
                .config()
                .encoderConfig(EncoderConfig.encoderConfig()
                    .encodeContentTypeAs(
                        TestConfigs.CONTENT_TYPE_YML,
                        ContentType.TEXT
                    )))
        .contentType(TestConfigs.CONTENT_TYPE_YML)
        .accept(TestConfigs.CONTENT_TYPE_YML)
        .pathParam("id", person.getId())
        .when()
        .delete("{id}")
        .then()
        .statusCode(204);
  }

  @Test
  @Order(6)
  public void testFindAll() throws JsonProcessingException {
    var wrapper =
        given()
            .spec(specification)
            .config(
                RestAssuredConfig
                    .config()
                    .encoderConfig(EncoderConfig.encoderConfig()
                        .encodeContentTypeAs(
                            TestConfigs.CONTENT_TYPE_YML,
                            ContentType.TEXT
                        )))
            .contentType(TestConfigs.CONTENT_TYPE_YML)
            .accept(TestConfigs.CONTENT_TYPE_YML)
            .queryParams("page", 3, "size", 10, "direction", "asc")
            .when()
            .get()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .as(PagedModelPerson.class, objectMapper);

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
    assertNotNull(foundPersonOne.getEnabled());

    assertTrue(foundPersonOne.getEnabled());

    assertEquals(910, foundPersonSix.getId());
    assertEquals("Allegra", foundPersonSix.getFirstName());
    assertEquals("Dome", foundPersonSix.getLastName());
    assertEquals("57 Roxbury Pass", foundPersonSix.getAddress());
    assertEquals("Female", foundPersonSix.getGender());
  }

  @Test
  @Order(7)
  public void testFindAllWithoutToken() throws JsonProcessingException {
    RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
        .setBasePath("/api/person/v1")
        .setPort(TestConfigs.SERVER_PORT)
        .addFilter(new RequestLoggingFilter(LogDetail.ALL))
        .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
        .build();

    given()
        .spec(specificationWithoutToken)
        .config(
            RestAssuredConfig
                .config()
                .encoderConfig(EncoderConfig.encoderConfig()
                    .encodeContentTypeAs(
                        TestConfigs.CONTENT_TYPE_YML,
                        ContentType.TEXT
                    )))
        .contentType(TestConfigs.CONTENT_TYPE_YML)
        .when()
        .get()
        .then()
        .statusCode(403);
  }
}
