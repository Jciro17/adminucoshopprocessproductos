package com.adminucoshopprocessproductos.adminucoshopprocessproductos.crosscutting.utils.gson;

import java.util.Optional;

public interface MapperJsonObject {

    Optional<String> execute(Object object);

    <T> Optional<T> execute(Object object, Class<T> DestinationClass);

    Optional<String> gsonExecute(Object object);

}
