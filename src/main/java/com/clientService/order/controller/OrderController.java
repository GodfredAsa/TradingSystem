package com.clientService.order.controller;

import com.clientService.order.model.OrderModel;
import com.clientService.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/order/")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("makeOrder")
    public ResponseEntity<String> makeOrder(@Valid @RequestBody OrderModel order) {
        String orderId = orderService.makeOrder(order);

        return new ResponseEntity<String>(orderId, HttpStatus.OK);
    }

}
