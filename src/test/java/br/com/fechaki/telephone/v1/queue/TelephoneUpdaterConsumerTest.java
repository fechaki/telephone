package br.com.fechaki.telephone.v1.queue;

import br.com.fechaki.telephone.v1.data.entity.TelephoneEntity;
import br.com.fechaki.telephone.v1.data.message.TelephoneStatusUpdaterMessageRequest;
import br.com.fechaki.telephone.v1.queue.impl.TelephoneUpdaterConsumerImpl;
import br.com.fechaki.telephone.v1.service.TelephoneService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TelephoneUpdaterConsumerTest {
    @Mock
    TelephoneService telephoneService;

    @InjectMocks
    TelephoneUpdaterConsumerImpl telephoneUpdaterConsumer;

    @Test
    @DisplayName("Test Telephone Status Updater Message Consumer")
    void receiveMessage() {
        UUID uuid = UUID.randomUUID();
        TelephoneStatusUpdaterMessageRequest message = new TelephoneStatusUpdaterMessageRequest(uuid.toString(), true, true);
        TelephoneEntity entity = new TelephoneEntity();
        entity.setTelephoneId(uuid);

        when(telephoneService.read(anyString())).thenReturn(entity);
        doNothing().when(telephoneService).update(any(TelephoneEntity.class));

        assertDoesNotThrow(() -> telephoneUpdaterConsumer.receiveMessage(message));
    }

    @Test
    @DisplayName("Test Telephone Status Null Entity")
    void receiveMessageNullEntity() {
        UUID uuid = UUID.randomUUID();
        TelephoneStatusUpdaterMessageRequest message = new TelephoneStatusUpdaterMessageRequest(uuid.toString(), true, true);

        when(telephoneService.read(anyString())).thenReturn(null);
        assertDoesNotThrow(() -> telephoneUpdaterConsumer.receiveMessage(message));
    }
}