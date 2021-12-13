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

    public ResponseEntity<?> getMarketData(){
        List<MarketDataProduct> marketData1= CachedMarketDataService.getMarketDataE1();
        List<MarketDataProduct> marketData2 = CachedMarketDataService.getMarketDataE2();
        return new ResponseEntity<>("hj", HttpStatus.OK);
    }

    public ResponseEntity<?> getAllProducts(){
        LoggerConfig.LOGGER.info("getAllProducts method hit");
        List<Product> productList = this.productRepository.findAll();
        if(productList.isEmpty()){
            return new ResponseEntity<>("No products in database", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(productList, HttpStatus.FOUND);
    }
}
