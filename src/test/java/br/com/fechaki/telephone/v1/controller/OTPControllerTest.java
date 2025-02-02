package br.com.fechaki.telephone.v1.controller;

import br.com.fechaki.telephone.TestcontainersConfiguration;
import br.com.fechaki.telephone.repository.TelephoneRepository;
import br.com.fechaki.telephone.v1.data.entity.TelephoneEntity;
import br.com.fechaki.telephone.v1.data.request.TelephoneValidationRequest;
import br.com.fechaki.telephone.v1.service.SNSService;
import br.com.fechaki.telephone.v1.service.impl.SNSServiceImpl;
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

import static org.junit.jupiter.api.Assertions.*;

@DisabledInAotMode
@ActiveProfiles("tst")
@Import(value = {TestcontainersConfiguration.class, SNSServiceImpl.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OTPControllerTest {
    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    TelephoneRepository repository;

    @Autowired
    SNSService snsService;

    String getHost() {
        return "http://localhost:" + port + "/api/v1/telephone/otp";
    }

    String getHost(String telephoneId) {
        return getHost() + "/" + telephoneId;
    }

    @Test
    @DisplayName("OTP Send Validation")
    void sendValidation() {
        TelephoneEntity request = new TelephoneEntity(null, "55", "21", "988888888", Boolean.TRUE, null, false, LocalDateTime.now(), LocalDateTime.now());
        TelephoneEntity result = repository.save(request);
        String telephoneId = result.getTelephoneId().toString();
        ResponseEntity<Void> response = restTemplate.postForEntity(getHost(telephoneId), null, Void.class);

        assertEquals(202, response.getStatusCode().value());
        assertFalse(response.hasBody());
    }

    @Test
    @DisplayName("OTP Validation Code - Valid")
    void validate() {
        TelephoneEntity entity = new TelephoneEntity(null, "55", "21", "977777777", Boolean.TRUE, null, false, LocalDateTime.now(), LocalDateTime.now());
        TelephoneEntity result = repository.save(entity);
        String telephoneId = result.getTelephoneId().toString();
        String otpToken = snsService.generateOtp(telephoneId);

        String uri = getHost(telephoneId) + "/validate";
        TelephoneValidationRequest request = new TelephoneValidationRequest(otpToken);
        ResponseEntity<Void> response = restTemplate.postForEntity(uri, request, Void.class);

        assertEquals(202, response.getStatusCode().value());
        assertFalse(response.hasBody());
    }

    @Test
    @DisplayName("OTP Validation Code - Invalid")
    void invalidate() {
        TelephoneEntity entity = new TelephoneEntity(null, "55", "21", "966666666", Boolean.TRUE, null, false, LocalDateTime.now(), LocalDateTime.now());
        TelephoneEntity result = repository.save(entity);
        String telephoneId = result.getTelephoneId().toString();
        String otpToken = "123456";

        String uri = getHost(telephoneId) + "/validate";
        TelephoneValidationRequest request = new TelephoneValidationRequest(otpToken);
        ResponseEntity<Void> response = restTemplate.postForEntity(uri, request, Void.class);

        assertEquals(400, response.getStatusCode().value());
        assertFalse(response.hasBody());
    }
}