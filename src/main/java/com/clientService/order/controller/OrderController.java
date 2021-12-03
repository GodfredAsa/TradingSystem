package com.clientService.order.controller;

import com.clientService.error.ApiError;
import com.clientService.exceptions.NotFoundException;
import com.clientService.order.model.Order;
import com.clientService.order.model.OrderRequest;
import com.clientService.order.service.OrderService;
import com.clientService.order.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/order/")
public class OrderController {

    private final OrderService orderService;
    private final ProductService productService;

    @Autowired
    public OrderController(OrderService orderService, ProductService productService) {
        this.orderService = orderService;
        this.productService = productService;
    }

    @PostMapping("makeOrder")
    public ResponseEntity<String> makeOrder(@Valid @RequestBody OrderRequest orderRequest) {
        String orderId = orderService.makeOrder(orderRequest);

        return new ResponseEntity<String>(orderId, HttpStatus.CREATED);
    }

    @GetMapping("getOrder/{orderId}")
    public Order getOrder(@PathVariable String orderId) {
        return this.orderService.getOrderById(orderId);
    }

}
