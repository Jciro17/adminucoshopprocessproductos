package com.adminucoshopprocessproductos.adminucoshopprocessproductos.messaging.notification;

import com.adminucoshopprocessproductos.adminucoshopprocessproductos.config.NotificationQueueConfig;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.crosscutting.utils.MessageSender;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.crosscutting.utils.gson.MapperJsonObject;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.notification.NotificationDomain;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MessageSenderBroker implements MessageSender<NotificationDomain> {

    private final RabbitTemplate rabbitTemplate;
    private final MapperJsonObject mapperJsonObject;
    private final NotificationQueueConfig notificationQueueConfig;

    public MessageSenderBroker(RabbitTemplate rabbitTemplate, MapperJsonObject mapperJsonObject, NotificationQueueConfig notificationQueueConfig) {
        this.rabbitTemplate = rabbitTemplate;
        this.mapperJsonObject = mapperJsonObject;
        this.notificationQueueConfig = notificationQueueConfig;
    }

    @Override
    public void execute(NotificationDomain message, Object object) {
        MessageProperties propiedadesMensaje = generarPropiedadesMensaje(object.toString());

        Optional<Message> cuerpoMensaje = obtenerCuerpoMensaje(message, propiedadesMensaje);
        cuerpoMensaje.ifPresent(msg ->
                rabbitTemplate.convertAndSend(
                        notificationQueueConfig.getExchangeName(),
                        notificationQueueConfig.getRoutingKeyName(),
                        msg
                )
        );
    }

    private MessageProperties generarPropiedadesMensaje(String idMessageSender) {
        return MessagePropertiesBuilder.newInstance()
                .setContentType("application/json")
                .setHeader("idMensaje", idMessageSender)
                .build();
    }

    private Optional<Message> obtenerCuerpoMensaje(Object mensaje, MessageProperties propiedadesMensaje) {
        Optional<String> textoMensaje = mapperJsonObject.gsonExecute(mensaje);
        return textoMensaje.map(msg -> new Message(msg.getBytes(), propiedadesMensaje));
    }


}
