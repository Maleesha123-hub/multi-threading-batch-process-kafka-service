package com.pdev.multi_threading_batch_process_kafka_service.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pdev.multi_threading_batch_process_kafka_service.service.TwilioSmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TwilioSmsServiceImpl implements TwilioSmsService {

    private final ObjectMapper objectMapper;

    @Override
    public void sendSms(List<String> bulk, String message) throws JsonProcessingException {
        String jsonOutput = objectMapper.writeValueAsString(bulk);
        System.out.println(message);
        System.out.println("Sms is sending to: " + jsonOutput);
    }
}
