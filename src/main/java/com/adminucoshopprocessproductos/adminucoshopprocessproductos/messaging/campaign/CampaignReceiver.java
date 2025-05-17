package com.adminucoshopprocessproductos.adminucoshopprocessproductos.messaging.campaign;

import com.adminucoshopprocessproductos.adminucoshopprocessproductos.crosscutting.utils.gson.MapperJsonObject;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.campaign.Campaingns;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.service.campaign.CampaingnsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CampaignReceiver {

    @Autowired
    private CampaingnsService campaingnsService;

    private final MapperJsonObject mapperJsonObject;

    public CampaignReceiver(MapperJsonObject mapperJsonObject) {
        this.mapperJsonObject = mapperJsonObject;
    }

    @RabbitListener(queues = "${products.campaign.process.queue-name-save}")
    public void receiveMessageProcessCampaign(String message) {
        try {
            campaingnsService.saveCampaign(obtenerObjetoDeMensaje(message).get());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private Optional<Campaingns> obtenerObjetoDeMensaje(String mensaje) {
        return mapperJsonObject.execute(mensaje, Campaingns.class);
    }
}