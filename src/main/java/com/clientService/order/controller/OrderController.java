package com.clientService.order.controller;

import com.clientService.order.model.FullOrderBook;
import com.clientService.order.model.OrderModel;
import com.clientService.order.model.OrderRequest;
import com.clientService.order.service.OrderService;
import com.clientService.order.service.OrderServiceHelper;
import com.clientService.order.service.OrderPlacingService;
import com.clientService.order.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@RequestMapping(path = "/api/order/")
public class OrderController {

    private final OrderService orderService;
    private final ProductService productService;
    private final OrderServiceHelper orderServiceHelper;
    private final OrderPlacingService orderPlacingService;

    public OrderController(OrderService orderService, ProductService productService, OrderServiceHelper orderServiceHelper, OrderPlacingService orderPlacingService) {
        this.orderService = orderService;
        this.productService = productService;
        this.orderServiceHelper = orderServiceHelper;
        this.orderPlacingService = orderPlacingService;
    }

    @PostMapping("makeOrder")
    public ResponseEntity<ArrayList<String>> makeOrder(@Valid @RequestBody OrderRequest orderRequest, @AuthenticationPrincipal UserDetails appPrincipal) {
        ArrayList<String> orderIds = orderPlacingService.makeOrder(orderRequest, appPrincipal);

        return new ResponseEntity<ArrayList<String>>(orderIds, HttpStatus.CREATED);
    }

    @GetMapping("getOrder/{orderId}")
    public ResponseEntity<OrderModel> checkOrderStatus(@PathVariable String orderId) {
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
