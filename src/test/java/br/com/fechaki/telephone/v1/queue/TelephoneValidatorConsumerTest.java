package br.com.fechaki.telephone.v1.queue;

import br.com.fechaki.telephone.client.ValidationClient;
import br.com.fechaki.telephone.client.data.response.ClientValidationResponse;
import br.com.fechaki.telephone.v1.data.message.TelephoneMessageRequest;
import br.com.fechaki.telephone.v1.data.message.TelephoneStatusUpdaterMessageRequest;
import br.com.fechaki.telephone.v1.queue.impl.TelephoneValidatorConsumerImpl;
import br.com.fechaki.telephone.v1.service.TelephoneValidationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TelephoneValidatorConsumerTest {

    @Mock
    ValidationClient validationClient;

    @Mock
    TelephoneValidationService telephoneValidationService;

    @Mock
    TelephoneUpdaterProducer telephoneUpdaterProducer;

    @InjectMocks
    private TelephoneValidatorConsumerImpl consumer;

    @Test
    @DisplayName("Validation Needed")
    void receiveMessage() {
        ReflectionTestUtils.setField(consumer, "accessKey", "ACCESS_KEY");
        ReflectionTestUtils.setField(consumer, "format", "1");
        ReflectionTestUtils.setField(consumer, "countryCode", "BR");
        ReflectionTestUtils.setField(consumer, "validationEnabled", true);

        TelephoneMessageRequest request = new TelephoneMessageRequest(UUID.randomUUID().toString(), "21999999999");
        ClientValidationResponse response = new ClientValidationResponse(true, request.phoneNumber(), "0, 55XX" + request.phoneNumber(), "55" + request.phoneNumber(), "+55", "BR", "Brazil (Republic federative, of)", "Rio de Janeiro, Brazil", "Vivo", "MOBILE");

        when(telephoneValidationService.isValidationNeeded(anyString())).thenReturn(true);
        when(validationClient.validate(anyString(), anyString(), anyString(), anyString())).thenReturn(response);
        doNothing().when(telephoneValidationService).saveValidation(anyString(), any(ClientValidationResponse.class));
        doNothing().when(telephoneUpdaterProducer).sendMessage(any(TelephoneStatusUpdaterMessageRequest.class));
        assertDoesNotThrow(() -> consumer.receiveMessage(request));
    }

    @Test
    @DisplayName("Validation Needed - No Response")
    void receiveMessageNoResponse() {
        ReflectionTestUtils.setField(consumer, "accessKey", "ACCESS_KEY");
        ReflectionTestUtils.setField(consumer, "format", "1");
        ReflectionTestUtils.setField(consumer, "countryCode", "BR");
        ReflectionTestUtils.setField(consumer, "validationEnabled", true);

        TelephoneMessageRequest request = new TelephoneMessageRequest(UUID.randomUUID().toString(), "21999999999");
        ClientValidationResponse response = new ClientValidationResponse(true, null, "0, 55XX" + request.phoneNumber(), "55" + request.phoneNumber(), "+55", "BR", "Brazil (Republic federative, of)", "Rio de Janeiro, Brazil", "Vivo", "MOBILE");

        when(telephoneValidationService.isValidationNeeded(anyString())).thenReturn(true);
        when(validationClient.validate(anyString(), anyString(), anyString(), anyString())).thenReturn(response);

        assertDoesNotThrow(() -> consumer.receiveMessage(request));
    }

    @Test
    @DisplayName("Validation Not Needed")
    void receiveMessageNotNeeded() {
        TelephoneMessageRequest request = new TelephoneMessageRequest(UUID.randomUUID().toString(), "21999999999");

        when(telephoneValidationService.isValidationNeeded(anyString())).thenReturn(false);

        assertDoesNotThrow(() -> consumer.receiveMessage(request));
    }
}