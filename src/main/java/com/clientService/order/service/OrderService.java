package com.clientService.order.service;

import com.clientService.enums.OrderStatus;
import com.clientService.exceptions.InvalidOrderRequestException;
import com.clientService.exceptions.NotEnoughFundsException;
import com.clientService.exceptions.NotFoundException;
import com.clientService.order.model.*;
import com.clientService.order.repository.OrderRepository;
import com.clientService.orderExecution.model.OrderExecution;
import com.clientService.user.model.AppUser;
import com.clientService.user.model.MarketProduct;
import com.clientService.user.model.MarketProductList;
import com.clientService.user.service.AppUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.*;

@Service
public class OrderService {

    private final ProductService productService;
    private final OrderRepository orderRepository;
    private final AppUserService appUserService;


    private final RestTemplate restTemplate;

    @Value("${api.key}")
    private String apiKey;

    @Value("${exchange.url1}")
    private String exchangeUrl1;

    @Value(("${exchange.url2}"))
    private String exchangeUrl2;

    @Value(("${marketData.url1}"))
    private String marketDataUrl1;

    @Value(("${marketData.url2}"))
    private String marketDataUrl2;

    @Value(("${marketData.both}"))
    private String bothMarketData;

    //Gets logged-in user for the current session
//    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

    public OrderService(RestTemplate restTemplate,
                        ProductService productService,
                        OrderRepository orderRepository,
                        AppUserService appUserService
    ) {
        this.restTemplate = restTemplate;
        this.productService = productService;
        this.orderRepository = orderRepository;
        this.appUserService = appUserService;

    }


    public ArrayList<String> makeOrder(OrderRequest orderRequest, Authentication authentication) {

        //provides product if validated successfully by product service,
        //custom exception and error handles catch any error and returns
        //the appropriate Http response
        Product orderProduct = productService.getProductByTicker(orderRequest.getProduct());


        //Todo: change to to implement correct logic once spring security is implemented and handle userNotFoundExceptions

        //Get the email of the user making the request
        AppUser user = appUserService.getAppUserByEmail(authentication.getName() /*((UserDetails) authentication.getDetails()).getUsername()*/);
        if (authentication.isAuthenticated() || user == null) {
            throw new NotFoundException("Client not found, the client making this order does not exist in the system");
        }

        if (!canMakeOrder(orderRequest, user)) {
            throw new NotEnoughFundsException("User has insufficient funds to process this order");
        }


        Map<String, Object> orderRequestBody = new HashMap<>() {{
            put("product", orderRequest.getProduct());
            put("quantity", orderRequest.getQuantity());
            put("price", orderRequest.getPrice());
            put("side", orderRequest.getSide());
        }};

//        Todo: Check the two exchanges for the best deal, buy all if quantity is >= then buy all else buy the rest from other exchange
        int currentOrderQuantity = orderRequest.getQuantity();
        Map<Integer, String> bestDeal = getBestBidAndQuantity(orderRequest);

        if (bestDeal.keySet().stream().findFirst().get() < currentOrderQuantity) {

//            Todo: Indication for partial purchase
            String newOrderId1 = restTemplate.postForObject(bestDeal.values().stream().findFirst().get(), orderRequestBody, String.class);
            String getOtherUrl = bestDeal.values().stream().findFirst().get().equals(exchangeUrl1) ? exchangeUrl2 : exchangeUrl1;
            String newOrderId2 = restTemplate.postForObject(getOtherUrl, orderRequestBody, String.class);

            OrderModel order1 = new OrderModel(
                    newOrderId1,
                    currentOrderQuantity,
                    orderRequest.getPrice(),
                    orderRequest.getSide(),
                    OrderStatus.PENDING,
                    orderProduct,
                    user,
                    new ArrayList<OrderExecution>(),
                    0);

            OrderModel order2 = new OrderModel(
                    newOrderId2,
                    currentOrderQuantity,
                    orderRequest.getPrice(),
                    orderRequest.getSide(),
                    OrderStatus.PENDING,
                    orderProduct,
                    user,
                    new ArrayList<OrderExecution>(),
                    0);

            orderRepository.save(order1);
            orderRepository.save(order2);

            return new ArrayList<>(List.of(newOrderId1, newOrderId2));
        } else {

            String newOrderId = restTemplate.postForObject(bestDeal.values().stream().findFirst().get(), orderRequestBody, String.class);
            OrderModel order = new OrderModel(
                    newOrderId,
                    currentOrderQuantity,
                    orderRequest.getPrice(),
                    orderRequest.getSide(),
                    OrderStatus.PENDING,
                    orderProduct,
                    user,
                    new ArrayList<OrderExecution>(),
                    0);

            orderRepository.save(order);
            return new ArrayList<>(List.of(newOrderId));

        }

    }

