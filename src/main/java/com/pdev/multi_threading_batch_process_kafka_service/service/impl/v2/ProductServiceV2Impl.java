package com.pdev.multi_threading_batch_process_kafka_service.service.impl.v2;

import com.pdev.multi_threading_batch_process_kafka_service.model.Product;
import com.pdev.multi_threading_batch_process_kafka_service.repository.ProductRepository;
import com.pdev.multi_threading_batch_process_kafka_service.service.v2.ProductServiceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class ProductServiceV2Impl implements ProductServiceV2 {

    private final ProductRepository productRepository;
    private final ExecutorService executorService = Executors.newFixedThreadPool(6);

    @Override
    public void processDiscount(List<Long> productIds) {
        List<List<Long>> batches = splitIntoBatches(productIds, 50);

        List<CompletableFuture<Void>> futures = batches
                .stream()
                .map(
                        batch -> CompletableFuture.runAsync(() -> processProductIds(batch), executorService))
                .toList();

        //wait for all future to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    private void processProductIds(List<Long> batch) {
        System.out.println("Processing batch " + batch + " by thread " + Thread.currentThread().getName());
        batch.forEach(this::fetchUpdateAndPublish);
    }

    private void fetchUpdateAndPublish(Long productId) {

        //fetch product by id
        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Product ID does not exist in the system")
                );

        //update discount properties
        updateDiscountedPrice(product);

        //save to DB
        productRepository.save(product);
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

    //batch List<Long> 1-50 , 2-100  ..
    private List<List<Long>> splitIntoBatches(List<Long> productIds, int batchSize) {

        int totalSize = productIds.size();
        //300 - 100 -> 3
        int batchNums = (totalSize + batchSize - 1) / batchSize;
        //calculate number of batch
        List<List<Long>> batches = new ArrayList<>();

        for (int i = 0; i < batchNums; i++) {
            int start = i * batchSize;// 0 , 51 ,100
            int end = Math.min(totalSize, (i + 1) * batchSize);// 50 , 100
            batches.add(productIds.subList(start, end));
        }


        return batches;
    }
}
