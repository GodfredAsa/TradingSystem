package com.clientService.order.service;

import com.clientService.exceptions.InvalidOrderRequestException;
import com.clientService.order.model.*;
import com.clientService.order.repository.OrderRepository;
import com.clientService.user.repository.MarketDataRepository;
import com.clientService.user.service.AppUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class OrderService {

    private final ProductService productService;
    private final OrderRepository orderRepository;
    private final AppUserService appUserService;
    private final MarketDataRepository marketDataRepository;


    private final RestTemplate restTemplate;

    @Value("${api.key}")
    private String apiKey;

    @Value("${exchange.url1}")
    private String exchangeUrl1;

    @Value(("${exchange.url2}"))
    private String exchangeUrl2;

    @Value(("${marketData.both}"))
    private String bothMarketData;

    //Gets logged-in user for the current session
//    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

    public OrderService(RestTemplate restTemplate,
                        ProductService productService,
                        OrderRepository orderRepository,
                        AppUserService appUserService,
                        MarketDataRepository marketDataRepository
    ) {
        this.restTemplate = restTemplate;
        this.productService = productService;
        this.orderRepository = orderRepository;
        this.appUserService = appUserService;
        this.marketDataRepository = marketDataRepository;

    }


    public OrderModel checkOrderStatus(String orderId) {

        OrderModel responseFromExchange1 = restTemplate.getForObject(exchangeUrl1 + apiKey + "/order/" + orderId, OrderModel.class);
        OrderModel responseFromExchange2 = restTemplate.getForObject(exchangeUrl2 + apiKey + "/order/" + orderId, OrderModel.class);
//        OrderModel response = marketDataRepository.findById(orderId);

//        find out whether the order has been completed by the
//        response status code (was suggested by PM) if so, then
//        return a local instance of the completed order
//        if (responseFromExchange1.getStatus().equals(HttpStatus.NOT_FOUND) && responseFromExchange1.getStatus().equals(HttpStatus.NOT_FOUND) && orderRepository.findById(orderId).isPresent()) {

        if (responseFromExchange1.getStatus().equals(HttpStatus.NOT_FOUND) && responseFromExchange2.getStatus().equals(HttpStatus.NOT_FOUND) && orderRepository.findById(orderId).isPresent()) {

//            Todo: We know the order is completed, we can do some custom logic here
            return orderRepository.findById(orderId).get();

        }
        //Entire method can be refactored to use only this check

        //check whether it was an actual invalid request
        else if (responseFromExchange1.getStatus().equals(HttpStatus.NOT_FOUND) && responseFromExchange2.getStatus().equals(HttpStatus.NOT_FOUND) && orderRepository.findById(orderId).isEmpty()) {

            throw new InvalidOrderRequestException("Order with the provided order Id does not exist");
        } else {
            return orderRepository.findById(orderId).get();
        }

    }

//    public String saveOrder(
//            String newOrderId,
//            int currentOrderQuantity,
//            double price,
//            String side,
//            OrderStatus orderStatus,
//            String orderProduct,
//            AppUser user,
//            List<OrderExecution> list
//    ) {
//
//
//            Order order = new Order(
//                    newOrderId,
//                    currentOrderQuantity,
//                    orderRequest.getPrice(),
//                    orderRequest.getSide(),
//                    OrderStatus.PENDING,
//                    orderProduct,
//                    user.get(),
//                    new ArrayList<OrderExecution>());
//            LoggerConfig.LOGGER.info("order placed successfully");
//            return response;
//
////         else {
////            LoggerConfig.LOGGER.error("There was an issue placing your order");
////            return "There was an issue placing your order, please try again later";
//
//
//        return "";
//
//    }

    public ArrayList<FullOrderBook> getOrderBook() {

        OrderBook orderBook = restTemplate.getForObject(bothMarketData + "/orderbook", OrderBook.class);
        ArrayList<FullOrderBook> fullFullOrderBooks = orderBook.getFullOrderBooks();
        return fullFullOrderBooks;
    }

    public ArrayList<FullOrderBook> getOrderBookOf(String product, String option) {

        OrderBook orderBook = restTemplate.getForObject(bothMarketData + "/orderbook/" + product + "/" + option, OrderBook.class);
        ArrayList<FullOrderBook> fullFullOrderBooks = orderBook.getFullOrderBooks();
        return fullFullOrderBooks;
    }

}
