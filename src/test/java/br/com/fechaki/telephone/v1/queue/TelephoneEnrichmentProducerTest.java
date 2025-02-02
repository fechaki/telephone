package br.com.fechaki.telephone.v1.queue;

import br.com.fechaki.telephone.TestcontainersConfiguration;
import br.com.fechaki.telephone.v1.data.message.TelephoneMessageRequest;
import br.com.fechaki.telephone.v1.queue.impl.TelephoneEnrichmentProducerImpl;
import br.com.fechaki.telephone.v1.service.impl.TelephoneServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.testcontainers.containers.RabbitMQContainer;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisabledInAotMode
@ActiveProfiles("tst")
@Import(value = {TestcontainersConfiguration.class, TelephoneServiceImpl.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TelephoneEnrichmentProducerTest {

    @Autowired
    private RabbitMQContainer rabbitMQContainer;

    @Autowired
    private TelephoneEnrichmentProducerImpl producer;

    @BeforeEach
    void setUp() {
        if(!rabbitMQContainer.isRunning()) {
            rabbitMQContainer.start();
        }
    }

    @Test
    @DisplayName("Check Test Container Status")
    void checkStatus() {
        assertTrue(rabbitMQContainer.isCreated());
        assertTrue(rabbitMQContainer.isRunning());
    }

    @Test
    void sendMessage() {
        TelephoneMessageRequest request = new TelephoneMessageRequest(UUID.randomUUID().toString(), "21999999999");
        assertDoesNotThrow(() -> producer.sendMessage(request));
    }
}