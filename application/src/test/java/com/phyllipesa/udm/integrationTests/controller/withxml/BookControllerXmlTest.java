package com.phyllipesa.udm.integrationTests.controller.withxml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.phyllipesa.udm.configs.TestConfigs;
import com.phyllipesa.udm.integrationTests.testcontainers.AbstractIntegrationTest;
import com.phyllipesa.udm.integrationTests.vo.AccountCredentialsVO;
import com.phyllipesa.udm.integrationTests.vo.BookVO;
import com.phyllipesa.udm.integrationTests.vo.TokenVO;
import com.phyllipesa.udm.integrationTests.vo.pagedmodels.PagedModelBook;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookControllerXmlTest extends AbstractIntegrationTest {
  private static RequestSpecification specification;
  private static XmlMapper objectMapper;
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
    objectMapper = new XmlMapper();
    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    book = new BookVO();
  }

  @Test
  @Order(0)
  public void authorization() {
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

    var content =
        given()
            .spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_XML)
            .accept(TestConfigs.CONTENT_TYPE_XML)
            .body(book)
            .when()
            .post()
            .then()
            .statusCode(201)
            .extract()
            .body()
            .asString();

    BookVO persistedBook = objectMapper.readValue(content, BookVO.class);
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

    var content =
        given()
            .spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_XML)
            .accept(TestConfigs.CONTENT_TYPE_XML)
            .body(book)
            .when()
            .put()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString();

    BookVO persistedBook = objectMapper.readValue(content, BookVO.class);
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
    var content =
        given().spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_XML)
            .accept(TestConfigs.CONTENT_TYPE_XML)
            .pathParam("id", book.getId())
            .when()
            .get("{id}")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString();

    BookVO persistedBook = objectMapper.readValue(content, BookVO.class);
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
        .contentType(TestConfigs.CONTENT_TYPE_XML)
        .accept(TestConfigs.CONTENT_TYPE_XML)
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
            .contentType(TestConfigs.CONTENT_TYPE_XML)
            .queryParams("page", 3, "size", 3, "direction", "asc")
            .accept(TestConfigs.CONTENT_TYPE_XML)
            .when()
            .get()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString();

    PagedModelBook wrapper = objectMapper.readValue(content, PagedModelBook.class);
    var books = wrapper.getContent();

    book = books.get(0);
    sdf.applyPattern("yyyy-MM-dd HH:mm:ss.SSS");
    date = sdf.parse("2017-11-07 15:09:01.674");
    assertNotNull(book.getId());
    assertNotNull(book.getAuthor());
    assertNotNull(book.getTitle());
    assertNotNull(book.getlaunchDate());
    assertNotNull(book.getPrice());

    assertEquals(13, book.getId());
    assertEquals("Richard Hunter e George Westerman", book.getAuthor());
    assertEquals("O verdadeiro valor de TI", book.getTitle());
    assertEquals(date.toInstant(), book.getlaunchDate().toInstant());
    assertEquals(95.0, book.getPrice());


    book = books.get(2);
    date = sdf.parse("2017-11-07 15:09:01.674");
    assertNotNull(book.getId());
    assertNotNull(book.getAuthor());
    assertNotNull(book.getTitle());
    assertNotNull(book.getlaunchDate());
    assertNotNull(book.getPrice());

    assertEquals(11, book.getId());
    assertEquals("Roger S. Pressman", book.getAuthor());
    assertEquals("Engenharia de Software: uma abordagem profissional", book.getTitle());
    assertEquals(date.toInstant(), book.getlaunchDate().toInstant());
    assertEquals(56.0, book.getPrice());
  }

  @Test
  @Order(6)
  public void testHATEAOS() throws JsonProcessingException {
    var content =
        given()
            .spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_XML)
            .accept(TestConfigs.CONTENT_TYPE_XML)
            .queryParams("page", 0, "size", 3, "direction", "asc")
            .when()
            .get()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString();

    assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8888/api/book/v1/15</href></links>"));
    assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8888/api/book/v1/9</href></links>"));
    assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8888/api/book/v1/4</href></links>"));

    assertTrue(content.contains("<links><rel>first</rel><href>http://localhost:8888/api/book/v1?direction=asc&amp;page=0&amp;size=3&amp;sort=author,asc</href></links>"));
    assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8888/api/book/v1?page=0&amp;size=3&amp;direction=asc</href></links>"));
    assertTrue(content.contains("<links><rel>next</rel><href>http://localhost:8888/api/book/v1?direction=asc&amp;page=1&amp;size=3&amp;sort=author,asc</href></links>"));
    assertTrue(content.contains("<links><rel>last</rel><href>http://localhost:8888/api/book/v1?direction=asc&amp;page=4&amp;size=3&amp;sort=author,asc</href></links>"));

    assertTrue(content.contains("<page><size>3</size><totalElements>15</totalElements><totalPages>5</totalPages><number>0</number></page>"));
  }

  @Test
  @Order(7)
  public void testFindAllWithoutToken() throws JsonProcessingException {
    RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
        .setBasePath("/api/book/v1")
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
