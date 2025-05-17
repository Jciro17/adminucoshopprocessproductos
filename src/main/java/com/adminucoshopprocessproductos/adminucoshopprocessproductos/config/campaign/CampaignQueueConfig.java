package com.adminucoshopprocessproductos.adminucoshopprocessproductos.config.campaign;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "products.campaign.process")
@PropertySource("classpath:products.properties")
public class CampaignQueueConfig {

    private String routingkeyNameSave;
    private String exchangeNameSave;
    private String queueNameSave;
    private String routingkeyNameDelete;
    private String exchangeNameDelete;
    private String queueNameDelete;
    private String routingkeyNameUpdate;
    private String exchangeNameUpdate;
    private String queueNameUpdate;

    public String getExchangeNameSave() {
        return exchangeNameSave;
    }

    public String getRoutingkeyNameSave() {
        return routingkeyNameSave;
    }

    public void setRoutingkeyNameSave(String routingkeyNameSave) {
        this.routingkeyNameSave = routingkeyNameSave;
    }

    public String getQueueNameSave() {
        return queueNameSave;
    }

    public void setExchangeNameSave(String exchangeNameSave) {
        this.exchangeNameSave = exchangeNameSave;
    }

    public void setQueueNameSave(String queueNameSave) {
        this.queueNameSave = queueNameSave;
    }

    public String getExchangeNameDelete() {
        return exchangeNameDelete;
    }

    public String getRoutingkeyNameDelete() {
        return routingkeyNameDelete;
    }

    public void setRoutingkeyNameDelete(String routingkeyNameDelete) {
        this.routingkeyNameDelete = routingkeyNameDelete;
    }

    public String getQueueNameDelete() {
        return queueNameDelete;
    }

    public void setExchangeNameDelete(String exchangeNameDelete) {
        this.exchangeNameDelete = exchangeNameDelete;
    }

    public void setQueueNameDelete(String queueNameDelete) {
        this.queueNameDelete = queueNameDelete;
    }

    public String getExchangeNameUpdate() {
        return exchangeNameUpdate;
    }

    public String getRoutingkeyNameUpdate() {
        return routingkeyNameUpdate;
    }

    public void setRoutingkeyNameUpdate(String routingkeyNameUpdate) {
        this.routingkeyNameUpdate = routingkeyNameUpdate;
    }

    public String getQueueNameUpdate() {
        return queueNameUpdate;
    }

    public void setExchangeNameUpdate(String exchangeNameUpdate) {
        this.exchangeNameUpdate = exchangeNameUpdate;
    }

    public void setQueueNameUpdate(String queueNameUpdate) {
        this.queueNameUpdate = queueNameUpdate;
    }
}