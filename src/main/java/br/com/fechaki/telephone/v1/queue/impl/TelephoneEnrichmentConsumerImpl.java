package br.com.fechaki.telephone.v1.queue.impl;

import br.com.fechaki.telephone.client.EnrichmentClient;
import br.com.fechaki.telephone.client.data.request.ClientEnrichmentRequest;
import br.com.fechaki.telephone.client.data.response.ClientEnrichmentResponse;
import br.com.fechaki.telephone.v1.data.message.TelephoneMessageRequest;
import br.com.fechaki.telephone.v1.data.message.TelephoneStatusUpdaterMessageRequest;
import br.com.fechaki.telephone.v1.queue.TelephoneUpdaterProducer;
import br.com.fechaki.telephone.v1.queue.TelephoneEnrichmentConsumer;
import br.com.fechaki.telephone.v1.service.TelephoneEnrichmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelephoneEnrichmentConsumerImpl implements TelephoneEnrichmentConsumer {
    private final EnrichmentClient validationClient;
    private final TelephoneEnrichmentService telephoneValidationService;
    private final TelephoneUpdaterProducer telephoneUpdaterProducer;

    @Value("${fechaki.telephone.enrichment.access-key}")
    private String accessKey;

    @Value("${fechaki.telephone.enrichment.enabled}")
    private boolean enrichmentEnabled;

    @Value("${fechaki.telephone.enrichment.format}")
    private String format;

    @Value("${fechaki.telephone.enrichment.country-code}")
    private String countryCode;

    @Override
    public void receiveMessage(TelephoneMessageRequest request) {
        if (telephoneValidationService.isEnrichmentNeeded(request.telephoneId())) {
            log.info("Telephone enrichment received message: {}", request);
            ClientEnrichmentRequest clientRequest = new ClientEnrichmentRequest(request.phoneNumber(), countryCode, format);
            ClientEnrichmentResponse response = doValidation(clientRequest);

            if(response.number() != null) {
                telephoneValidationService.saveEnrichment(request.telephoneId(), response);
                updateTelephoneStatus(request.telephoneId(), response.valid());
            }
            else {
                log.error("Enrichment API Call Error (Null Telephone ID): {}", request.phoneNumber());
            }
        }
        else {
            log.info("Enrichment API call not Needed: {}", request.telephoneId());
        }
    }

    private void updateTelephoneStatus(String telephoneId, Boolean validated) {
        TelephoneStatusUpdaterMessageRequest messageRequest = new TelephoneStatusUpdaterMessageRequest(
                telephoneId,
                Boolean.TRUE,
                validated
        );
        telephoneUpdaterProducer.sendMessage(messageRequest);
    }

    private ClientEnrichmentResponse doValidation(ClientEnrichmentRequest request) {
        if(enrichmentEnabled)
            return validationClient.validate(accessKey, request.number(), request.countryCode(), request.format());
        else
            return new ClientEnrichmentResponse(true, request.number(), "-", "-", "-", "-", "-", "-", "-", "Mobile");
    }
}
