package br.com.fechaki.telephone.v1.controller;

import br.com.fechaki.telephone.TestcontainersConfiguration;
import br.com.fechaki.telephone.repository.TelephoneRepository;
import br.com.fechaki.telephone.v1.data.entity.TelephoneEntity;
import br.com.fechaki.telephone.v1.data.request.TelephoneRequest;
import br.com.fechaki.telephone.v1.data.response.TelephoneResponse;
import br.com.fechaki.telephone.v1.service.impl.TelephoneServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.aot.DisabledInAotMode;

import java.time.LocalDateTime;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpEntity.EMPTY;
import static org.springframework.http.HttpMethod.DELETE;

@DisabledInAotMode
@ActiveProfiles("tst")
@Import(value = {TestcontainersConfiguration.class, TelephoneServiceImpl.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TelephoneControllerTest {
    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    TelephoneRepository repository;

    String getHost() {
        return "http://localhost:" + port + "/api/v1/telephone";
    }

    String getHost(String telephoneId) {
        return getHost() + "/" + telephoneId;
    }

    @Test
    @DisplayName("Integration Test - Create")
    void createAction() {
        TelephoneRequest request = new TelephoneRequest("55", "21", "999999999");
        ResponseEntity<TelephoneResponse> response = restTemplate.postForEntity(getHost(), request, TelephoneResponse.class);
        assertNotNull(response);
        assertEquals(201, response.getStatusCode().value());
        assertNull(response.getBody());
        assertNotNull(response.getHeaders().getLocation());
        assertNotNull(response.getHeaders().get("Server"));
    }

    @Test
    @DisplayName("Integration Test - Read By ID")
    void readById() {
        TelephoneEntity request = new TelephoneEntity(null, "55", "21", "988888888", Boolean.TRUE, null, false, LocalDateTime.now(), LocalDateTime.now());
        TelephoneEntity result = repository.save(request);
        ResponseEntity<TelephoneResponse> response = restTemplate.getForEntity(getHost(result.getTelephoneId().toString()), TelephoneResponse.class);
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.hasBody());
        assertNotNull(Objects.requireNonNull(response.getBody()).telephoneId());
        assertEquals("55", result.getCountryCode());
        assertEquals("21", result.getAreaCode());
        assertEquals("988888888", result.getPhoneNumber());
        assertNotNull(response.getHeaders().get("Server"));
    }

    @Test
    @DisplayName("Integration Test - Read by Telephone")
    void testReadByTelephone() {
        TelephoneEntity request = new TelephoneEntity(null, "55", "21", "977777777", Boolean.TRUE, null, false, LocalDateTime.now(), LocalDateTime.now());
        TelephoneEntity result = repository.save(request);
        ResponseEntity<TelephoneResponse> response = restTemplate.getForEntity(getHost(request.getCountryCode() + "/" + result.getAreaCode() + "/" + result.getPhoneNumber()), TelephoneResponse.class);
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.hasBody());
        assertNotNull(Objects.requireNonNull(response.getBody()).telephoneId());
        assertEquals("55", result.getCountryCode());
        assertEquals("21", result.getAreaCode());
        assertEquals("977777777", result.getPhoneNumber());
        assertNotNull(response.getHeaders().get("Server"));
    }

    @Test
    @DisplayName("Integration Test - Delete")
    void deleteById() {
        TelephoneEntity request = new TelephoneEntity(null, "55", "21", "966666666", Boolean.TRUE, null, false, LocalDateTime.now(), LocalDateTime.now());
        TelephoneEntity result = repository.save(request);
        ResponseEntity<Void> response = restTemplate.exchange(getHost(result.getTelephoneId().toString()), DELETE, EMPTY, Void.class);
        assertEquals(202, response.getStatusCode().value());
        assertNotNull(response.getHeaders().get("Server"));
    }
}