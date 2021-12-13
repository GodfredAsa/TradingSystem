package com.clientService.order.controller;

import com.clientService.order.model.FullOrderBook;
import com.clientService.order.model.OrderModel;
import com.clientService.order.model.OrderRequest;
import com.clientService.order.service.*;
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
    private final CancelOrderService cancelOrderService;

    public OrderController(OrderService orderService,
                           ProductService productService,
                           OrderServiceHelper orderServiceHelper,
                           OrderPlacingService orderPlacingService,
                           CancelOrderService cancelOrderService) {
        this.orderService = orderService;
        this.productService = productService;
        this.orderServiceHelper = orderServiceHelper;
        this.orderPlacingService = orderPlacingService;
        this.cancelOrderService = cancelOrderService;
    }

    /**
     * @param orderRequest Json containing details of the clients order
     * @param appPrincipal Auth details of the client placing the order
     * @return List containing one order id or two order ids in the case of a split order
     */
    @PostMapping("makeorder")
    public ResponseEntity<ArrayList<String>> makeOrder(@Valid @RequestBody OrderRequest orderRequest, @AuthenticationPrincipal UserDetails appPrincipal) {
        ArrayList<String> orderIds = orderPlacingService.makeOrder(orderRequest, appPrincipal);

        return new ResponseEntity<ArrayList<String>>(orderIds, HttpStatus.CREATED);
    }

    /**
     * @param orderId ID of the order which is to be tracked
     * @return Json containing current details of the order belonging to the provided id
     */
    @GetMapping("getstatus/{orderid}")
    public ResponseEntity<OrderModel> checkOrderStatus(@PathVariable String orderId) {
        return new ResponseEntity<>(this.orderService.checkOrderStatus(orderId), HttpStatus.OK);
    }

    /**
     * @return Order book for the provided exchange 1 or 2
     */
    @GetMapping("orderbook/{exchange}")
    public ResponseEntity<ArrayList<FullOrderBook>> getOrderBook(@PathVariable int exchange) {

        ArrayList<FullOrderBook> fullOrderBook = orderService.getOrderBook(exchange);

        return new ResponseEntity<ArrayList<FullOrderBook>>(fullOrderBook, HttpStatus.OK);
    }

    /**
     * @param product  Product ticker
     * @param filterby Metric to filter by
     * @return List of orders and their executions filtered by provided filter
     */
    @GetMapping("orderbook/{exchange}/{product}/{filterby}")
    public ResponseEntity<ArrayList<FullOrderBook>> getOrderBookOf(@PathVariable int exchange, @PathVariable String product, @PathVariable String filterby) {

        ArrayList<FullOrderBook> fullOrderBook = orderService.getOrderBookOf(exchange, product, filterby);

        return new ResponseEntity<ArrayList<FullOrderBook>>(fullOrderBook, HttpStatus.OK);
    }

    /**
     *
     * @param portfolioId portfolio id for which portfolio the order belongs to
     * @param orderId The id for the order requested to be cancelled
     * @param appPrincipal The current user of the active session
     * @return A boolean indicating whether the order could or could not be cancelled
     */
    @DeleteMapping("cancel/{portfolioId}/{orderId}")
    public ResponseEntity<Boolean> cancelOrder(@PathVariable Long portfolioId, @PathVariable String orderId, @AuthenticationPrincipal UserDetails appPrincipal) {

        boolean isOrderCancelled = cancelOrderService.cancelOrder(portfolioId, orderId, appPrincipal);

        return new ResponseEntity<Boolean>(isOrderCancelled, HttpStatus.OK);

    }

}
