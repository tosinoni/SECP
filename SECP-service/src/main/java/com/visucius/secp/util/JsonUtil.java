package com.visucius.secp.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonUtil {

    public static <T> T convertStringToJson(String object, Class<T> className) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(object,className);
    }

    public static String convertToJsonString(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
