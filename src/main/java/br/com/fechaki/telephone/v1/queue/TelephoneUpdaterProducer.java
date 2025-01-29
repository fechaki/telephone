package br.com.fechaki.telephone.v1.queue;

import br.com.fechaki.telephone.v1.data.message.TelephoneStatusUpdaterMessageRequest;

public interface TelephoneUpdaterProducer {
    void sendMessage(TelephoneStatusUpdaterMessageRequest messageRequest);
}
