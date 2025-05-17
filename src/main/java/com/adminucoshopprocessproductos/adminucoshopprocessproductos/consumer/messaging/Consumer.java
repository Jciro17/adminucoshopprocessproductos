package com.adminucoshopprocessproductos.adminucoshopprocessproductos.consumer.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

    private static final Logger log = LoggerFactory.getLogger(Consumer.class);

    @RabbitListener(queues = {"${sacavix.queue.name}"})
    public void receivedInvoiceMessage (@Payload String message){
        log.info(message);
        makeSlow();
    }

    private void makeSlow(){
        try {
            Thread.sleep(5000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
