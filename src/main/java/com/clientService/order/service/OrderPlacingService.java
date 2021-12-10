package com.clientService.order.service;

import com.clientService.enums.OrderStatus;
import com.clientService.enums.PortfolioStatus;
import com.clientService.exceptions.InvalidOrderRequestException;
import com.clientService.exceptions.NotEnoughFundsException;
import com.clientService.exceptions.NotFoundException;
import com.clientService.loggerPack.LoggerConfig;
import com.clientService.order.model.OrderModel;
import com.clientService.order.model.OrderRequest;
import com.clientService.order.model.Product;
import com.clientService.order.repository.OrderRepository;
import com.clientService.orderExecution.model.OrderExecution;
import com.clientService.user.model.AppUser;
import com.clientService.user.model.Portfolio;
import com.clientService.user.repository.PortfolioRepository;
import com.clientService.user.service.AppUserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class OrderPlacingService {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private ProductService productService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private OrderServiceHelper orderServiceHelper;

    @Value("${api.key}")
    private String apiKey;

    @Value("${exchange.url1}")
    private String exchangeUrl1;

    @Value(("${exchange.url2}"))
    private String exchangeUrl2;


    public ArrayList<String> makeOrder(OrderRequest orderRequest, UserDetails appPrincipal) {

        //provides product if validated successfully by product service,
        //custom exception and error handlers catch any error and returns
        //the appropriate Http response and status
        Product orderProduct = productService.getProductByTicker(orderRequest.getProduct());
        Portfolio orderPortfolio = portfolioRepository.findById(orderRequest.getPortfolioId()).get();


        //Get the email of the user making the request
        AppUser user = appUserService.getAppUserByEmail(appPrincipal.getUsername() /*((UserDetails) authentication.getDetails()).getUsername()*/);
        if (user == null || !appPrincipal.isAccountNonExpired()) {
            throw new NotFoundException("Client not found, the client making this order does not exist in the system");
        }

        //TODO Return response to client instead of throwing an exception

        if (!canMakeOrder(orderRequest, user)) {
            throw new NotEnoughFundsException("User has insufficient funds to process this order");
        }


        Map<String, Object> orderRequestBody = new HashMap<>() {{
            put("product", orderRequest.getProduct());
            put("quantity", orderRequest.getQuantity());
            put("price", orderRequest.getPrice());
            put("side", orderRequest.getSide());
        }};

//        Get the exchange with the best deal
        int currentOrderQuantity = orderRequest.getQuantity();
        Map<Integer, String> bestDeal = orderServiceHelper.getBestBidAndQuantity(orderRequest);

//        Place all orders on the exchange with the best deal if they
//        hold an enough quantity of the product the user
//        is making a buy or sell order for, otherwise place
//        the remaining order on the other exchange
        long bestDealQuantity = bestDeal.keySet().stream().findFirst().get();
        if (bestDealQuantity < currentOrderQuantity) {
            int remainder = (int) (orderRequest.getQuantity() - bestDealQuantity);
            orderRequest.setQuantity((int) bestDealQuantity);

//            Todo: Indication for partial purchase and split exchange purchases
            String orderId1 = restTemplate.postForObject(bestDeal.values().stream().findFirst().get() + apiKey + "/order", orderRequestBody, String.class);
            String otherUrl = bestDeal.values().stream().findFirst().get().equals(exchangeUrl1) ? exchangeUrl2 : exchangeUrl1;

            orderRequest.setQuantity(remainder);
            String orderId2 = restTemplate.postForObject(otherUrl + apiKey + "/order", orderRequestBody, String.class);

            String newOrderId1 = orderId1.substring(1, orderId1.length() - 1);
            String newOrderId2 = orderId2.substring(1, orderId2.length() - 1);
            OrderModel order1 = new OrderModel(
                    newOrderId1,
                    orderRequest.getQuantity(),
                    orderRequest.getPrice(),
                    orderRequest.getSide(),
                    orderProduct,
                    orderPortfolio,
                    user,
                    new ArrayList<OrderExecution>(),
                    0
            );


            OrderModel order2 = new OrderModel(
                    newOrderId2,
                    currentOrderQuantity,
                    orderRequest.getPrice(),
                    orderRequest.getSide(),
                    orderProduct,
                    orderPortfolio,
                    user,
                    new ArrayList<OrderExecution>(),
                    0);
            LoggerConfig.LOGGER.info("---------------------------------- order id1 "
                    + order1.getId()
                    + "order id2 "
                    + order2.getId());

            orderRepository.save(order1);
            orderRepository.save(order2);

            return new ArrayList<String>(List.of(order1.getId(), order2.getId()));
        } else {

            String orderId = restTemplate.postForObject(bestDeal.values().stream().findFirst().get() + apiKey + "/order", orderRequestBody, String.class);
            String newOrderId = orderId.substring(1, orderId.length() - 1);
            OrderModel order = new OrderModel(
                    newOrderId,
                    currentOrderQuantity,
                    orderRequest.getPrice(),
                    orderRequest.getSide(),
                    orderProduct,
                    orderPortfolio,
                    user,
                    new ArrayList<OrderExecution>(),
                    0);

            LoggerConfig.LOGGER.info("---------------------------------- order id "
                    + order.getId());

            orderRepository.save(order);
            return new ArrayList<String>(List.of(order.getId()));

        }

    }

    public boolean canMakeOrder(OrderRequest orderRequest, AppUser user) {

        if (orderRequest.getSide().equals("BUY")) {

            double totalOrderCost = orderRequest.getPrice() * orderRequest.getQuantity();
            double userCurrentAccountBalance = user
                    .getAccount()
                    .getBalance();

            return userCurrentAccountBalance >= totalOrderCost;
        } else {

            // Get the available quantity of products from each exchange by checking their order books
            // List<OrderModel> usersActiveBuyOrdersOfProd = orderRepository.getAllUsersBuyOrdersOfAProduct(orderRequest.getProduct(), user.getId(), "BUY");
            List<OrderModel> usersActiveBuyOrdersOfProd = user
                    .getPortfolios()
                    .stream()
                    .filter(portfolio -> portfolio.getStatus().equals(PortfolioStatus.OPENED) && portfolio.getId().equals(orderRequest.getPortfolioId()))
                    .findFirst()
                    .get()
                    .getOrders()
                    .stream()
                    .filter(order -> order.getSide().equals("BUY"))
                    .collect(Collectors.toList());

            if (usersActiveBuyOrdersOfProd.isEmpty()) {
                throw new InvalidOrderRequestException("User does not own the specified product");
            }

            int availableQuantOfProd = usersActiveBuyOrdersOfProd
                    .stream()
                    .map(ord -> ord.getQuantity() - ord.getCumulativeQuantity())
                    .mapToInt((cumQuant -> Integer.valueOf(cumQuant)))
                    .sum();

            return orderRequest.getQuantity() >= availableQuantOfProd;
        }

    }
}
