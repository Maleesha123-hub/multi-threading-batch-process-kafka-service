package com.pdev.multi_threading_batch_process_kafka_service.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pdev.multi_threading_batch_process_kafka_service.model.Product;
import com.pdev.multi_threading_batch_process_kafka_service.repository.ProductRepository;
import com.pdev.multi_threading_batch_process_kafka_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final String topicName = "PRODUCT_DISCOUNT_UPDATE";

    @Override
    public List<Long> getProductIds() {
        return productRepository.findAll()
                .stream()
                .map(Product::getId)
                .collect(Collectors.toList());
    }

    @Override
    public void processDiscount(List<Long> productIds) {
        productIds.parallelStream()
                .forEach(this::fetchUpdateAndPublish);
    }

    @Override
    public String resetRecords() {
        productRepository.findAll()
                .forEach(product -> {
                    product.setOfferApplied(false);
                    product.setPriceAfterDiscount(product.getPrice());
                    product.setDiscountPercentage(0);
                    productRepository.save(product);
                });
        return "Data Reset to DB";
    }

    private void fetchUpdateAndPublish(Long productId) {

        //fetch product by id
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product ID does not exist in the system"));

        //update discount properties
        updateDiscountedPrice(product);

        //save to DB
        productRepository.save(product);

        //kafka events
    }

    private void updateDiscountedPrice(Product product) {

        double price = product.getPrice();

        int discountPercentage = (price >= 1000) ? 10 : (price > 500 ? 5 : 0);

        double priceAfterDiscount = price - (price * discountPercentage / 100);

        if (discountPercentage > 0) {
            product.setOfferApplied(true);
        }
        product.setDiscountPercentage(discountPercentage);
        product.setPriceAfterDiscount(priceAfterDiscount);
    }

}
