package br.com.fechaki.telephone.v1.queue.impl;

import br.com.fechaki.telephone.client.ValidationClient;
import br.com.fechaki.telephone.client.data.request.ClientValidationRequest;
import br.com.fechaki.telephone.client.data.response.ClientValidationResponse;
import br.com.fechaki.telephone.v1.data.message.TelephoneMessageRequest;
import br.com.fechaki.telephone.v1.queue.TelephoneValidatorConsumer;
import br.com.fechaki.telephone.v1.service.TelephoneValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelephoneValidatorConsumerImpl implements TelephoneValidatorConsumer {
    private final ValidationClient validationClient;
    private final TelephoneValidationService telephoneValidationService;

    @Value("${fechaki.telephone.validation.access-key}")
    private String accessKey;

    @Value("${fechaki.telephone.validation.format}")
    private String format;

    @Value("${fechaki.telephone.validation.country-code}")
    private String countryCode;

    @Override
    public void receiveMessage(TelephoneMessageRequest request) {
        if (telephoneValidationService.isValidationNeeded(request.telephoneId())) {
            log.info("Telephone validator received message: {}", request);
            ClientValidationRequest clientRequest = new ClientValidationRequest(request.phoneNumber(), countryCode, format);
            ClientValidationResponse response = doValidation(clientRequest);

            if(response.number() != null) {
                telephoneValidationService.saveValidation(request.telephoneId(), response);
            }
            else {
                log.error("Validation API Call Error (Null Telephone ID): {}", request.phoneNumber());
            }
        }
        else {
            log.info("Validation API call not Needed: {}", request.telephoneId());
        }
    }

    private ClientValidationResponse doValidation(ClientValidationRequest request) {
        return validationClient.validate(accessKey, request.number(), request.countryCode(), request.format());
    }
}
