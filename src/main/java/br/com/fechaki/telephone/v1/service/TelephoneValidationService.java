package br.com.fechaki.telephone.v1.service;

import br.com.fechaki.telephone.client.data.response.ClientValidationResponse;
import br.com.fechaki.telephone.v1.data.entity.TelephoneEntity;


public interface TelephoneValidationService {
    void addQueueValidation(TelephoneEntity entity);
    void saveValidation(String telephoneId, ClientValidationResponse entity);
    boolean isValidationNeeded(String telephoneId);
}
