package br.com.fechaki.telephone.v1.queue.impl;

import br.com.fechaki.telephone.v1.data.message.TelephoneStatusUpdaterMessageRequest;
import br.com.fechaki.telephone.v1.queue.TelephoneUpdaterProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelephoneUpdaterProducerImpl implements TelephoneUpdaterProducer {
    private final AmqpTemplate amqpTemplate;

    @Value("${fechaki.telephone.queue.updater.name}")
    private String queueName;

    @Override
    public void sendMessage(TelephoneStatusUpdaterMessageRequest messageRequest) {
        log.info("Sending message to Telephone Status Updater queue: {}", queueName);
        amqpTemplate.convertAndSend(queueName, messageRequest);
    }
}
