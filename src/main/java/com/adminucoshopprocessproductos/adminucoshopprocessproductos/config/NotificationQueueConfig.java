package com.adminucoshopprocessproductos.adminucoshopprocessproductos.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationQueueConfig {

    // Nombre de la cola
    public static final String QUEUE_NAME = "apiproducts.process.save.notification.qu";

    // Exchange y routingKey opcionalmente si los necesitas
    public static final String EXCHANGE_NAME = "apiproducts.process.save.notification.ex";
    public static final String ROUTING_KEY = "products.notification.process.queues.save.routingkey";

    @Bean
    public Queue notificationQueue() {
        return QueueBuilder.durable(QUEUE_NAME).build();
    }

    @Bean
    public DirectExchange notificationExchange() {
        return ExchangeBuilder.directExchange(EXCHANGE_NAME).durable(true).build();
    }

    @Bean
    public Binding notificationBinding() {
        return BindingBuilder.bind(notificationQueue()).to(notificationExchange()).with(ROUTING_KEY);
    }

    // Bean para administrar colas al inicio
    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }
}
