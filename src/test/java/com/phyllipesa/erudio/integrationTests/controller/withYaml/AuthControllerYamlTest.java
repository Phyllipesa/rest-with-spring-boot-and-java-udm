package com.phyllipesa.erudio.integrationTests.controller.withYaml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phyllipesa.erudio.configs.TestConfigs;
import com.phyllipesa.erudio.integrationTests.controller.withYaml.mapper.YMLMapper;
import com.phyllipesa.erudio.integrationTests.testcontainers.AbstractIntegrationTest;
import com.phyllipesa.erudio.integrationTests.vo.AccountCredentialsVO;
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

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerYamlTest extends AbstractIntegrationTest {

  private static YMLMapper objectMapper;
  private static TokenVO tokenVO;

  @BeforeAll
  public static void setup() {
    objectMapper = new YMLMapper();
  }

  @Test
  @Order(1)
  public void testSignin() throws JsonProcessingException {
    AccountCredentialsVO user = new AccountCredentialsVO("phyllipe", "admin123");

    RequestSpecification specification = new RequestSpecBuilder()
        .addFilter(new RequestLoggingFilter(LogDetail.ALL))
        .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
        .build();

    tokenVO = given().spec(specification)
        .config(
            RestAssuredConfig
                .config()
                .encoderConfig(EncoderConfig.encoderConfig()
                    .encodeContentTypeAs(
                        TestConfigs.CONTENT_TYPE_YML,
                        ContentType.TEXT
                    )))
        .accept(TestConfigs.CONTENT_TYPE_YML)
        .basePath("/auth/signin")
        .port(TestConfigs.SERVER_PORT)
        .contentType(TestConfigs.CONTENT_TYPE_YML)
        .body(user, objectMapper)
        .when()
        .post()
        .then()
        .statusCode(200)
        .extract()
        .body()
        .as(TokenVO.class, objectMapper);

    assertNotNull(tokenVO.getAccessToken());
    assertNotNull(tokenVO.getRefreshToken());
  }

  @Test
  @Order(2)
  public void testRefresh() throws JsonProcessingException {
    AccountCredentialsVO user = new AccountCredentialsVO("phyllipe", "admin123");

    var newtokenVO = given()
        .config(
            RestAssuredConfig
                .config()
                .encoderConfig(EncoderConfig.encoderConfig()
                    .encodeContentTypeAs(
                        TestConfigs.CONTENT_TYPE_YML,
                        ContentType.TEXT
                    )))
        .accept(TestConfigs.CONTENT_TYPE_YML)
        .basePath("/auth/refresh")
        .port(TestConfigs.SERVER_PORT)
        .contentType(TestConfigs.CONTENT_TYPE_YML)
        .pathParam("username", tokenVO.getUsername())
        .header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenVO.getRefreshToken())
        .when()
        .put("{username}")
        .then()
        .statusCode(200)
        .extract()
        .body()
        .as(TokenVO.class, objectMapper);

    assertNotNull(newtokenVO.getAccessToken());
    assertNotNull(newtokenVO.getRefreshToken());
  }
}
