package br.com.fechaki.telephone.controller;

import br.com.fechaki.telephone.TestcontainersConfiguration;
import br.com.fechaki.telephone.v1.data.entity.TelephoneEntity;
import br.com.fechaki.telephone.v1.data.request.TelephoneRequest;
import br.com.fechaki.telephone.v1.data.response.TelephoneResponse;
import br.com.fechaki.telephone.v1.service.TelephoneService;
import br.com.fechaki.telephone.v1.service.impl.TelephoneServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.aot.DisabledInAotMode;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpEntity.EMPTY;
import static org.springframework.http.HttpMethod.DELETE;

@DisabledInAotMode
@Import(value = {TestcontainersConfiguration.class, TelephoneServiceImpl.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TelephoneV1ControllerTest {
    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    TelephoneService service;

    String getHost() {
        return "http://localhost:" + port + "/api/v1/telephone";
    }

    String getHost(String telephoneId) {
        return getHost() + "/" + telephoneId;
    }

    @Test
    void createAction() {
        TelephoneRequest request = new TelephoneRequest("55", "21", "999999999");
        ResponseEntity<TelephoneResponse> response = restTemplate.postForEntity(getHost(), request, TelephoneResponse.class);
        assertNotNull(response);
        assertEquals(201, response.getStatusCode().value());
        assertNull(response.getBody());
        assertNotNull(response.getHeaders().getLocation());
    }

    @Test
    void readById() {
        TelephoneEntity request = new TelephoneEntity(null, "55", "21", "988888888", Boolean.TRUE, null, false, LocalDateTime.now(), LocalDateTime.now());
        TelephoneEntity result = service.create(request);
        ResponseEntity<TelephoneResponse> response = restTemplate.getForEntity(getHost(result.getTelephoneId().toString()), TelephoneResponse.class);
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.hasBody());
        assertNotNull(response.getBody().telephoneId());
        assertEquals("55", result.getCountryCode());
        assertEquals("21", result.getAreaCode());
        assertEquals("988888888", result.getPhoneNumber());
    }

    @Test
    void testReadByTelephone() {
        TelephoneEntity request = new TelephoneEntity(null, "55", "21", "977777777", Boolean.TRUE, null, false, LocalDateTime.now(), LocalDateTime.now());
        TelephoneEntity result = service.create(request);
        ResponseEntity<TelephoneResponse> response = restTemplate.getForEntity(getHost(request.getCountryCode() + "/" + result.getAreaCode() + "/" + result.getPhoneNumber()), TelephoneResponse.class);
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.hasBody());
        assertNotNull(response.getBody().telephoneId());
        assertEquals("55", result.getCountryCode());
        assertEquals("21", result.getAreaCode());
        assertEquals("977777777", result.getPhoneNumber());
    }

    @Test
    void deleteById() {
        TelephoneEntity request = new TelephoneEntity(null, "55", "21", "966666666", Boolean.TRUE, null, false, LocalDateTime.now(), LocalDateTime.now());
        TelephoneEntity result = service.create(request);
        ResponseEntity<Void> response = restTemplate.exchange(getHost(result.getTelephoneId().toString()), DELETE, EMPTY, Void.class);
        assertEquals(202, response.getStatusCode().value());
    }
}