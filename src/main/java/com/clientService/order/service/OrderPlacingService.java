package com.clientService.order.service;

import com.clientService.enums.OrderStatus;
import com.clientService.enums.PortfolioStatus;
import com.clientService.exceptions.InvalidOrderRequestException;
import com.clientService.exceptions.NotEnoughFundsException;
import com.clientService.exceptions.NotFoundException;
import com.clientService.order.model.OrderModel;
import com.clientService.order.model.OrderRequest;
import com.clientService.order.model.Product;
import com.clientService.order.repository.OrderRepository;
import com.clientService.orderExecution.model.OrderExecution;
import com.clientService.user.model.AppUser;
import com.clientService.user.model.Portfolio;
import com.clientService.user.repository.PortfolioRepository;
import com.clientService.user.service.AppUserService;
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
public class OrderPlacingService {

    private final AppUserService appUserService;
    private final ProductService productService;
    private final RestTemplate restTemplate;
    private final OrderRepository orderRepository;
    private final PortfolioRepository portfolioRepository;

    @Value("${api.key}")
    private String apiKey;

    @Value("${exchange.url1}")
    private String exchangeUrl1;

    @Value(("${exchange.url2}"))
    private String exchangeUrl2;

    public OrderPlacingService(AppUserService appUserService,
                               ProductService productService,
                               RestTemplate restTemplate,
                               OrderRepository orderRepository,
                               PortfolioRepository portfolioRepository
    ) {
        this.appUserService = appUserService;
        this.productService = productService;
        this.restTemplate = restTemplate;
        this.orderRepository = orderRepository;
        this.portfolioRepository = portfolioRepository;
    }

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
        Map<Long, String> bestDeal = OrderServiceHelper.getBestBidAndQuantity(orderRequest);

//        Place all orders on the exchange with the best deal if they
//        hold an enough quantity of the product the user
//        is making a buy or sell order for, otherwise place
//        the remaining order on the other exchange
        long bestDealQuantity = bestDeal.keySet().stream().findFirst().get();
        if (bestDealQuantity < currentOrderQuantity) {
            int remainder = (int) (orderRequest.getQuantity() - bestDealQuantity);
            orderRequest.setQuantity((int) bestDealQuantity);

//            Todo: Indication for partial purchase and split exchange purchases
            String newOrderId1 = restTemplate.postForObject(bestDeal.values().stream().findFirst().get() + apiKey + "/order", orderRequestBody, String.class);
            String otherUrl = bestDeal.values().stream().findFirst().get().equals(exchangeUrl1) ? exchangeUrl2 : exchangeUrl1;

            orderRequest.setQuantity(remainder);
            String newOrderId2 = restTemplate.postForObject(otherUrl + apiKey + "/order", orderRequestBody, String.class);

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
                    orderProduct,
                    orderPortfolio,
                    user,
                    new ArrayList<OrderExecution>(),
                    0);

            orderRepository.save(order);
            return new ArrayList<>(List.of(newOrderId));

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
//            List<OrderModel> usersActiveBuyOrdersOfProd = orderRepository.getAllUsersBuyOrdersOfAProduct(orderRequest.getProduct(), user.getId(), "BUY");
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
