package com.pdev.multi_threading_batch_process_kafka_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface TwilioSmsService {

    void sendSms(List<String> bulk, String message) throws JsonProcessingException;
}
