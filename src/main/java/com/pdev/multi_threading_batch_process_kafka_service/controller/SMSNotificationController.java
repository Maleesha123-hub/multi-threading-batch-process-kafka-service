package com.pdev.multi_threading_batch_process_kafka_service.controller;

import com.pdev.multi_threading_batch_process_kafka_service.service.SMSNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/sms/v1")
public class SMSNotificationController {

    private final SMSNotificationService smsNotificationService;

    @PostMapping(value = "/send-bulk")
    public ResponseEntity<String> sendBulkSms(@RequestBody List<String> phoneNumbers) {
        smsNotificationService.sendBulkSms(phoneNumbers);
        return ResponseEntity.ok("Bulk SMS sending initiated.");
    }

    @GetMapping(value = "/get-phone-numbers")
    public ResponseEntity<List<String>> phoneNumbers() {
        return ResponseEntity.ok(smsNotificationService.phoneNumbers());
    }
}
