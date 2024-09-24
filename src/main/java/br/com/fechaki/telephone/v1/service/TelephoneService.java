package br.com.fechaki.telephone.v1.service;

import br.com.fechaki.telephone.v1.data.entity.TelephoneEntity;
import reactor.core.publisher.Mono;

public interface TelephoneService {
    Mono<TelephoneEntity> create(TelephoneEntity entity);
    Mono<TelephoneEntity> read(String id);
    Mono<TelephoneEntity> read(String countryCode, String areaCoda, String phoneNumber);
    Mono<Void> delete(String id);
}
