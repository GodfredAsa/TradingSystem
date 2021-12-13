package com.clientService.order.service;

import com.clientService.exceptions.InvalidOrderRequestException;
import com.clientService.order.model.FullOrderBook;
import com.clientService.order.model.OrderBook;
import com.clientService.order.model.OrderModel;
import com.clientService.order.model.OrderResponse;
import com.clientService.order.repository.OrderRepository;
import com.clientService.user.model.Account;
import com.clientService.user.model.AppUser;
import com.clientService.user.repository.AppUserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${api.key}")
    private String apiKey;

    @Value("${exchange.url1}")
    private String exchangeUrl1;

    @Value(("${exchange.url2}"))
    private String exchangeUrl2;


    /**
     * @param orderId The id of the order to be retrieved
     * @return The order with the provided id, if it exists
     */
    public OrderModel checkOrderStatus(String orderId) {

        OrderModel responseFromExchange1;
        OrderModel responseFromExchange2;


        try {
            responseFromExchange1 = restTemplate.getForObject(exchangeUrl1 + apiKey + "/order/" + orderId, OrderModel.class);

        } catch (HttpServerErrorException e1) {
            responseFromExchange1 = null;
        }

        try {
            responseFromExchange2 = restTemplate.getForObject(exchangeUrl2 + apiKey + "/order/" + orderId, OrderModel.class);

        } catch (HttpServerErrorException e2) {
            responseFromExchange2 = null;
        }

//        find out whether the order has been completed by the
//        response status code (was suggested by PM) if so, then
//        return a local db instance of the completed order
        if (responseFromExchange1 == null && responseFromExchange2 == null && orderRepository.findById(orderId).isPresent()) {

//            Todo: We know the order is completed, we can do some custom logic here
            return orderRepository.findById(orderId).get();

        }
        //Entire method can be refactored to use only this check

        //check whether it was an actual invalid request
        else if (responseFromExchange1 == null && responseFromExchange2 == null && orderRepository.findById(orderId).isEmpty()) {

            throw new InvalidOrderRequestException("Order with the provided order Id does not exist");
        } else {
            return orderRepository.findById(orderId).get();
        }

    }

    /**
     * @return A list containing a single order id or two order ids in the case of a split order
     */
    public ArrayList<FullOrderBook> getOrderBook(int exchange) {

        OrderBook orderBook = restTemplate.getForObject(exchange == 1 ? exchangeUrl1 : exchangeUrl2 + "/orderbook", OrderBook.class);
        ArrayList<FullOrderBook> fullFullOrderBooks = orderBook.getFullOrderBooks();
        return fullFullOrderBooks;
    }

    /**
     * @param product  The ticker for a product
     * @param filterby Which metric to filter by
     * @return
     * @Param exchange The exchange to make this query against
     */
    public ArrayList<FullOrderBook> getOrderBookOf(int exchange, String product, String filterby) {

        OrderBook orderBook = restTemplate.getForObject(exchange == 1 ? exchangeUrl1 : exchangeUrl2 + "/orderbook/" + product + "/" + filterby, OrderBook.class);
        ArrayList<FullOrderBook> fullFullOrderBooks = orderBook.getFullOrderBooks();
        return fullFullOrderBooks;
    }

}
