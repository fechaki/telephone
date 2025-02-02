package br.com.fechaki.telephone.v1.queue;

import br.com.fechaki.telephone.v1.data.message.TelephoneMessageRequest;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

public interface TelephoneEnrichmentConsumer {
    @RabbitListener(queues = "${fechaki.telephone.queue.enrichment.name}")
    void receiveMessage(TelephoneMessageRequest message);
}
