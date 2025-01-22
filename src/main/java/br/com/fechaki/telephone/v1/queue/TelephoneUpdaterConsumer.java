package br.com.fechaki.telephone.v1.queue;

import br.com.fechaki.telephone.v1.data.message.TelephoneStatusUpdaterMessageRequest;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

public interface TelephoneUpdaterConsumer {
    @RabbitListener(queues = "${fechaki.telephone.queue.updater.name}")
    void receiveMessage(TelephoneStatusUpdaterMessageRequest message);
}
