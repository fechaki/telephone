package br.com.fechaki.telephone.v1.service;

import br.com.fechaki.telephone.client.data.response.ClientEnrichmentResponse;
import br.com.fechaki.telephone.exception.type.TelephoneEnrichmentException;
import br.com.fechaki.telephone.repository.TelephoneEnrichmentRepository;
import br.com.fechaki.telephone.v1.data.entity.TelephoneEnrichmentEntity;
import br.com.fechaki.telephone.v1.data.entity.TelephoneEntity;
import br.com.fechaki.telephone.v1.data.message.TelephoneMessageRequest;
import br.com.fechaki.telephone.v1.queue.TelephoneEnrichmentProducer;
import br.com.fechaki.telephone.v1.service.impl.TelephoneEnrichmentServiceImpl;
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
class TelephoneEnrichmentServiceTest {

    @Mock
    private TelephoneEnrichmentProducer validatorProducer;

    @Mock
    private TelephoneEnrichmentRepository validationRepository;

    @InjectMocks
    private TelephoneEnrichmentServiceImpl service;

    @Test
    @DisplayName("Validade Telephone to add in Queue")
    void addQueueEnrichment() {
        doNothing().when(validatorProducer).sendMessage(any(TelephoneMessageRequest.class));
        TelephoneEntity entity = new TelephoneEntity(UUID.randomUUID(), "55", "21", "988888888", Boolean.TRUE, null, false, LocalDateTime.now(), LocalDateTime.now());
        assertDoesNotThrow(() -> service.addQueueEnrichment(entity));
    }

    @Test
    @DisplayName("Save Validation Result")
    void saveEnrichment() {
        UUID uuid = UUID.randomUUID();
        ClientEnrichmentResponse response = new ClientEnrichmentResponse(true, "21999999999", "0, 0xx2199999-9999", "+5521999999999", "+55", "BR", "Brazil", "Rio de Janeiro", "Vivo", "Mobile");
        TelephoneEnrichmentEntity entity = new TelephoneEnrichmentEntity(uuid, response.valid(), response.number(), response.local_format(), response.international_format(), response.country_prefix(), response.country_code(), response.country_name(), response.location(), response.carrier(), response.line_type(), LocalDateTime.now());
        when(validationRepository.existsById(any(UUID.class))).thenReturn(false);
        when(validationRepository.save(any(TelephoneEnrichmentEntity.class))).thenReturn(entity);

        assertDoesNotThrow(() -> service.saveEnrichment(uuid.toString(), response));
    }

    @Test
    @DisplayName("Does not Save Validation Result, already Done")
    void saveEnrichmentExistTelephone() {
        String uuid = UUID.randomUUID().toString();
        ClientEnrichmentResponse response = new ClientEnrichmentResponse(true, "21999999999", "0, 0xx2199999-9999", "+5521999999999", "+55", "BR", "Brazil", "Rio de Janeiro", "Vivo", "Mobile");
        when(validationRepository.existsById(any(UUID.class))).thenReturn(true);
        assertThrows(TelephoneEnrichmentException.class, () -> service.saveEnrichment(uuid, response));
    }

    @Test
    @DisplayName("Check the Needs of new validation - Happy Path")
    void isEnrichmentNeeded() {
        UUID uuid = UUID.randomUUID();
        ReflectionTestUtils.setField(service, "untilDays", 90);
        Optional<TelephoneEnrichmentEntity> entity = Optional.of(new TelephoneEnrichmentEntity(uuid, true, "21999999999", "0, 0xx2199999-9999", "+5521999999999", "+55", "BR", "Brazil", "Rio de Janeiro", "Vivo", "Mobile", LocalDateTime.now()));
        when(validationRepository.findById(any(UUID.class))).thenReturn(entity);
        assertFalse(service.isEnrichmentNeeded(uuid.toString()));
    }

    @Test
    @DisplayName("Check the Needs of new validation - Null")
    void isEnrichmentNeededNull() {
        UUID uuid = UUID.randomUUID();
        Optional<TelephoneEnrichmentEntity> entity = Optional.empty();
        when(validationRepository.findById(any(UUID.class))).thenReturn(entity);
        assertTrue(service.isEnrichmentNeeded(uuid.toString()));
    }

    @Test
    @DisplayName("Check the Needs of new validation - Inside Date")
    void isEnrichmentNeededFalse() {
        UUID uuid = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime pass = now.minusDays(60);
        ReflectionTestUtils.setField(service, "untilDays", 90);
        Optional<TelephoneEnrichmentEntity> entity = Optional.of(new TelephoneEnrichmentEntity(uuid, true, "21999999999", "0, 0xx2199999-9999", "+5521999999999", "+55", "BR", "Brazil", "Rio de Janeiro", "Vivo", "Mobile", pass));
        when(validationRepository.findById(any(UUID.class))).thenReturn(entity);
        assertFalse(service.isEnrichmentNeeded(uuid.toString()));
    }

    @Test
    @DisplayName("Check the Needs of new validation - Out of Date")
    void isEnrichmentNeededTrue() {
        UUID uuid = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime pass = now.minusDays(90);
        ReflectionTestUtils.setField(service, "untilDays", 60);
        Optional<TelephoneEnrichmentEntity> entity = Optional.of(new TelephoneEnrichmentEntity(uuid, true, "21999999999", "0, 0xx2199999-9999", "+5521999999999", "+55", "BR", "Brazil", "Rio de Janeiro", "Vivo", "Mobile", pass));
        when(validationRepository.findById(any(UUID.class))).thenReturn(entity);
        assertTrue(service.isEnrichmentNeeded(uuid.toString()));
    }
}