package com.pdev.multi_threading_batch_process_kafka_service.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pdev.multi_threading_batch_process_kafka_service.repository.PersonRepository;
import com.pdev.multi_threading_batch_process_kafka_service.service.SMSNotificationService;
import com.pdev.multi_threading_batch_process_kafka_service.service.TwilioSmsService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

@Service
public class SMSNotificationServiceImpl implements SMSNotificationService {

    private final PersonRepository personRepository;
    private final TwilioSmsService twilioSmsService;
    private final Executor smsExecutor;

    public SMSNotificationServiceImpl(PersonRepository personRepository,
                                      TwilioSmsService twilioSmsService,
                                      @Qualifier("createSmsExecutorService") Executor smsExecutor) {
        this.personRepository = personRepository;
        this.twilioSmsService = twilioSmsService;
        this.smsExecutor = smsExecutor;
    }

    @Override
    public void sendBulkSms(List<String> phoneNumbers) {

        String message = "Happy New Year! Wishing you joy and success!";

        List<List<String>> batches = splitInToBatches(phoneNumbers);

        for (List<String> batch : batches) {
            smsExecutor.execute(() -> {

                // Print the current thread name
                System.out.println("Processing batch " + "batch" + " in thread " + Thread.currentThread().getName());
                sendSmsToBatch(batch, message); // Submitting task to executor
            });
        }
    }

    // Helper method to send SMS to a batch
    private void sendSmsToBatch(List<String> batch, String message) {
        try {
            twilioSmsService.sendSms(batch, message); // Call to actual service
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error sending SMS", e);
        }
    }

    private List<List<String>> splitInToBatches(List<String> phoneNumbers) {

        List<List<String>> batches = new ArrayList<>();

        int CHUNK_SIZE = 200;
        int phoneNumbersCount = phoneNumbers.size();

        for (int i = 0; phoneNumbersCount >= i; i += CHUNK_SIZE) {
            int toIndex = Math.min(i + CHUNK_SIZE, phoneNumbers.size());
            List<String> batch = phoneNumbers.subList(i, toIndex);
            batches.add(batch);
        }

        return batches;
    }

    @Override
    public List<String> phoneNumbers() {
        return personRepository.findPhone();
    }
}
