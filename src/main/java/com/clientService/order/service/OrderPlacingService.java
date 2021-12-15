package com.clientService.order.service;

import com.clientService.exceptions.InvalidOrderRequestException;
import com.clientService.exceptions.NotEnoughFundsException;
import com.clientService.exceptions.NotFoundException;
import com.clientService.loggerPack.LoggerConfig;
import com.clientService.order.model.*;
import com.clientService.order.repository.OrderRepository;
import com.clientService.orderExecution.model.OrderExecution;
import com.clientService.user.model.Account;
import com.clientService.user.model.AppUser;
import com.clientService.user.model.Portfolio;
import com.clientService.user.model.PortfolioProductData;
import com.clientService.user.repository.PortfolioProductDataRepository;
import com.clientService.user.repository.PortfolioRepository;
import com.clientService.user.service.AppUserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class OrderPlacingService {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PortfolioProductDataRepository portfolioProductDataRepository;

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

    @Value(("${report.url}"))
    private String reportUrl;


    /**
     * @param orderRequest Details of the order to be placed
     * @param appPrincipal Auth details of the client placing the order
     * @return List of one order id or two in the case of a split order
     */
    public ArrayList<String> makeOrder(OrderRequest orderRequest, UserDetails appPrincipal) {

        //provides product if validated successfully by product service,
        //custom exception and error handlers catch any error and returns
        //the appropriate Http response and status
        Product orderProduct = productService.getProductByTicker(orderRequest.getProduct());
        Portfolio orderPortfolio = portfolioRepository.findById(orderRequest.getPortfolioId()).get();


        //Get the email of the user making the request
        AppUser user = appUserService.getAppUserByEmail(appPrincipal.getUsername());
        if (user == null || !appPrincipal.isAccountNonExpired()) {
            throw new NotFoundException("Client not found, the client making this order does not exist in the system");
        }

        //TODO Return response to client instead of throwing an exception

        if (!canMakeOrder(orderRequest, user, orderPortfolio)) {
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
        Account userAccount = user.getAccount();

        Map<MarketDataProduct, String> bestDeal = orderServiceHelper.getBestBidAndQuantity(orderRequest);


//        Place all orders on the exchange with the best deal if they
//        hold an enough quantity of the product the user
//        is making a buy or sell order for, otherwise place
//        the remaining order on the other exchange
        int bestDealQuantity = orderRequest
                .getSide()
                .equals("BUY") ? bestDeal
                .keySet()
                .stream()
                .findFirst()
                .get()
                .getBuyLimit() : bestDeal
                .keySet()
                .stream()
                .findFirst()
                .get()
                .getSellLimit();

        double bestDealPrice = orderRequest.getSide().equals("BUY") ? bestDeal.keySet().stream().findFirst().get().getAskPrice() : bestDeal.keySet().stream().findFirst().get().getBidPrice();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        if (bestDealQuantity < currentOrderQuantity) {
            int remainingQuantity = (int) (orderRequest.getQuantity() - bestDealQuantity);
            orderRequest.setQuantity((int) bestDealQuantity);

//            Todo: Indication for partial purchase and split exchange purchases
            String orderId1 = restTemplate.postForObject(bestDeal.values().stream().findFirst().get() + apiKey + "/order", orderRequestBody, String.class);
            LocalDateTime nowE1 = LocalDateTime.now();
            adjustPortfolio(orderRequest, userAccount, orderPortfolio, orderProduct);

            String otherUrl = bestDeal.values().stream().findFirst().get().equals(exchangeUrl1) ? exchangeUrl2 : exchangeUrl1;

            orderRequest.setQuantity(remainingQuantity);
            String orderId2 = restTemplate.postForObject(otherUrl + apiKey + "/order", orderRequestBody, String.class);
            LocalDateTime nowE2 = LocalDateTime.now();
            adjustPortfolio(orderRequest, userAccount, orderPortfolio, orderProduct);


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

            LoggerConfig.LOGGER.info("=================================== Split order ===================================\n");
            LoggerConfig.LOGGER.info("=================================== First order with id: " + order1.getId() + " placed ===================================\n");
            LoggerConfig.LOGGER.info("=================================== Second order with id: " + order1.getId() + " placed ===================================\n");

            orderRepository.save(order1);
            orderRepository.save(order2);


            OrderReportModel orderReportE1 = new OrderReportModel(order1.getId(),
                    user.getId(),
                    order1.getId(),
                    order1.getQuantity(),
                    orderRequest.getPrice(),
                    orderRequest.getSide(),
                    nowE1,
                    order1.getStatus().toString(),
                    0,
                    true,
                    bestDealPrice,
                    bestDealQuantity);

            reportOrder(orderReportE1);

            OrderReportModel orderReportE2 = new OrderReportModel(order2.getId(),
                    user.getId(),
                    order2.getId(),
                    order2.getQuantity(),
                    orderRequest.getPrice(),
                    orderRequest.getSide(),
                    nowE2,
                    order2.getStatus().toString(),
                    0,
                    true,
                    bestDealPrice,
                    bestDealQuantity);

            reportOrder(orderReportE2);

            return new ArrayList<String>(List.of(order1.getId(), order2.getId()));

        } else {

            String orderId = restTemplate.postForObject(bestDeal.values().stream().findFirst().get() + apiKey + "/order", orderRequestBody, String.class);
            LocalDateTime now = LocalDateTime.now();
            adjustPortfolio(orderRequest, userAccount, orderPortfolio, orderProduct);

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

            LoggerConfig.LOGGER.info("=================================== Order with id: " + order.getId() + " placed ===================================\n");

            orderRepository.save(order);

            OrderReportModel orderReport = new OrderReportModel(order.getId(),
                    user.getId(),
                    order.getId(),
                    order.getQuantity(),
                    orderRequest.getPrice(),
                    orderRequest.getSide(),
                    now,
                    order.getStatus().toString(),
                    0,
                    true,
                    bestDealPrice,
                    bestDealQuantity);

            reportOrder(orderReport);

            return new ArrayList<String>(List.of(order.getId()));

        }

    }

    /**
     * @param orderReport
     */
    private void reportOrder(OrderReportModel orderReport) {


        try {
            boolean success = Boolean.TRUE.equals(restTemplate.postForObject(reportUrl + "orders", orderReport, Object.class));

            if (success) {

                LoggerConfig.LOGGER.info("=================================== Order Logged Successfully ===================================\n");
            } else {

                LoggerConfig.LOGGER.info("=================================== Order Logging Unsuccessful ===================================\n");
            }
        } catch (Exception e) {

            LoggerConfig.LOGGER.info("=================================== Error reporting order: " + e.getMessage() + " ===================================\n");
            return;
        }

    }

    /**
     * @param orderRequest User's order request details
     * @param account      User's current Account details
     * @param portfolio    Portfolio from which the order is being made
     * @param orderProduct The product being purchased
     */
    private void adjustPortfolio(OrderRequest orderRequest, Account account, Portfolio portfolio, Product orderProduct) {

        PortfolioProductData portfolioProductData;

        if (orderRequest.getSide().equals("BUY")) {
            account.setBalance(account.getBalance() - (orderRequest.getPrice() * orderRequest.getQuantity()));


            if (portfolio.getPortfolioProductData().contains(orderProduct)) {

                portfolioProductData = getPortfolioProduct(portfolio, orderRequest);

                portfolioProductData.setQuantity(portfolioProductData.getQuantity() - orderRequest.getQuantity());

            } else {

                PortfolioProductData newPpd = new PortfolioProductData(orderProduct, portfolio, orderRequest.getQuantity());
                portfolio.getPortfolioProductData().add(newPpd);
                portfolioProductDataRepository.save(newPpd);

            }
        } else {

            portfolioProductData = getPortfolioProduct(portfolio, orderRequest);

            portfolioProductData.setQuantity(portfolioProductData.getQuantity() - orderRequest.getQuantity());

        }

    }

    /**
     * @param portfolio
     * @param orderRequest
     * @return
     */
    private PortfolioProductData getPortfolioProduct(Portfolio portfolio, OrderRequest orderRequest) {

        return portfolio
                .getPortfolioProductData()
                .stream()
                .filter(ppd -> ppd
                        .getProduct()
                        .getTicker()
                        .equals(orderRequest.getProduct()) &&
                        ppd
                                .getPortfolio()
                                .getId()
                                .equals(orderRequest.getPortfolioId()))
                .findFirst()
                .get();
    }

    /**
     * @param orderRequest Details of clients order
     * @param user         Details of client placing the order
     * @return A boolean indicating whether the client is eligible to place the trade
     */
    public boolean canMakeOrder(OrderRequest orderRequest, AppUser user, Portfolio orderPortfolio) {

        if (orderRequest.getSide().equals("BUY")) {

            double totalOrderCost = orderRequest.getPrice() * orderRequest.getQuantity();
            double userCurrentAccountBalance = user
                    .getAccount()
                    .getBalance();

            return userCurrentAccountBalance >= totalOrderCost;

        } else {

//              Get the client's available quantity of products
            Optional<PortfolioProductData> currentPortfolioProduct = orderPortfolio
                    .getPortfolioProductData()
                    .stream()
                    .filter(ppd -> ppd
                            .getProduct()
                            .getTicker()
                            .equals(orderRequest.getProduct()))
                    .findFirst();

            if (currentPortfolioProduct.isEmpty()) {

                throw new InvalidOrderRequestException("User does not own the specified product");
            }
            int userCurrentProductQuantity = currentPortfolioProduct
                    .get()
                    .getQuantity();

            return userCurrentProductQuantity >= orderRequest.getQuantity();

        }

    }
}
