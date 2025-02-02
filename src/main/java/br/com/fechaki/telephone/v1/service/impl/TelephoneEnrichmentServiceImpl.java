package br.com.fechaki.telephone.v1.service.impl;

import br.com.fechaki.telephone.client.data.response.ClientEnrichmentResponse;
import br.com.fechaki.telephone.exception.type.TelephoneEnrichmentException;
import br.com.fechaki.telephone.repository.TelephoneEnrichmentRepository;
import br.com.fechaki.telephone.v1.data.entity.TelephoneEntity;
import br.com.fechaki.telephone.v1.data.entity.TelephoneEnrichmentEntity;
import br.com.fechaki.telephone.v1.data.message.TelephoneMessageRequest;
import br.com.fechaki.telephone.v1.mapper.TelephoneEnrichmentMapper;
import br.com.fechaki.telephone.v1.mapper.TelephoneMapper;
import br.com.fechaki.telephone.v1.queue.TelephoneEnrichmentProducer;
import br.com.fechaki.telephone.v1.service.TelephoneEnrichmentService;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class TelephoneEnrichmentServiceImpl implements TelephoneEnrichmentService {
    private final TelephoneMapper telephoneMapper;
    private final TelephoneEnrichmentMapper enrichmentMapper;
    private final TelephoneEnrichmentProducer enrichmentProducer;
    private final TelephoneEnrichmentRepository enrichmentRepository;

    @Value("${fechaki.telephone.enrichment.until-days}")
    private int untilDays;

    public TelephoneEnrichmentServiceImpl(TelephoneEnrichmentProducer enrichmentProducer, TelephoneEnrichmentRepository enrichmentRepository) {
        this.telephoneMapper = Mappers.getMapper(TelephoneMapper.class);
        this.enrichmentMapper = Mappers.getMapper(TelephoneEnrichmentMapper.class);
        this.enrichmentProducer = enrichmentProducer;
        this.enrichmentRepository = enrichmentRepository;
    }

    @Override
    public void addQueueEnrichment(TelephoneEntity entity) {
        log.info("Convert Entity to Message and add to queue enrichment");
        TelephoneMessageRequest request = telephoneMapper.toMessageRequest(entity);
        enrichmentProducer.sendMessage(request);
    }

    @Override
    public void saveEnrichment(String telephoneId, ClientEnrichmentResponse response) {
        UUID uuid = UUID.fromString(telephoneId);
        if(!enrichmentRepository.existsById(uuid)) {
            TelephoneEnrichmentEntity entity = enrichmentMapper.toEntity(response);
            entity.setTelephoneId(uuid);
            log.info("Save enrichment Result: {}", entity);
            enrichmentRepository.save(entity);
        }
        else {
            log.info("Enrichment already exists: {}", telephoneId);
            throw new TelephoneEnrichmentException(telephoneId);
        }
    }

    @Override
    public boolean isEnrichmentNeeded(String telephoneId) {
        boolean result = false;
        UUID uuid = UUID.fromString(telephoneId);
        Optional<TelephoneEnrichmentEntity> entity = enrichmentRepository.findById(uuid);
        if(entity.isPresent()) {
            TelephoneEnrichmentEntity validationEntity = entity.get();
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime minusDays = now.minusDays(untilDays);
            log.info("now: {}", now);
            log.info("minusDays: {}", minusDays);
            log.info("validationEntity: {}", validationEntity.getCreated());
            if (validationEntity.getCreated().isBefore(minusDays)) {
                result = true;
            }
        }
        else {
            result = true;
        }
        return result;
    }
}
