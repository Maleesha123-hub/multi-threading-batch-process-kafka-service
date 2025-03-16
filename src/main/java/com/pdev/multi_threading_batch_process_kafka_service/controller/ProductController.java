package com.pdev.multi_threading_batch_process_kafka_service.controller;

import com.pdev.multi_threading_batch_process_kafka_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/product/v1")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping(value = "/ids")
    public ResponseEntity<List<Long>> getIds() {
        return ResponseEntity.ok(productService.getProductIds());
    }

    @PostMapping(value = "/reset-discount")
    public ResponseEntity<String> resetProductRecords() {
        String response = productService.resetRecords();
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/process-discount")
    public ResponseEntity<String> processDiscount(@RequestBody List<Long> productIds) {
        productService.processDiscount(productIds);
        return ResponseEntity.ok("Products processed and events published.");
    }
}
