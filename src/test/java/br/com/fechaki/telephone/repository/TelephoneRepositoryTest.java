package br.com.fechaki.telephone.repository;

import br.com.fechaki.telephone.TestcontainersConfiguration;
import br.com.fechaki.telephone.v1.data.entity.TelephoneEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@Testcontainers
@DisabledInAotMode
@Import(TestcontainersConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TelephoneRepositoryTest {
    @Autowired
    TelephoneRepository repository;

    static Stream<Arguments> validTelephoneBuilder() {
        return Stream.of(
            Arguments.of(
                new TelephoneEntity(UUID.randomUUID(), "55", "21", "999999999", Boolean.TRUE, null, false, LocalDateTime.now(), LocalDateTime.now()),
                new TelephoneEntity(UUID.randomUUID(), "55", "21", "988888888", Boolean.TRUE, null, false, LocalDateTime.now(), LocalDateTime.now())
            )
        );
    }

    @ParameterizedTest
    @DisplayName("Find Telephone by Id")
    @MethodSource("validTelephoneBuilder")
    void findFirstByTelephoneIdAndDeletedFalse(TelephoneEntity entity) {
        TelephoneEntity savedEntity = repository.save(entity);
        TelephoneEntity result = repository.findFirstByTelephoneIdAndDeletedFalse(savedEntity.getTelephoneId());
        assertNotNull(result);
    }

    @ParameterizedTest
    @MethodSource("validTelephoneBuilder")
    @DisplayName("Find Telephone by Number")
    void findFirstByCountryCodeAndAreaCodeAndPhoneNumberAndDeletedFalse(TelephoneEntity entity) {
        TelephoneEntity savedEntity = repository.save(entity);
        TelephoneEntity result = repository.findFirstByCountryCodeAndAreaCodeAndPhoneNumberAndDeletedFalse(savedEntity.getCountryCode(), savedEntity.getAreaCode(), savedEntity.getPhoneNumber());
        assertNotNull(result);
    }
}