package com.phyllipesa.udm.integrationTests.controller.withjson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phyllipesa.udm.configs.TestConfigs;
import com.phyllipesa.udm.integrationTests.testcontainers.AbstractIntegrationTest;
import com.phyllipesa.udm.integrationTests.vo.AccountCredentialsVO;
import com.phyllipesa.udm.integrationTests.vo.TokenVO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerJsonTest extends AbstractIntegrationTest {

  private static TokenVO tokenVO;

  @Test
  @Order(1)
  public void testSignin() throws JsonProcessingException {
    AccountCredentialsVO user = new AccountCredentialsVO("phyllipe", "admin123");

    tokenVO = given()
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
                .as(TokenVO.class);

    assertNotNull(tokenVO.getAccessToken());
    assertNotNull(tokenVO.getRefreshToken());
  }

  @Test
  @Order(2)
  public void testRefresh() throws JsonProcessingException {
    AccountCredentialsVO user = new AccountCredentialsVO("phyllipe", "admin123");

    var newtokenVO = given()
        .basePath("/auth/refresh")
        .port(TestConfigs.SERVER_PORT)
        .contentType(TestConfigs.CONTENT_TYPE_JSON)
          .pathParam("username", tokenVO.getUsername())
          .header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenVO.getRefreshToken())
        .when()
          .put("{username}")
        .then()
          .statusCode(200)
            .extract()
              .body()
                .as(TokenVO.class);

    assertNotNull(newtokenVO.getAccessToken());
    assertNotNull(newtokenVO.getRefreshToken());
  }
}