    public OrderModel checkOrderStatus(String orderId) {

        OrderModel response = restTemplate.getForObject(exchangeUrl1 + apiKey + "/order/" + orderId, OrderModel.class);

//        find out whether the order has been completed by the
//        response status code (was suggested by PM) if so, then
//        return a local instance of the completed order
        if (response.getStatus().equals(HttpStatus.NOT_FOUND) && orderRepository.findById(orderId).isPresent()) {

            return orderRepository.findById(orderId).get();

        }
        //check whether it was an actual invalid request
        else if (response.getStatus().equals(HttpStatus.NOT_FOUND) && orderRepository.findById(orderId).isEmpty()) {

            throw new InvalidOrderRequestException("Order with the provided order Id does not exist");
        }

        return response;
    }

    public boolean canMakeOrder(OrderRequest orderRequest, AppUser user) {
        double totalOrderCost = orderRequest.getPrice() * orderRequest.getQuantity();
        double userCurrentAccountBalance = user
                .getAccount()
                .getBalance();

        if (orderRequest.getSide().equals("BUY")) {

            return userCurrentAccountBalance >= totalOrderCost;
        } else {
//          Todo:Create method in the repository that queries the db for the total quantity of all buy orders of that particular product make by the current order issuer
            List<OrderModel> usersActiveBuyOrdersOfProd = orderRepository.getAllUsersBuyOrdersOfAProduct(orderRequest.getProduct(), user.getId(), "BUY");

            if (usersActiveBuyOrdersOfProd.isEmpty()) {
                throw new InvalidOrderRequestException("User does not own the specified product");
            }

            int availableQuntOfProd = usersActiveBuyOrdersOfProd
                    .stream()
                    .map(ord -> ord.getQuantity() - ord.getCumulativeQuantity())
                    .mapToInt((cumQuant -> Integer.valueOf(cumQuant)))
                    .sum();

            return orderRequest.getQuantity() >= availableQuntOfProd;
        }

    }

    //Returns the exchange and quantity to buy from first before the other
    public Map<Integer, String> getBestBidAndQuantity(OrderRequest orderRequest) {

        //      Todo: Abstract this logic into it's own method in a service
        MarketProductList marketProductList1 = restTemplate
                .getForObject(marketDataUrl1, MarketProductList.class);
        List<MarketProduct> exchange1Products = marketProductList1
                .getMarketProducts();

        MarketProductList marketProductList2 = restTemplate
                .getForObject(marketDataUrl2, MarketProductList.class);
        List<MarketProduct> exchange2Products = marketProductList2
                .getMarketProducts();


        Optional<MarketProduct> bestOfExc1;
        Optional<MarketProduct> bestOfExc2;

        //Setting Best trades per order side
        if (orderRequest.getSide() == "BUY") {

            bestOfExc1 = exchange1Products
                    .stream()
                    .filter(prod -> prod.getTicker().equals(orderRequest.getProduct()))
                    .reduce((a, b) -> a.getAskPrice() < b.getAskPrice() ? a : b);

            bestOfExc2 = exchange2Products
                    .stream()
                    .filter(prod -> prod.getTicker().equals(orderRequest.getProduct()))
                    .reduce((a, b) -> a.getAskPrice() < b.getAskPrice() ? a : b);
        } else {

            bestOfExc1 = exchange1Products
                    .stream()
                    .filter(prod -> prod.getTicker().equals(orderRequest.getProduct()))
                    .reduce((a, b) -> a.getAskPrice() > b.getAskPrice() ? a : b);

            bestOfExc2 = exchange2Products
                    .stream()
                    .filter(prod -> prod.getTicker().equals(orderRequest.getProduct()))
                    .reduce((a, b) -> a.getAskPrice() > b.getAskPrice() ? a : b);

        }

        //check whether the product is available on both exchanges
        if (bestOfExc1.isPresent() && bestOfExc2.isPresent()) {

            int firstOrSecond = Double.compare(bestOfExc1.get().getAskPrice(), bestOfExc2.get().getAskPrice());

            //If exchange1 selling price is lower
            if (firstOrSecond < 0) {

                return new HashMap<Integer, String>() {{
                    put(bestOfExc1.get().getBuyLimit(), exchangeUrl1);
                }};

            }
            //If exchange2 selling price is lower
            else {

                return new HashMap<Integer, String>() {{
                    put(bestOfExc2.get().getBuyLimit(), exchangeUrl2);
                }};
            }
        }
        //If product is only on exchange1
        else if (bestOfExc1.isPresent()) {

            return new HashMap<Integer, String>() {{
                put(bestOfExc1.get().getBuyLimit(), exchangeUrl1);
            }};

        }
        //If product is only on exchange2
        else if (bestOfExc2.isPresent()) {

            return new HashMap<Integer, String>() {{
                put(bestOfExc2.get().getBuyLimit(), exchangeUrl2);
            }};

        }
        //If product is not yet on any exchange
        else {

            //just split then in two equal halves
            return new HashMap<Integer, String>() {{
                put((orderRequest.getQuantity() / 2), exchangeUrl1);
            }};
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
