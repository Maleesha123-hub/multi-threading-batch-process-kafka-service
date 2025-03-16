package com.pdev.multi_threading_batch_process_kafka_service.controller.v2;

import com.pdev.multi_threading_batch_process_kafka_service.service.v2.ProductServiceV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/product/v2")
public class ProductControllerV2 {

    @Autowired
    private ProductServiceV2 productServiceV2;

    @PostMapping(value = "/process-discount")
    public ResponseEntity<String> processDiscount(@RequestBody List<Long> productIds) {
        productServiceV2.processDiscount(productIds);
        return ResponseEntity.ok("Products processed and events published.");
    }
}
