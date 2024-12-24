package br.com.fechaki.telephone.repository;

import br.com.fechaki.telephone.TestcontainersConfiguration;
import br.com.fechaki.telephone.v1.data.entity.TelephoneEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
@DisabledInAotMode
@Import(TestcontainersConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS, scripts = {"classpath:sql/dbinit.sql"})
class TelephoneRepositoryTest {
    @Autowired
    TelephoneRepository repository;

    @Autowired
    private PostgreSQLContainer<?> postgresContainer;

    @Test
    @DisplayName("Check Test Container Status")
    void checkStatus() {
        assertTrue(postgresContainer.isCreated());
        assertTrue(postgresContainer.isRunning());
    }

    @Test
    @DisplayName("Find all Telephones")
    void findAll() {
        List<TelephoneEntity> telephoneEntityList = repository.findAll();
        assertNotNull(telephoneEntityList);
        assertEquals(4, telephoneEntityList.size());
    }

    @Test
    @DisplayName("Find Telephone by Id")
    void findFirstByTelephoneIdAndDeletedFalse() {
        UUID telephoneId = UUID.fromString("6031dff8-2cd4-4dc7-b3b7-0d0a8b1d4ee8");
        TelephoneEntity result = repository.findFirstByTelephoneIdAndDeletedFalse(telephoneId);
        assertNotNull(result);
        assertEquals(telephoneId, result.getTelephoneId());
        assertEquals("55", result.getCountryCode());
        assertEquals("21", result.getAreaCode());
        assertEquals("999999999", result.getPhoneNumber());
        assertFalse(result.getActivated());
        assertFalse(result.getValidated());
        assertFalse(result.isDeleted());
        assertNotNull(result.getCreated());
        assertNotNull(result.getUpdated());
    }

    @Test
    @DisplayName("Find Telephone by Number")
    void findFirstByCountryCodeAndAreaCodeAndPhoneNumberAndDeletedFalse() {
        TelephoneEntity result = repository.findFirstByCountryCodeAndAreaCodeAndPhoneNumberAndDeletedFalse("55", "21", "988888888");
        assertNotNull(result);
        assertEquals(UUID.fromString("018b2f19-e79e-7d6a-a56d-29feb6211b04"), result.getTelephoneId());
        assertEquals("55", result.getCountryCode());
        assertEquals("21", result.getAreaCode());
        assertEquals("988888888", result.getPhoneNumber());
        assertFalse(result.getActivated());
        assertFalse(result.getValidated());
        assertFalse(result.isDeleted());
        assertNotNull(result.getCreated());
        assertNotNull(result.getUpdated());
    }
}