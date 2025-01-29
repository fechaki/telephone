package br.com.fechaki.telephone.v1.service.impl;

import br.com.fechaki.telephone.exception.type.DuplicateTelephoneException;
import br.com.fechaki.telephone.exception.type.TelephoneNotExistException;
import br.com.fechaki.telephone.exception.type.TelephoneNotFoundException;
import br.com.fechaki.telephone.repository.TelephoneRepository;
import br.com.fechaki.telephone.v1.data.entity.TelephoneEntity;
import br.com.fechaki.telephone.v1.service.TelephoneService;
import br.com.fechaki.telephone.v1.service.TelephoneValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TelephoneServiceImpl implements TelephoneService {
    private final TelephoneRepository repository;
    private final TelephoneValidationService validationService;

    @Override
    public TelephoneEntity create(TelephoneEntity entity) {
        try {
            TelephoneEntity result = repository.save(entity);
            validationService.addQueueValidation(result);
            return result;
        }
        catch (DuplicateKeyException ex) {
            throw new DuplicateTelephoneException(entity.getCountryCode(), entity.getAreaCode(), entity.getPhoneNumber());
        }
    }

    @Override
    public TelephoneEntity read(String id) {
        TelephoneEntity entity = repository.findFirstByTelephoneIdAndDeletedFalse(UUID.fromString(id));
        if(entity == null) {
            throw new TelephoneNotFoundException(id);
        }
        return entity;
    }

    @Override
    public TelephoneEntity read(String countryCode, String areaCode, String phoneNumber) {
        TelephoneEntity entity = repository.findFirstByCountryCodeAndAreaCodeAndPhoneNumberAndDeletedFalse(countryCode, areaCode, phoneNumber);
        if(entity == null) {
            throw new TelephoneNotFoundException(countryCode + areaCode + phoneNumber);
        }
        return entity;
    }

    @Override
    public void update(TelephoneEntity entity) {
        UUID id = entity.getTelephoneId();
        try {
            if(!repository.existsById(id)) {
                throw new TelephoneNotExistException(id);
            }
            repository.save(entity);
        }
        catch (Exception ex) {
            throw new TelephoneNotExistException(id);
        }
    }

    @Override
    public void delete(String id) {
        boolean result = repository.existsById(UUID.fromString(id));
        if(!result) {
            throw new TelephoneNotExistException(id);
        }
        try {
            repository.deleteById(UUID.fromString(id));
        }
        catch (Exception ex) {
            throw new TelephoneNotExistException(id);
        }
    }
}
