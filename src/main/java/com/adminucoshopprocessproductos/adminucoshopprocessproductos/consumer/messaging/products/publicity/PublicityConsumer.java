package com.adminucoshopprocessproductos.adminucoshopprocessproductos.consumer.messaging.products.publicity;

import com.adminucoshopprocessproductos.adminucoshopprocessproductos.crosscutting.utils.gson.MapperJsonObjectJackson;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.publicity.PublicityDomain;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.service.publicity.PublicityService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class PublicityConsumer {

    private final PublicityService publicityService;
    private final MapperJsonObjectJackson mapper;

    public PublicityConsumer(PublicityService publicityService, MapperJsonObjectJackson mapper) {
        this.publicityService = publicityService;
        this.mapper = mapper;
    }

    @RabbitListener(queues = {"apiproducts.process.save.publicity.qu"})
    public String createPublicity(String messageBody) {
        try {
            Optional<PublicityDomain> publicityOpt = mapper.ejecutar(messageBody, PublicityDomain.class);

            if (publicityOpt.isPresent()) {
                PublicityDomain publicity = publicityOpt.get();
                publicityService.createPublicity(publicity);

                return "OK";
            } else {
                String error = "No se pudo deserializar el mensaje a Publicity.";
                log.error(error);
                return error;
            }
        } catch (Exception ex) {
            String error = "Error al guardar la publicidad: " + ex.getMessage();
            log.error(error, ex);
            return error;
        }
    }

    @RabbitListener(queues = {"apiproducts.process.update.publicity.qu"})
    public String updatePublicity(String messageBody) {
        try {
            Optional<PublicityDomain> publicityOpt = mapper.ejecutar(messageBody, PublicityDomain.class);

            if (publicityOpt.isPresent()) {
                PublicityDomain publicity = publicityOpt.get();
                publicityService.updatePublicity(publicity.getId(), publicity);
                return "OK";
            } else {
                String error = "No se pudo deserializar la actualzicion a publicity.";
                log.error(error);
                return error;
            }
        } catch (Exception ex) {
            String error = "Error al actualizar la publicidad: " + ex.getMessage();
            log.error(error, ex);
            return error;
        }
    }

    @RabbitListener(queues = {"apiproducts.process.delete.publicity.qu"})
    public String deletePublicity(String messageBody) {
        try {
            Optional<PublicityDomain> publicityOpt = mapper.ejecutar(messageBody, PublicityDomain.class);

            if (publicityOpt.isPresent()) {
                PublicityDomain publicity = publicityOpt.get();
                publicityService.deletePublicity(publicity.getId());
                return "OK";
            } else {
                String error = "No se pudo deserializar el mensaje a publicity.";
                log.error(error);
                return error;
            }
        } catch (Exception ex) {
            String error = "Error al eliminar la publicidad: " + ex.getMessage();
            log.error(error, ex);
            return error;
        }
    }

}
