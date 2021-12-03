package com.clientService.order.controller;

import com.clientService.order.model.Order;
import com.clientService.order.model.FullOrderBook;
import com.clientService.order.model.OrderRequest;
import com.clientService.order.service.OrderService;
import com.clientService.order.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

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
    public ResponseEntity<ArrayList<String>> makeOrder(@Valid @RequestBody OrderRequest orderRequest) {
        ArrayList<String> orderIds = orderService.makeOrder(orderRequest);

        return new ResponseEntity<ArrayList<String>>(orderIds, HttpStatus.CREATED);
    }

    @GetMapping("getOrder/{orderId}")
    public ResponseEntity<Order> checkOrderStatus(@PathVariable String orderId) {
        return new ResponseEntity<>(this.orderService.checkOrderStatus(orderId), HttpStatus.OK);
    }

    @GetMapping("orderbook")
    public ResponseEntity<ArrayList<FullOrderBook>> getOrderBook() {

        ArrayList<FullOrderBook> fullOrderBook = orderService.getOrderBook();

        return new ResponseEntity<ArrayList<FullOrderBook>>(fullOrderBook, HttpStatus.OK);
    }

    @GetMapping("orderbook/{product}/{option}")
    public ResponseEntity<ArrayList<FullOrderBook>> getOrderBookOf(@PathVariable String product, @PathVariable String option) {

        ArrayList<FullOrderBook> fullOrderBook = orderService.getOrderBookOf(product, option);

        return new ResponseEntity<ArrayList<FullOrderBook>>(fullOrderBook, HttpStatus.OK);
    }


}
