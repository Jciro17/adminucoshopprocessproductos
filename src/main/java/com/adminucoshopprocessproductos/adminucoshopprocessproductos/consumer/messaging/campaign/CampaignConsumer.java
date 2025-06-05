package com.adminucoshopprocessproductos.adminucoshopprocessproductos.consumer.messaging.campaign;

import com.adminucoshopprocessproductos.adminucoshopprocessproductos.crosscutting.utils.gson.MapperJsonObjectJackson;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.campaign.Campaigns;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.service.campaign.CampaignsService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class CampaignConsumer {

    private final CampaignsService campaignsService;
    private final MapperJsonObjectJackson mapper;

    public CampaignConsumer(CampaignsService campaignsService, MapperJsonObjectJackson mapper) {
        this.campaignsService = campaignsService;
        this.mapper = mapper;
    }

    @RabbitListener(queues = {"apiproducts.process.save.campaign.qu"})
    public String saveCampaign(String messageBody) {
        try {
            Optional<Campaigns> campaignsOpt = mapper.ejecutar(messageBody, Campaigns.class);
            if (campaignsOpt.isPresent()) {
                Campaigns campaigns = campaignsOpt.get();
                campaignsService.saveCampaign(campaigns);
                return "OK";
            } else {
                String error = "Nose pudó deserializar el mensaje a Campaigns";
                log.error(error);
                return error;
            }
        } catch (Exception ex) {
            String error = "Error al guardar la campaña: " + ex.getMessage();
            log.error(error);
            return error;
        }
    }

    @RabbitListener(queues = {"apiproducts.process.update.campaign.qu"})
    public String updateCampaign(String messageBody) {
        try {
            Optional<Campaigns> campaignsOpt = mapper.ejecutar(messageBody, Campaigns.class);
            if (campaignsOpt.isPresent()) {
                Campaigns campaigns = campaignsOpt.get();
                campaignsService.updateCampaign(campaigns.getId(), campaigns);
                return "OK";
            } else {
                String error = "No se pudó deserializar el mensaje a Campaigns";
                log.error(error);
                return error;
            }
        } catch (Exception ex) {
            String error = "Error al actualizar la campaña: " + ex.getMessage();
            log.error(error, ex);
            return error;
        }
    }

    @RabbitListener(queues = {"apiproducts.process.delete.campaign.qu"})
    public String deleteCampaign(String messageBody) {
        try {
            Optional<Campaigns> campaignsOpt = mapper.ejecutar(messageBody, Campaigns.class);
            if (campaignsOpt.isPresent()) {
                Campaigns campaigns = campaignsOpt.get();
                campaignsService.deleteCampaign(campaigns.getId());
                return "OK";
            } else {
                String error = "No se pudó deserializar el mensaje a Campaigns";
                log.error(error);
                return error;
            }
        } catch (Exception ex) {
            String error = "Error al eliminar la campaña " + ex.getMessage();
            log.error(error, ex);
            return error;
        }
    }
}