package com.clientService.order.service;

import com.clientService.order.model.OrderModel;
import com.clientService.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void makeBuyOrder(OrderModel order) {

//        Todo: validate if user has enough fund to make the purchase

    }

    public void makeSellOrder(OrderModel order) {
//        Todo: validate if user owns the stocks they intend to sell


    }
}
