package com.adminucoshopprocessproductos.adminucoshopprocessproductos.consumer.messaging.notification;

import com.adminucoshopprocessproductos.adminucoshopprocessproductos.crosscutting.utils.gson.MapperJsonObjectJackson;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.notification.NotificationDomain;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.service.notification.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Slf4j
@Component
public class NotificationConsumer {

    private final NotificationService notificationService;
    private final MapperJsonObjectJackson mapper;

    public NotificationConsumer(NotificationService notificationService, MapperJsonObjectJackson mapper) {
        this.notificationService = notificationService;
        this.mapper = mapper;
    }

    @RabbitListener(queues = {"apiproducts.process.save.notification.qu"})
    public String saveNotification(String messageBody) {
        log.info("📥 [SAVE] Mensaje recibido: {}", messageBody);

        try {
            Optional<NotificationDomain> notificationOpt = mapper.ejecutar(messageBody, NotificationDomain.class);
            if (notificationOpt.isPresent()) {
                notificationService.saveNotification(notificationOpt.get());
                return "OK"; // ✅ Respuesta que espera el publicador
            } else {
                log.warn("⚠️ No se pudo deserializar el mensaje.");
                return "Deserialización fallida";
            }
        } catch (Exception ex) {
            log.error("❌ Error al guardar la notificación: {}", ex.getMessage(), ex);
            return "Error en el consumidor al guardar";
        }
    }

    @RabbitListener(queues = {"apiproducts.process.delete.notification.qu"})
    public String deleteNotification(String messageBody) {
        log.info("📥 [DELETE] Mensaje recibido: {}", messageBody);

        try {
            Optional<NotificationDomain> notificationOpt = mapper.ejecutar(messageBody, NotificationDomain.class);
            if (notificationOpt.isPresent()) {
                notificationService.deleteNotification(notificationOpt.get().getIdNotification());
                return "OK";
            } else {
                log.warn("⚠️ No se pudo deserializar el mensaje.");
                return "Deserialización fallida";
            }
        } catch (Exception ex) {
            log.error("❌ Error al eliminar la notificación: {}", ex.getMessage(), ex);
            return "Error en el consumidor al eliminar";
        }
    }
}