package com.pdev.multi_threading_batch_process_kafka_service.service;

import java.util.List;

public interface ProductService {

    List<Long> getProductIds();

    void processDiscount(List<Long> productIds);

    String resetRecords();
}
