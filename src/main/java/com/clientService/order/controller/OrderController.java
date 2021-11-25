package com.clientService.order.controller;

import com.clientService.order.model.OrderModel;
import com.clientService.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping("makeBuyOrder")
    public String makeBuyOrder(@RequestBody OrderModel order) {
        String orderId = orderService.makeBuyOrder(order);
        return orderId;

    }

    @PostMapping("makeSellOrder")
    public String makeSellOrder(@RequestBody OrderModel order) {
        String orderId = orderService.makeSellOrder(order);
        return orderId;
    }

}
