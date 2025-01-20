package br.com.fechaki.telephone.v1.service.impl;

import br.com.fechaki.telephone.client.data.response.ClientValidationResponse;
import br.com.fechaki.telephone.exception.type.TelephoneValidationException;
import br.com.fechaki.telephone.repository.TelephoneValidationRepository;
import br.com.fechaki.telephone.v1.data.entity.TelephoneEntity;
import br.com.fechaki.telephone.v1.data.entity.TelephoneValidationEntity;
import br.com.fechaki.telephone.v1.data.message.TelephoneMessageRequest;
import br.com.fechaki.telephone.v1.mapper.TelephoneMapper;
import br.com.fechaki.telephone.v1.mapper.TelephoneValidationMapper;
import br.com.fechaki.telephone.v1.queue.TelephoneValidatorProducer;
import br.com.fechaki.telephone.v1.service.TelephoneValidationService;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class TelephoneValidationServiceImpl implements TelephoneValidationService {
    private final TelephoneMapper telephoneMapper;
    private final TelephoneValidationMapper validationMapper;
    private final TelephoneValidatorProducer validatorProducer;
    private final TelephoneValidationRepository validationRepository;

    @Value("${fechaki.telephone.validation.until-days}")
    private int untilDays;

    public TelephoneValidationServiceImpl(TelephoneValidatorProducer validatorProducer, TelephoneValidationRepository validationRepository) {
        this.telephoneMapper = Mappers.getMapper(TelephoneMapper.class);
        this.validationMapper = Mappers.getMapper(TelephoneValidationMapper.class);
        this.validatorProducer = validatorProducer;
        this.validationRepository = validationRepository;
    }

    @Override
    public void addQueueValidation(TelephoneEntity entity) {
        log.info("Convert Entity to Message and add to queue validation");
        TelephoneMessageRequest request = telephoneMapper.toMessageRequest(entity);
        validatorProducer.sendMessage(request);
    }

    @Override
    public void saveValidation(String telephoneId, ClientValidationResponse response) {
        UUID uuid = UUID.fromString(telephoneId);
        if(!validationRepository.existsById(uuid)) {
            TelephoneValidationEntity entity = validationMapper.toEntity(response);
            entity.setTelephoneId(uuid);
            log.info("Save validation Result: {}", entity);
            validationRepository.save(entity);
        }
        else {
            log.info("Validation already exists: {}", telephoneId);
            throw new TelephoneValidationException(telephoneId);
        }
    }

    @Override
    public boolean isValidationNeeded(String telephoneId) {
        boolean result = false;
        UUID uuid = UUID.fromString(telephoneId);
        Optional<TelephoneValidationEntity> entity = validationRepository.findById(uuid);
        if(entity.isPresent()) {
            TelephoneValidationEntity validationEntity = entity.get();
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
