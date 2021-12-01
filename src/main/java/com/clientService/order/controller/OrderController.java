package com.clientService.order.controller;

import com.clientService.order.model.OrderModel;
import com.clientService.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/order/")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("makeOrder")
    public String makeOrder(@RequestBody OrderModel order) {
        return orderService.makeOrder(order);
    }

}
