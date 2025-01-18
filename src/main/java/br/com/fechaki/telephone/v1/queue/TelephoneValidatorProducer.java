package br.com.fechaki.telephone.v1.queue;

import br.com.fechaki.telephone.v1.data.message.TelephoneMessageRequest;

public interface TelephoneValidatorProducer {
    void sendMessage(TelephoneMessageRequest messageRequest);
}
