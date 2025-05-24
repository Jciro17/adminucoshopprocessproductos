package com.adminucoshopprocessproductos.adminucoshopprocessproductos.consumer.messaging.product_management;

import com.adminucoshopprocessproductos.adminucoshopprocessproductos.crosscutting.utils.gson.MapperJsonObjectJackson;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.product_management.ProductDomain;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.service.product_management.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProductManagamentConsumer {

    private static final Logger log = LoggerFactory.getLogger(ProductManagamentConsumer.class);

    private final ProductService productService;
    private final MapperJsonObjectJackson mapper;

    public ProductManagamentConsumer(ProductService productService, MapperJsonObjectJackson mapper) {
        this.productService = productService;
        this.mapper = mapper;
    }

    @RabbitListener(queues = {"apiproducts.process.save.product.qu"})
    public String saveProduct(Object object) {

        try {
            Optional<ProductDomain> productDomain = mapper.execute(object, ProductDomain.class);

            if (productDomain.isPresent()) {
                ProductDomain product = productDomain.get();
                productService.saveProduct(product);
                return "OK";
            } else {
                String error = "No se pudo deserializar el mensaje a Product";
                log.error(error);
                return error;
            }

        } catch (Exception ex) {
            String error = "Error al intentar guardar el producto: " + ex.getMessage();
            log.error(error, ex);
            return error;
        }
    }

    @RabbitListener(queues = {"apiproducts.process.update.product.qu"})
    public String updateProduct(Object object) {
        try {
            Optional<ProductDomain> productDomain = mapper.execute(object, ProductDomain.class);

            if (productDomain.isPresent()) {
                ProductDomain product = productDomain.get();
                productService.updateProduct(product.getProductId(), product);
                return "OK";
            } else {
                String error = "No se pudo deserializar el mensaje a producto (UPDATE)";
                log.error(error);
                return error;
            }

        } catch (Exception ex) {
            String error = "Error al tratar de actualizar el producto... " + ex.getMessage();
            log.error(error, ex);
            return error;
        }
    }


    @RabbitListener(queues = {"apiproducts.process.delete.product.qu"})
    public String deleteProduct(Object object) {
        try {
            Optional<ProductDomain> productDomain = mapper.execute(object, ProductDomain.class);

            if (productDomain.isPresent()) {
                ProductDomain product = productDomain.get();
                productService.deleteProduct(product.getProductId());
                return "OK";
            } else {
                String error = "No se pudo deserializar el mensaje a producto";
                log.error(error);
                return error;
            }

        } catch (Exception ex) {
            String error = "Error al tratar de eliminar el producto... " + ex.getMessage();
            log.error(error, ex);
            return error;
        }
    }
}
