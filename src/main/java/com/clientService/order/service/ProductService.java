package com.clientService.order.service;

import com.clientService.exceptions.NotFoundException;
import com.clientService.order.model.Product;
import com.clientService.order.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product getProductByTicker(String id) {
        return this.productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product with provided ID does not exist"));
    }
}
