package br.com.fechaki.telephone.v1.queue.impl;

import br.com.fechaki.telephone.v1.data.message.TelephoneMessageRequest;
import br.com.fechaki.telephone.v1.queue.TelephoneEnrichmentProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelephoneEnrichmentProducerImpl implements TelephoneEnrichmentProducer {

    private final AmqpTemplate amqpTemplate;

    @Value("${fechaki.telephone.queue.enrichment.name}")
    private String queueName;

    @Override
    public void sendMessage(TelephoneMessageRequest messageRequest) {
        log.info("Sending message to Telephone Validation queue: {}", queueName);
        amqpTemplate.convertAndSend(queueName, messageRequest);
    }
}
