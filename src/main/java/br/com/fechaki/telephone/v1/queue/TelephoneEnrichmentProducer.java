package br.com.fechaki.telephone.v1.queue;

import br.com.fechaki.telephone.v1.data.message.TelephoneMessageRequest;

public interface TelephoneEnrichmentProducer {
    void sendMessage(TelephoneMessageRequest messageRequest);
}
