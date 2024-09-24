package br.com.fechaki.telephone.v1.service;

import br.com.fechaki.telephone.v1.data.entity.TelephoneEntity;

public interface TelephoneService {
    TelephoneEntity create(TelephoneEntity entity);
    TelephoneEntity read(String id);
    TelephoneEntity read(String countryCode, String areaCoda, String phoneNumber);
    void delete(String id);
}
