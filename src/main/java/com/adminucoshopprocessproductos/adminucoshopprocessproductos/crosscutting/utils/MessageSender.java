package com.adminucoshopprocessproductos.adminucoshopprocessproductos.crosscutting.utils;

public interface MessageSender<T> {

    void execute(T message, Object object);

}
