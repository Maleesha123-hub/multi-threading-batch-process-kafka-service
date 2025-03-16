package com.pdev.multi_threading_batch_process_kafka_service.service.v2;

import java.util.List;

public interface ProductServiceV2 {

    void processDiscount(List<Long> productIds);
}
