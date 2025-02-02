package br.com.fechaki.telephone.v1.queue;

import br.com.fechaki.telephone.client.EnrichmentClient;
import br.com.fechaki.telephone.client.data.response.ClientEnrichmentResponse;
import br.com.fechaki.telephone.v1.data.message.TelephoneMessageRequest;
import br.com.fechaki.telephone.v1.data.message.TelephoneStatusUpdaterMessageRequest;
import br.com.fechaki.telephone.v1.queue.impl.TelephoneEnrichmentConsumerImpl;
import br.com.fechaki.telephone.v1.service.TelephoneEnrichmentService;
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
class TelephoneEnrichmentConsumerTest {

    @Mock
    EnrichmentClient enrichmentClient;

    @Mock
    TelephoneEnrichmentService telephoneEnrichmentService;

    @Mock
    TelephoneUpdaterProducer telephoneUpdaterProducer;

    @InjectMocks
    private TelephoneEnrichmentConsumerImpl consumer;

    @Test
    @DisplayName("Enrichment Needed")
    void receiveMessage() {
        ReflectionTestUtils.setField(consumer, "accessKey", "ACCESS_KEY");
        ReflectionTestUtils.setField(consumer, "format", "1");
        ReflectionTestUtils.setField(consumer, "countryCode", "BR");
        ReflectionTestUtils.setField(consumer, "enrichmentEnabled", true);

        TelephoneMessageRequest request = new TelephoneMessageRequest(UUID.randomUUID().toString(), "21999999999");
        ClientEnrichmentResponse response = new ClientEnrichmentResponse(true, request.phoneNumber(), "0, 55XX" + request.phoneNumber(), "55" + request.phoneNumber(), "+55", "BR", "Brazil (Republic federative, of)", "Rio de Janeiro, Brazil", "Vivo", "MOBILE");

        when(telephoneEnrichmentService.isEnrichmentNeeded(anyString())).thenReturn(true);
        when(enrichmentClient.validate(anyString(), anyString(), anyString(), anyString())).thenReturn(response);
        doNothing().when(telephoneEnrichmentService).saveEnrichment(anyString(), any(ClientEnrichmentResponse.class));
        doNothing().when(telephoneUpdaterProducer).sendMessage(any(TelephoneStatusUpdaterMessageRequest.class));
        assertDoesNotThrow(() -> consumer.receiveMessage(request));
    }

    @Test
    @DisplayName("Enrichment Needed but Disabled")
    void receiveMessageDisabled() {
        ReflectionTestUtils.setField(consumer, "enrichmentEnabled", false);

        TelephoneMessageRequest request = new TelephoneMessageRequest(UUID.randomUUID().toString(), "21999999999");

        when(telephoneEnrichmentService.isEnrichmentNeeded(anyString())).thenReturn(true);
        doNothing().when(telephoneEnrichmentService).saveEnrichment(anyString(), any(ClientEnrichmentResponse.class));
        doNothing().when(telephoneUpdaterProducer).sendMessage(any(TelephoneStatusUpdaterMessageRequest.class));
        assertDoesNotThrow(() -> consumer.receiveMessage(request));
    }

    @Test
    @DisplayName("enrichment Needed - No Response")
    void receiveMessageNoResponse() {
        ReflectionTestUtils.setField(consumer, "accessKey", "ACCESS_KEY");
        ReflectionTestUtils.setField(consumer, "format", "1");
        ReflectionTestUtils.setField(consumer, "countryCode", "BR");
        ReflectionTestUtils.setField(consumer, "enrichmentEnabled", true);

        TelephoneMessageRequest request = new TelephoneMessageRequest(UUID.randomUUID().toString(), "21999999999");
        ClientEnrichmentResponse response = new ClientEnrichmentResponse(true, null, "0, 55XX" + request.phoneNumber(), "55" + request.phoneNumber(), "+55", "BR", "Brazil (Republic federative, of)", "Rio de Janeiro, Brazil", "Vivo", "MOBILE");

        when(telephoneEnrichmentService.isEnrichmentNeeded(anyString())).thenReturn(true);
        when(enrichmentClient.validate(anyString(), anyString(), anyString(), anyString())).thenReturn(response);

        assertDoesNotThrow(() -> consumer.receiveMessage(request));
    }

    @Test
    @DisplayName("Validation Not Needed")
    void receiveMessageNotNeeded() {
        TelephoneMessageRequest request = new TelephoneMessageRequest(UUID.randomUUID().toString(), "21999999999");

        when(telephoneEnrichmentService.isEnrichmentNeeded(anyString())).thenReturn(false);

        assertDoesNotThrow(() -> consumer.receiveMessage(request));
    }
}