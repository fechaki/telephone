package br.com.fechaki.telephone.v1.service;

import br.com.fechaki.telephone.exception.type.DuplicateTelephoneException;
import br.com.fechaki.telephone.exception.type.TelephoneNotExistException;
import br.com.fechaki.telephone.exception.type.TelephoneNotFoundException;
import br.com.fechaki.telephone.repository.TelephoneRepository;
import br.com.fechaki.telephone.v1.data.entity.TelephoneEntity;
import br.com.fechaki.telephone.v1.service.impl.TelephoneServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TelephoneServiceTest {
    @Mock
    private TelephoneRepository repository;

    @Mock
    private TelephoneEnrichmentService validationService;

    @InjectMocks
    private TelephoneServiceImpl service;

    @Test
    @DisplayName("Create Telephone Service")
    void create() {
        TelephoneEntity entity = new TelephoneEntity(UUID.randomUUID(), "55", "21", "988888888", Boolean.TRUE, null, false, LocalDateTime.now(), LocalDateTime.now());
        when(repository.save(any(TelephoneEntity.class))).thenReturn(entity);
        doNothing().when(validationService).addQueueEnrichment(any(TelephoneEntity.class));
        TelephoneEntity result = service.create(entity);

        assertNotNull(result);
        assertNotNull(result.getTelephoneId());
        assertEquals("55", result.getCountryCode());
        assertEquals("21", result.getAreaCode());
        assertEquals("988888888", result.getPhoneNumber());
        assertNull(result.getValidated());
        assertTrue(result.getActivated());
        assertFalse(result.isDeleted());
        assertNotNull(result.getCreated());
        assertNotNull(result.getUpdated());
    }

    @Test
    @DisplayName("Duplicated Telephone Exception")
    void createWithException() {
        TelephoneEntity entity = new TelephoneEntity();
        when(repository.save(any(TelephoneEntity.class))).thenThrow(DuplicateKeyException.class);

        DuplicateTelephoneException exception = assertThrows(DuplicateTelephoneException.class, () -> service.create(entity));
        assertNotNull(exception);
    }

    @Test
    @DisplayName("Read Telephone By Id")
    void readById() {
        TelephoneEntity entity = new TelephoneEntity(UUID.randomUUID(), "55", "21", "988888888", Boolean.TRUE, null, false, LocalDateTime.now(), LocalDateTime.now());
        when(repository.findFirstByTelephoneIdAndDeletedFalse(any(UUID.class))).thenReturn(entity);

        TelephoneEntity result = service.read(UUID.randomUUID().toString());
        assertNotNull(result);
    }

    @Test
    @DisplayName("Read Telephone by Id Exception")
    void readByIdWithException() {
        String id = UUID.randomUUID().toString();
        doThrow(new TelephoneNotFoundException(UUID.randomUUID().toString())).when(repository).findFirstByTelephoneIdAndDeletedFalse(any(UUID.class));

        TelephoneNotFoundException exception = assertThrows(TelephoneNotFoundException.class, () -> service.read(id));
        assertNotNull(exception);
    }

    @Test
    @DisplayName("Read Telephone by Id Exception Null Entity")
    void readByIdWithNullException() {
        String id = UUID.randomUUID().toString();
        when(repository.findFirstByTelephoneIdAndDeletedFalse(any(UUID.class))).thenReturn(null);

        TelephoneNotFoundException exception = assertThrows(TelephoneNotFoundException.class, () -> service.read(id));
        assertNotNull(exception);
    }

    @Test
    @DisplayName("Read Telephone By Number")
    void readByTelephone() {
        TelephoneEntity entity = new TelephoneEntity(null, "55", "21", "988888888", Boolean.TRUE, null, false, LocalDateTime.now(), LocalDateTime.now());
        when(repository.findFirstByCountryCodeAndAreaCodeAndPhoneNumberAndDeletedFalse(anyString(), anyString(), anyString())).thenReturn(entity);

        TelephoneEntity result = service.read("", "", "");
        assertNotNull(result);
    }

    @Test
    @DisplayName("Read Telephone By Number Exception")
    void readByTelephoneWithException() {
        doThrow(new TelephoneNotFoundException(UUID.randomUUID().toString())).when(repository).findFirstByCountryCodeAndAreaCodeAndPhoneNumberAndDeletedFalse(anyString(), anyString(), anyString());

        TelephoneNotFoundException exception = assertThrows(TelephoneNotFoundException.class, () -> service.read("", "", ""));
        assertNotNull(exception);
    }

    @Test
    @DisplayName("Read Telephone By Number Exception Null Entity")
    void readByTelephoneWithNullException() {
        when(repository.findFirstByCountryCodeAndAreaCodeAndPhoneNumberAndDeletedFalse(anyString(), anyString(), anyString())).thenReturn(null);

        TelephoneNotFoundException exception = assertThrows(TelephoneNotFoundException.class, () -> service.read("", "", ""));
        assertNotNull(exception);
    }

    @Test
    @DisplayName("Update Telephone")
    void update() {
        UUID telephoneId = UUID.randomUUID();
        TelephoneEntity entity = new TelephoneEntity(telephoneId, "55", "21", "988888888", Boolean.TRUE, null, false, LocalDateTime.now(), LocalDateTime.now());
        when(repository.existsById(Mockito.any(UUID.class))).thenReturn(true);
        when(repository.save(any(TelephoneEntity.class))).thenReturn(entity);

        assertDoesNotThrow(() -> service.update(entity));
    }

    @Test
    @DisplayName("Update Telephone Wrong ID")
    void updateWrongID() {
        UUID telephoneId = UUID.randomUUID();
        TelephoneEntity entity = new TelephoneEntity(telephoneId, "55", "21", "988888888", Boolean.TRUE, null, false, LocalDateTime.now(), LocalDateTime.now());
        when(repository.existsById(Mockito.any(UUID.class))).thenReturn(false);

        TelephoneNotExistException exception = assertThrows(TelephoneNotExistException.class, () -> service.update(entity));
        assertNotNull(exception);
    }

    @Test
    @DisplayName("Update Telephone Exception")
    void updateException() {
        UUID telephoneId = UUID.randomUUID();
        TelephoneEntity entity = new TelephoneEntity(telephoneId, "55", "21", "988888888", Boolean.TRUE, null, false, LocalDateTime.now(), LocalDateTime.now());

        TelephoneNotExistException exception = assertThrows(TelephoneNotExistException.class, () -> service.update(entity));
        assertNotNull(exception);
    }

    @Test
    @DisplayName("Delete Telephone")
    void delete() {
        when(repository.existsById(Mockito.any(UUID.class))).thenReturn(true);

        service.delete(UUID.randomUUID().toString());
        verify(repository).deleteById(any(UUID.class));
    }

    @Test
    @DisplayName("Delete Telephone Not Exist Exception")
    void deleteWithException() {
        String id = UUID.randomUUID().toString();
        when(repository.existsById(Mockito.any(UUID.class))).thenReturn(false);

        TelephoneNotExistException exception = assertThrows(TelephoneNotExistException.class, () -> service.delete(id));
        assertNotNull(exception);
    }

    @Test
    @DisplayName("Delete Telephone Exist but with Exception")
    void deleteWithNotExistException() {
        String id = UUID.randomUUID().toString();
        when(repository.existsById(Mockito.any(UUID.class))).thenReturn(true);
        doThrow(new TelephoneNotExistException(UUID.randomUUID().toString())).when(repository).deleteById(any(UUID.class));

        TelephoneNotExistException exception = assertThrows(TelephoneNotExistException.class, () -> service.delete(id));
        assertNotNull(exception);
    }
}