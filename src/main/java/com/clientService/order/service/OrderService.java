package com.clientService.order.service;

import com.clientService.exceptions.NotEnoughFundsException;
import com.clientService.exceptions.NotFoundException;
import com.clientService.loggerPack.LoggerConfig;
import com.clientService.order.model.Order;
import com.clientService.order.model.OrderRequest;
import com.clientService.order.model.Product;
import com.clientService.order.repository.OrderRepository;
import com.clientService.user.model.AppUser;
import com.clientService.user.model.MarketProduct;
import com.clientService.user.model.MarketProductList;
import com.clientService.user.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderService {

    private final ProductService productService;
    private final OrderRepository orderRepository;
    private final AppUserRepository appUserRepository;

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

    //Gets logged-in user for the current session
    UserDetails userDetails = (UserDetails) SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getPrincipal();


    public OrderService(RestTemplate restTemplate,
                        ProductService productService,
                        OrderRepository orderRepository,
                        AppUserRepository appUserRepository
    ) {
        this.restTemplate = restTemplate;
        this.productService = productService;
        this.orderRepository = orderRepository;
        this.appUserRepository = appUserRepository;

    }


    public String makeOrder(OrderRequest orderRequest) {

        //provides product if validated successfully by product service,
        //custom exception and error handles catch any error and returns
        //the appropriate Http response
        Product orderProduct = productService.getProductByTicker(orderRequest.getProduct());


        //Todo: change to to implement correct logic once spring security is implemented and handle userNotFoundExceptions
        Optional<AppUser> user = appUserRepository
                .findById(Long
                        .valueOf(userDetails
                                .getUsername()));
        if (user.isEmpty()) {
            throw new NotFoundException("Client not found, the client making this order does not exist in the system");
        }


        if (!canMakePurchase(orderRequest, user.get())) {
            throw new NotEnoughFundsException("User has insufficient funds to process this order");
        }


        Map<String, Object> orderRequestBody = new HashMap<>() {{
            put("product", orderRequest.getProduct());
            put("quantity", orderRequest.getQuantity());
            put("price", orderRequest.getPrice());
            put("side",orderRequest.getSide());
        }};

//        Todo: Check the two exchanges for the best deal, buy all if quantity is >= then buy all else buy the rest from other exchange
        int currentOrderQuantity = orderRequest.getQuantity();
        Map<Integer, String> bestDeal = getBestBidAndQuantity(orderRequest);
        if (bestDeal.keySet().stream().findFirst().get() >= currentOrderQuantity){

            String newOrderId = restTemplate.postForObject(bestDeal.values().stream().findFirst().get(),orderRequestBody,  String.class);
        }
//        Todo: Indication for partial purchase

        //Sell validation
//        Todo: Best deal (but orders more than the the specified price first)
//        Todo: Indication for partial purchase


        String response = restTemplate.postForObject(exchangeUrl1 + apiKey + "/order", orderRequestBody, String.class);
        if (response != null) {

//            Order order = new Order(response, orderRequest.getQuantity(), orderRequest.getPrice(), orderRequest.getSide(),
//                    OrderStatus.PENDING, orderProduct, )
            LoggerConfig.LOGGER.info("order placed successfully");
            return response;
        } else {
            LoggerConfig.LOGGER.error("There was an issue placing your order");
            return "There was an issue placing your order, please try again later";
        }
    }

    public Order getOrderById(String orderId) {

        Order response = restTemplate.getForObject(exchangeUrl1 + apiKey + "/order/" + orderId, Order.class);

        if (response.getStatus().equals(HttpStatus.NOT_FOUND)) {
//            orderRepository.findById(orderId);
        }

        if (response == null) {
            throw new NotFoundException("Order with the provided orderID does not exist");
        }

        return response;
    }

    public boolean canMakePurchase(OrderRequest orderRequest, AppUser user) {
        double totalOrderCost = orderRequest.getPrice() * orderRequest.getQuantity();
        double userCurrentAccountBalance = user
                .getAccount()
                .getBalance();

        return userCurrentAccountBalance > totalOrderCost;
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

}
