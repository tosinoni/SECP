package com.visucius.secp.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.visucius.secp.DTO.MessageResponseDTO;
import com.visucius.secp.models.Message;
import org.atmosphere.config.managed.Decoder;
import org.atmosphere.config.managed.Encoder;

import javax.inject.Inject;
import java.io.IOException;

public class ChatEncoder implements Decoder<String, MessageResponseDTO>, Encoder<MessageResponseDTO, String> {
    @Inject
    private ObjectMapper mapper;

    @Override
    public MessageResponseDTO decode(String s) {
        try {
            return mapper.readValue(s, MessageResponseDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String encode(MessageResponseDTO m) {
        try {
            return mapper.writeValueAsString(m);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
