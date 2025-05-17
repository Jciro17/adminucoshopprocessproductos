package com.adminucoshopprocessproductos.adminucoshopprocessproductos.crosscutting.utils.gson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
public class MapperJsonObjectJackson implements MapperJsonObject {

    @Override
    public Optional<String> execute(Object object) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return Optional.ofNullable(objectMapper.writeValueAsString(object));
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
    }

    @Override
    public <T> Optional<T> execute(Object json, Class<T> destinationClass) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            if (json instanceof String jsonString) {
                return Optional.ofNullable(objectMapper.readValue(jsonString, destinationClass));
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> gsonExecute(Object object) {
        try {
            Gson gson = new GsonBuilder().serializeNulls()
                    .registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                        @Override
                        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                            return LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                        }
                    })
                    .create();
            String jsonString = gson.toJson(object);
            return Optional.ofNullable(jsonString);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
