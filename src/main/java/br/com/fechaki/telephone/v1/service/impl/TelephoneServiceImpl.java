package br.com.fechaki.telephone.v1.service.impl;

import br.com.fechaki.telephone.exception.type.DuplicateTelephoneException;
import br.com.fechaki.telephone.exception.type.TelephoneNotExistException;
import br.com.fechaki.telephone.repository.TelephoneRepository;
import br.com.fechaki.telephone.v1.data.entity.TelephoneEntity;
import br.com.fechaki.telephone.v1.service.TelephoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TelephoneServiceImpl implements TelephoneService {
    private final TelephoneRepository repository;

    @Override
    public Mono<TelephoneEntity> create(TelephoneEntity entity) {
        return repository.save(entity)
                .onErrorMap(_ -> new DuplicateTelephoneException());
    }

    @Override
    public Mono<TelephoneEntity> read(String id) {
        return repository.findFirstByTelephoneIdAndDeletedFalse(id);
    }

    @Override
    public Mono<TelephoneEntity> read(String countryCode, String areaCoda, String phoneNumber) {
        return repository.findFirstByCountryCodeAndAreaCodeAndPhoneNumberAndDeletedFalse(countryCode, areaCoda, phoneNumber);
    }

    @Override
    public Mono<Void> delete(String id) {
        return repository
                .existsById(id)
                .flatMap(data -> {
                    if(Boolean.TRUE.equals(data)) {
                        return repository.deleteById(id);
                    }
                    else {
                        return Mono.error(new TelephoneNotExistException());
                    }
                });
    }
}
