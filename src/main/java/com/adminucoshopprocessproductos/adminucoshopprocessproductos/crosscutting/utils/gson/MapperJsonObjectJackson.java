package com.adminucoshopprocessproductos.adminucoshopprocessproductos.crosscutting.utils.gson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

@Component
public class MapperJsonObjectJackson implements MapperJsonObject {

    @Override
    public Optional<String> ejecutar(Object objeto) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            return Optional.ofNullable(objectMapper.writeValueAsString(objeto));
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
    }

    @Override
    public <T> Optional<T> ejecutar(String json, Class<T> claseDestino) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            return Optional.ofNullable(objectMapper.readValue(json, claseDestino));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> ejecutarGson(Object objeto) {
        try {
            Gson gson = new GsonBuilder()
                    .serializeNulls()
                    // Serializador/Deserializador para LocalDateTime
                    .registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                        @Override
                        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                            return LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_DATE_TIME);
                        }
                    })
                    .registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
                        @Override
                        public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
                            return new JsonPrimitive(src.format(DateTimeFormatter.ISO_DATE_TIME));
                        }
                    })
                    // Serializador/Deserializador para Date
                    .registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                        @Override
                        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                            try {
                                return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse(json.getAsString());
                            } catch (ParseException e) {
                                throw new JsonParseException(e);
                            }
                        }
                    })
                    .registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
                        @Override
                        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
                            return new JsonPrimitive(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").format(src));
                        }
                    })
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
                    .create();
            String json = gson.toJson(objeto);
            return Optional.ofNullable(json);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}