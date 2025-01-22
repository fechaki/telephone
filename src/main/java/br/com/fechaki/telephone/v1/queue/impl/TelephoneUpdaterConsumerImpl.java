package br.com.fechaki.telephone.v1.queue.impl;

import br.com.fechaki.telephone.v1.data.entity.TelephoneEntity;
import br.com.fechaki.telephone.v1.data.message.TelephoneStatusUpdaterMessageRequest;
import br.com.fechaki.telephone.v1.queue.TelephoneUpdaterConsumer;
import br.com.fechaki.telephone.v1.service.TelephoneService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelephoneUpdaterConsumerImpl implements TelephoneUpdaterConsumer {
    private final TelephoneService service;

    @Override
    public void receiveMessage(TelephoneStatusUpdaterMessageRequest message) {
        TelephoneEntity entity = service.read(message.telephoneId());
        if (entity != null) {
            entity.setActivated(message.activated());
            entity.setValidated(message.validated());
            entity.setUpdated(LocalDateTime.now());
            service.update(entity);
            log.info("Telephone Status Update Done: {}", entity);
        }
    }
}
