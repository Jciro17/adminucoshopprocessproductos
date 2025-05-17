package com.adminucoshopprocessproductos.adminucoshopprocessproductos.messaging.notification;

import com.adminucoshopprocessproductos.adminucoshopprocessproductos.crosscutting.utils.gson.MapperJsonObject;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.notification.NotificationDomain;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.service.notification.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ReceiverMessagesBroker {

    @Autowired
    private NotificationService notificationService;

    private final MapperJsonObject mapperJsonObjeto;

    public ReceiverMessagesBroker(MapperJsonObject mapperJsonObjeto) {
        this.mapperJsonObjeto = mapperJsonObjeto;
    }


    @RabbitListener(queues = "${notification.queue-recibir.notification.queue-name}")
    public void receiveMessageProcessClient(String message) {
        try {
            notificationService.saveNotification(obtenerObjetoDeMensaje(message).get());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private Optional<NotificationDomain> obtenerObjetoDeMensaje(String mensaje) {
        return mapperJsonObjeto.execute(mensaje, NotificationDomain.class);
    }


}