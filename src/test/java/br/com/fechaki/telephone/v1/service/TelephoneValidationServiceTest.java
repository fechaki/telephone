package br.com.fechaki.telephone.v1.service;

import br.com.fechaki.telephone.client.data.response.ClientValidationResponse;
import br.com.fechaki.telephone.exception.type.TelephoneValidationException;
import br.com.fechaki.telephone.repository.TelephoneValidationRepository;
import br.com.fechaki.telephone.v1.data.entity.TelephoneEntity;
import br.com.fechaki.telephone.v1.data.entity.TelephoneValidationEntity;
import br.com.fechaki.telephone.v1.data.message.TelephoneMessageRequest;
import br.com.fechaki.telephone.v1.queue.TelephoneValidatorProducer;
import br.com.fechaki.telephone.v1.service.impl.TelephoneValidationServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TelephoneValidationServiceTest {

    @Mock
    private TelephoneValidatorProducer validatorProducer;

    @Mock
    private TelephoneValidationRepository validationRepository;

    @InjectMocks
    private TelephoneValidationServiceImpl service;

    @Test
    @DisplayName("Validade Telephone to add in Queue")
    void addQueueValidation() {
        doNothing().when(validatorProducer).sendMessage(any(TelephoneMessageRequest.class));
        TelephoneEntity entity = new TelephoneEntity(UUID.randomUUID(), "55", "21", "988888888", Boolean.TRUE, null, false, LocalDateTime.now(), LocalDateTime.now());
        assertDoesNotThrow(() -> service.addQueueValidation(entity));
    }

    @Test
    @DisplayName("Save Validation Result")
    void saveValidation() {
        UUID uuid = UUID.randomUUID();
        ClientValidationResponse response = new ClientValidationResponse(true, "21999999999", "0, 0xx2199999-9999", "+5521999999999", "+55", "BR", "Brazil", "Rio de Janeiro", "Vivo", "Mobile");
        TelephoneValidationEntity entity = new TelephoneValidationEntity(uuid, response.valid(), response.number(), response.local_format(), response.international_format(), response.country_prefix(), response.country_code(), response.country_name(), response.location(), response.carrier(), response.line_type(), LocalDateTime.now());
        when(validationRepository.existsById(any(UUID.class))).thenReturn(false);
        when(validationRepository.save(any(TelephoneValidationEntity.class))).thenReturn(entity);

        assertDoesNotThrow(() -> service.saveValidation(uuid.toString(), response));
    }

    @Test
    @DisplayName("Does not Save Validation Result, already Done")
    void saveValidationExistTelephone() {
        String uuid = UUID.randomUUID().toString();
        ClientValidationResponse response = new ClientValidationResponse(true, "21999999999", "0, 0xx2199999-9999", "+5521999999999", "+55", "BR", "Brazil", "Rio de Janeiro", "Vivo", "Mobile");
        when(validationRepository.existsById(any(UUID.class))).thenReturn(true);
        assertThrows(TelephoneValidationException.class, () -> service.saveValidation(uuid, response));
    }

    @Test
    @DisplayName("Check the Needs of new validation - Happy Path")
    void isValidationNeeded() {
        UUID uuid = UUID.randomUUID();
        ReflectionTestUtils.setField(service, "untilDays", 90);
        Optional<TelephoneValidationEntity> entity = Optional.of(new TelephoneValidationEntity(uuid, true, "21999999999", "0, 0xx2199999-9999", "+5521999999999", "+55", "BR", "Brazil", "Rio de Janeiro", "Vivo", "Mobile", LocalDateTime.now()));
        when(validationRepository.findById(any(UUID.class))).thenReturn(entity);
        assertFalse(service.isValidationNeeded(uuid.toString()));
    }

    @Test
    @DisplayName("Check the Needs of new validation - Null")
    void isValidationNeededNull() {
        UUID uuid = UUID.randomUUID();
        Optional<TelephoneValidationEntity> entity = Optional.empty();
        when(validationRepository.findById(any(UUID.class))).thenReturn(entity);
        assertTrue(service.isValidationNeeded(uuid.toString()));
    }

    @Test
    @DisplayName("Check the Needs of new validation - Inside Date")
    void isValidationNeededFalse() {
        UUID uuid = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime pass = now.minusDays(60);
        ReflectionTestUtils.setField(service, "untilDays", 90);
        Optional<TelephoneValidationEntity> entity = Optional.of(new TelephoneValidationEntity(uuid, true, "21999999999", "0, 0xx2199999-9999", "+5521999999999", "+55", "BR", "Brazil", "Rio de Janeiro", "Vivo", "Mobile", pass));
        when(validationRepository.findById(any(UUID.class))).thenReturn(entity);
        assertFalse(service.isValidationNeeded(uuid.toString()));
    }

    @Test
    @DisplayName("Check the Needs of new validation - Out of Date")
    void isValidationNeededTrue() {
        UUID uuid = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime pass = now.minusDays(90);
        ReflectionTestUtils.setField(service, "untilDays", 60);
        Optional<TelephoneValidationEntity> entity = Optional.of(new TelephoneValidationEntity(uuid, true, "21999999999", "0, 0xx2199999-9999", "+5521999999999", "+55", "BR", "Brazil", "Rio de Janeiro", "Vivo", "Mobile", pass));
        when(validationRepository.findById(any(UUID.class))).thenReturn(entity);
        assertTrue(service.isValidationNeeded(uuid.toString()));
    }
}