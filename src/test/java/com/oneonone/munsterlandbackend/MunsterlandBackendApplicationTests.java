package com.oneonone.munsterlandbackend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.http.HttpStatus;

@SpringBootTest
class MunsterlandBackendApplicationTests {

    @LocalServerPort int port;

    @Autowired TestRestTemplate rest;

    @Test
    void signupCreatesUser() {
      var body = Map.of(
        "email", "it{{rand}}@example.com".replace("{{rand}}", ""+System.nanoTime()),
        "password", "Secret123!",
        "fullName", "It Test",
        "kind", "NEWCOMER",
        "role", "STUDENT"
      );
      var resp = rest.postForEntity("http://localhost:"+port+"/auth/signup", body, String.class);
      assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(resp.getBody()).contains("email");
    }
  

}
