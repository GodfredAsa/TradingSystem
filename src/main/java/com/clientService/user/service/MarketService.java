package com.clientService.user.service;

import com.clientService.loggerPack.LoggerConfig;
import com.clientService.order.model.MarketDataProduct;
import com.clientService.order.model.Product;
import com.clientService.order.repository.ProductRepository;
import com.clientService.order.service.CachedMarketDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarketService {
    private final ProductRepository productRepository;

    public MarketService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ResponseEntity<?> getExchange1Data(){
        List<MarketDataProduct> marketData1= CachedMarketDataService.getMarketDataE1();

        return new ResponseEntity<>(marketData1, HttpStatus.OK);
    }

    public ResponseEntity<?> getExchange2Data(){
        List<MarketDataProduct> marketData2= CachedMarketDataService.getMarketDataE2();

        return new ResponseEntity<>(marketData2, HttpStatus.OK);
    }

    public ResponseEntity<?> getAllProducts(){
        List<Product> productList = this.productRepository.findAll();
        if(productList.isEmpty()){
            return new ResponseEntity<>("No products in database", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(productList, HttpStatus.FOUND);
    }
}
