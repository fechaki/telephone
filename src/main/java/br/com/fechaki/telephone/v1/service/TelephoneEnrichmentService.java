package br.com.fechaki.telephone.v1.service;

import br.com.fechaki.telephone.client.data.response.ClientEnrichmentResponse;
import br.com.fechaki.telephone.v1.data.entity.TelephoneEntity;


public interface TelephoneEnrichmentService {
    void addQueueEnrichment(TelephoneEntity entity);
    void saveEnrichment(String telephoneId, ClientEnrichmentResponse entity);
    boolean isEnrichmentNeeded(String telephoneId);
}
