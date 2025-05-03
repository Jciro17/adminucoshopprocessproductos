package com.adminucoshopprocessproductos.adminucoshopprocessproductos.crosscutting.exceptions;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.ucoshopapi.ucoshopapi.controllers.category.CategoryController;
import com.ucoshopapi.ucoshopapi.controllers.notification.NotificationController;
import com.ucoshopapi.ucoshopapi.controllers.payment_management.BankDomainController;
import com.ucoshopapi.ucoshopapi.controllers.payment_management.BuyDomainController;
import com.ucoshopapi.ucoshopapi.controllers.payment_management.PaymentDomainController;
import com.ucoshopapi.ucoshopapi.controllers.payment_management.PaymentMethodDomainController;
import com.ucoshopapi.ucoshopapi.controllers.product_management.ProductDomainController;
import com.ucoshopapi.ucoshopapi.domain.error.ErrorDomain;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.NoSuchElementException;
import java.util.UUID;

@ControllerAdvice(assignableTypes = {CategoryController.class, ProductDomainController.class, NotificationController.class, BankDomainController.class, BuyDomainController.class, PaymentDomainController.class, PaymentMethodDomainController.class})
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDomain> handleRuntimeException(RuntimeException ex) {
        ErrorDomain error = new ErrorDomain("Error de negocio", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(JsonMappingException.class)
    public ResponseEntity<ErrorDomain> handleJsonMappingException(JsonMappingException ex) {
        ErrorDomain error = new ErrorDomain("Error de formato", "Los datos proporcionados tienen un formato incorrecto.");
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorDomain> handleNoSuchElementExceptionException(NoSuchElementException ex) {
        ErrorDomain error = new ErrorDomain("Error de negocio", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorDomain> handleIllegalStateExceptionException(IllegalStateException ex) {
        ErrorDomain error = new ErrorDomain("Error de negocio", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDomain> handleIllegalArgumentExceptionException(IllegalArgumentException ex) {
        ErrorDomain error = new ErrorDomain("Error de negocio", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }



        @ExceptionHandler(MethodArgumentTypeMismatchException.class)
        public ResponseEntity<ErrorDomain> handleUUIDErrors(MethodArgumentTypeMismatchException ex) {
            if (ex.getRequiredType() == UUID.class) {
                String message = ex.getMessage();

                if (message != null && message.contains("Invalid UUID string: null")) {
                    return ResponseEntity.badRequest()
                            .body(new ErrorDomain("Error de negocio", "El ID no puede ser null."));
                } else {
                    return ResponseEntity.badRequest()
                            .body(new ErrorDomain("Error de negocio", "El ID no tiene un formato UUID válido."));
                }
            }

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }



    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDomain> handleGeneralException() {
        ErrorDomain error = new ErrorDomain("Error inesperado ", "ha sucedido un error inesperado");
        return ResponseEntity.internalServerError().body(error);
    }
}
