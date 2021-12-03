//package com.clientService.order.controller;
//import com.clientService.order.model.Order;
//import com.clientService.order.model.OrderRequest;
//import com.clientService.order.service.OrderService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//
//@RestController
//@RequestMapping(path = "/api/order/")
//public class OrderController {
//
//    private final OrderService orderService;
//    private final ProductService productService;
//
//    @Autowired
//    public OrderController(OrderService orderService, ProductService productService) {
//        this.orderService = orderService;
//        this.productService = productService;
//    }
//
//    @PostMapping("makeOrder")
//    public ResponseEntity<String> makeOrder(@RequestBody OrderRequest orderRequest) {
//        String orderId = orderService.makeOrder(orderRequest);
//
//        return new ResponseEntity<String>(orderId, HttpStatus.CREATED);
//    }
//
//    @GetMapping("getOrder/{orderId}")
//    public Order getOrder(@PathVariable String orderId) {
//        return this.orderService.getOrderById(orderId);
//    }
//
//}
