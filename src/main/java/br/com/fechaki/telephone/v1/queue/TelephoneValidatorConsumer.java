package br.com.fechaki.telephone.v1.queue;

import br.com.fechaki.telephone.v1.data.message.TelephoneMessageRequest;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

public interface TelephoneValidatorConsumer {
    @RabbitListener(queues = "${fechaki.telephone.queue.validator.name}")
    void receiveMessage(TelephoneMessageRequest message);
}
