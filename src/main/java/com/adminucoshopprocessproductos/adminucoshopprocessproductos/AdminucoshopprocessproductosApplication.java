package com.adminucoshopprocessproductos.adminucoshopprocessproductos;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class AdminucoshopprocessproductosApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdminucoshopprocessproductosApplication.class, args);
	}

}
