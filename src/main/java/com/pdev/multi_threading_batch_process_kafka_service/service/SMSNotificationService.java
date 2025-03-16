package com.pdev.multi_threading_batch_process_kafka_service.service;

import java.util.List;

public interface SMSNotificationService {

    void sendBulkSms(List<String> phoneNumbers);

    List<String> getPhoneNumbersBatch();

    List<String> getPhoneNumbers();
}
