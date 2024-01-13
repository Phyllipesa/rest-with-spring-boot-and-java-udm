package com.phyllipesa.erudio.integrationTests.controller.withYaml;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.phyllipesa.erudio.configs.TestConfigs;
import com.phyllipesa.erudio.integrationTests.controller.withYaml.mapper.YMLMapper;
import com.phyllipesa.erudio.integrationTests.testcontainers.AbstractIntegrationTest;
import com.phyllipesa.erudio.integrationTests.vo.AccountCredentialsVO;
import com.phyllipesa.erudio.integrationTests.vo.BookVO;
import com.phyllipesa.erudio.integrationTests.vo.TokenVO;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookControllerYmlTest extends AbstractIntegrationTest {
  private static RequestSpecification specification;
  private static YMLMapper objectMapper;
  private static BookVO book;
  private static Date date;
  private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  private void mockBook() {
    try {
      date = sdf.parse("2003-08-22 00:00:00");

      book.setAuthor("Dany Evans");
      book.setPrice(118.72);
      book.setTitle("Domain-Driven Design: Tackling Complexity in the Heart of Software");
      book.setlaunchDate(date);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  @BeforeAll
  public static void setup() {
    objectMapper = new YMLMapper();
    book = new BookVO();
  }

  @Test
  @Order(0)
  public void authorization() {
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
        .setBasePath("/api/book/v1")
        .setPort(TestConfigs.SERVER_PORT)
        .addFilter(new RequestLoggingFilter(LogDetail.ALL))
        .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
        .build();
  }

  @Test
  @Order(1)
  public void testCreate() throws JsonProcessingException {
    mockBook();

    var persistedBook =
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
            .body(book, objectMapper)
            .when()
            .post()
            .then()
            .statusCode(201)
            .extract()
            .body()
            .as(BookVO.class, objectMapper);

    book = persistedBook;
    assertNotNull(persistedBook.getId());
    assertNotNull(persistedBook.getAuthor());
    assertNotNull(persistedBook.getTitle());
    assertNotNull(persistedBook.getlaunchDate());
    assertNotNull(persistedBook.getPrice());

    assertTrue(book.getId() > 0);
    assertEquals("Dany Evans", persistedBook.getAuthor());
    assertEquals("Domain-Driven Design: Tackling Complexity in the Heart of Software", persistedBook.getTitle());
    assertEquals(date, persistedBook.getlaunchDate());
    assertEquals(118.72, persistedBook.getPrice());
  }

  @Test
  @Order(2)
  public void testUpdate() throws JsonProcessingException {
    book.setAuthor("Eric Evans");

    var persistedBook =
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
            .body(book, objectMapper)
            .when()
            .put()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .as(BookVO.class, objectMapper);

    book = persistedBook;
    assertNotNull(persistedBook.getId());
    assertNotNull(persistedBook.getAuthor());
    assertNotNull(persistedBook.getTitle());
    assertNotNull(persistedBook.getlaunchDate());
    assertNotNull(persistedBook.getPrice());

    assertTrue(book.getId() > 0);
    assertEquals("Eric Evans", persistedBook.getAuthor());
    assertEquals("Domain-Driven Design: Tackling Complexity in the Heart of Software", persistedBook.getTitle());
    assertEquals(date, persistedBook.getlaunchDate());
    assertEquals(118.72, persistedBook.getPrice());
  }

  @Test
  @Order(3)
  public void testFindById() throws JsonProcessingException {
    var persistedBook =
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
            .pathParam("id", book.getId())
            .when()
            .get("{id}")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .as(BookVO.class, objectMapper);

    book = persistedBook;
    assertNotNull(persistedBook.getId());
    assertNotNull(persistedBook.getAuthor());
    assertNotNull(persistedBook.getTitle());
    assertNotNull(persistedBook.getlaunchDate());
    assertNotNull(persistedBook.getPrice());

    assertTrue(book.getId() > 0);
    assertEquals("Eric Evans", persistedBook.getAuthor());
    assertEquals("Domain-Driven Design: Tackling Complexity in the Heart of Software", persistedBook.getTitle());
    assertEquals(date, persistedBook.getlaunchDate());
    assertEquals(118.72, persistedBook.getPrice());
  }

  @Test
  @Order(4)
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
        .pathParam("id", book.getId())
        .when()
        .delete("{id}")
        .then()
        .statusCode(204);
  }

  @Test
  @Order(5)
  public void testFindAll() throws JsonProcessingException, ParseException {
    var content =
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
            .when()
            .get()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .as(BookVO[].class, objectMapper);

    List<BookVO> books = Arrays.asList(content);
    book = books.get(0);
    sdf.applyPattern("yyyy-MM-dd HH:mm:ss.SSS");
    date = sdf.parse("2017-11-29 13:50:05.878");
    assertNotNull(book.getId());
    assertNotNull(book.getAuthor());
    assertNotNull(book.getTitle());
    assertNotNull(book.getlaunchDate());
    assertNotNull(book.getPrice());

    assertEquals(1, book.getId());
    assertEquals("Michael C. Feathers", book.getAuthor());
    assertEquals("Working effectively with legacy code", book.getTitle());
    assertEquals(date.toInstant(), book.getlaunchDate().toInstant());
    assertEquals(49.0, book.getPrice());


    book = books.get(5);
    date = sdf.parse("2017-11-07 15:09:01.674");
    assertNotNull(book.getId());
    assertNotNull(book.getAuthor());
    assertNotNull(book.getTitle());
    assertNotNull(book.getlaunchDate());
    assertNotNull(book.getPrice());

    assertEquals(6, book.getId());
    assertEquals("Martin Fowler e Kent Beck", book.getAuthor());
    assertEquals("Refactoring", book.getTitle());
    assertEquals(date.toInstant(), book.getlaunchDate().toInstant());
    assertEquals(88.0, book.getPrice());
  }

  @Test
  @Order(6)
  public void testFindAllWithoutToken() throws JsonProcessingException {
    RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
        .setBasePath("/api/book/v1")
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
