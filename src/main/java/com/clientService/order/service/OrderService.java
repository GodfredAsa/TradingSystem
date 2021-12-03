//package com.clientService.order.service;
//
//import com.clientService.enums.OrderStatus;
//import com.clientService.exceptions.NotFoundException;
//import com.clientService.loggerPack.LoggerConfig;
//import com.clientService.order.model.Order;
//import com.clientService.order.model.OrderRequest;
//import com.clientService.order.model.Product;
//import com.clientService.order.repository.OrderRepository;
//import com.clientService.user.model.AppUser;
//import com.clientService.user.repository.AppUserRepository;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Service
//public class OrderService {
//
//    private ProductService productService;
//    private OrderRepository orderRepository;
//    private AppUserRepository appUserRepository;
//
//    private final RestTemplate restTemplate;
//
//    @Value("${api.key}")
//    private String apiKey;
//
//    @Value("${exchange.url1}")
//    private String exchangeUrl1;
//
//    @Value(("${exchange.url2}"))
//    private String exchangeUrl2;
//
//
//    public OrderService(RestTemplate restTemplate, ProductService productService,
//                        OrderRepository orderRepository, AppUserRepository appUserRepository) {
//        this.restTemplate = restTemplate;
//        this.productService = productService;
//        this.orderRepository = orderRepository;
//        this.appUserRepository = appUserRepository;
//
//    }
//
//
//    public String makeOrder(OrderRequest orderRequest) {
//
//        //Initial validation
//        Product orderProduct = productService.getProductByTicker(orderRequest.getProduct());
//        //        Todo: get the product name using the product id
//
//        //Buy validation
////        Todo: Check the quantity by(X) the price should be less than the total account balance
////        Todo: Check the two exchanges for the best deal, buy all if quantity is >= then buy all else buy the rest from other exchange
////        Todo: Indication for partial purchase
//
//        //Sell validation
////        Todo: Best deal (but orders more than the the specified price first)
////        Todo: Indication for partial purchase
//
//
//        Map<String, Object> orderRequestBody = new HashMap<>() {{
//            put("product", orderRequest.getProduct());
//            put("quantity", orderRequest.getQuantity());
//            put("price", orderRequest.getPrice());
//        }};
//
//        if (orderRequest.getSide().equals("BUY")) {
//            orderRequestBody.put("side", "BUY");
//        } else {
//            orderRequestBody.put("side", "SELL");
//        }
//
//        String response = restTemplate.postForObject(exchangeUrl1 + apiKey + "/order", orderRequestBody, String.class);
//        if (response != null) {
//            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
//                    .getPrincipal();
////            AppUser user = appUserRepository.findById(userDetails.getUsername());
////            Order order = new Order(response, orderRequest.getQuantity(), orderRequest.getPrice(), orderRequest.getSide(),
////                    OrderStatus.PENDING, orderProduct, )
//            LoggerConfig.LOGGER.info("order placed successfully");
//            return response;
//        } else {
//            LoggerConfig.LOGGER.error("There was an issue placing your order");
//            return "There was an issue placing your order, please try again later";
//        }
//    }
//
//    public Order getOrderById(String orderid) {
//
//        Order response = restTemplate.getForObject(exchangeUrl1 + apiKey + "/order/" + orderid, Order.class);
//
//        if (response.getStatus().equals(HttpStatus.NOT_FOUND)) {
////            orderRepository.findById(orderid)
//        }
//
//        if (response == null) {
//            throw new NotFoundException("Order with the provided orderID does not exist");
//        }
//
//        return response;
//    }
//
//}
