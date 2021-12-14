package com.clientService.order.service;

import com.clientService.loggerPack.LoggerConfig;
import com.clientService.order.model.OrderModel;
import com.clientService.order.model.OrderResponse;
import com.clientService.order.repository.OrderRepository;
import com.clientService.user.model.Account;
import com.clientService.user.model.AppUser;
import com.clientService.user.model.PortfolioProductData;
import com.clientService.user.repository.AppUserRepository;
import com.clientService.user.repository.PortfolioRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class CancelOrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${exchange.url1}")
    private String exchangeUrl1;

    @Value(("${exchange.url2}"))
    private String exchangeUrl2;


    public boolean cancelOrder(long portfolioId, String orderId, UserDetails currentUser) {

        ResponseEntity<OrderResponse> responseFromExchange1;
        ResponseEntity<OrderResponse> responseFromExchange2;

        if (!currentUser.isCredentialsNonExpired())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "User credentials are expired, please login.");

        try {

            responseFromExchange1 = restTemplate.exchange(
                    exchangeUrl1,
                    HttpMethod.DELETE,
                    null,
                    OrderResponse.class);

//            restTemplate.delete(exchangeUrl1 + apiKey + "/order/" + orderId);

        } catch (Exception e1) {

            responseFromExchange1 = new ResponseEntity<OrderResponse>(new OrderResponse(), HttpStatus.PRECONDITION_FAILED);
        }

        try {

            responseFromExchange2 = restTemplate.exchange(
                    exchangeUrl2,
                    HttpMethod.DELETE,
                    null,
                    OrderResponse.class);

//            restTemplate.delete(exchangeUrl2 + apiKey + "/order/" + orderId, OrderModel.class);

        } catch (Exception e2) {

            responseFromExchange2 = new ResponseEntity<OrderResponse>(new OrderResponse(), HttpStatus.PRECONDITION_FAILED);
        }


        if (responseFromExchange1.getStatusCode().equals(HttpStatus.OK) || responseFromExchange2.getStatusCode().equals(HttpStatus.OK)) {

            OrderResponse cancelledOrder = responseFromExchange1.getStatusCode().equals(HttpStatus.OK) ? responseFromExchange1.getBody() : responseFromExchange2.getBody();
            AppUser user = appUserRepository.getAppUserByEmail(currentUser.getUsername());

            try {

                closeOrder(user, cancelledOrder, portfolioId);

            } catch (Exception e) {

                LoggerConfig.LOGGER.info("=================================== Error closing order gracefully: " + e.getMessage() + " ===================================\n");
            }


            List<OrderModel> portfolioOrders = user
                    .getPortfolios()
                    .stream()
                    .filter(portfolio -> portfolio
                            .getId()
                            .equals(portfolioId))
                    .findFirst()
                    .get()
                    .getOrders();

            portfolioOrders
                    .remove(portfolioOrders
                            .stream()
                            .filter(order -> order
                                    .getId()
                                    .equals(orderId))
                            .findFirst()
                            .get());

            orderRepository.deleteById(orderId);

            return true;

        } else {
            return false;
        }

        //Todo: deduct the add the remaining sum back to the users account and remember to deduct the previous order from the user
    }

    /**
     * @param user  User to be refunded on successful order cancellation
     * @param order The current state of the order requested to be cancelled
     */
    private void closeOrder(AppUser user, OrderResponse order, long portfolioId) {

        if (order.getSide().equals("BUY")) {

            //refund the total price for the quantity of un-executed orders
            double refundAmount = order.getPrice() * (order.getQuantity() - order.getCumulativeQuantity());
            Account userAccount = user.getAccount();
            userAccount.setBalance(userAccount.getBalance() + refundAmount);
        }
        //payout the total price for the quantity of the order executed
        else if (order.getSide().equals("SELL")) {

            double totalSales = order.getPrice() * order.getCumulativeQuantity();

            Account userAccount = user.getAccount();
            userAccount.setBalance(userAccount.getBalance() + totalSales);

            PortfolioProductData portfolioProductData = portfolioRepository
                    .findById(portfolioId)
                    .get()
                    .getPortfolioProductData()
                    .stream()
                    .filter(ppd -> ppd
                            .getProduct()
                            .getTicker()
                            .equals(order.getProduct()) &&
                            ppd
                                    .getPortfolio()
                                    .getId()
                                    .equals(portfolioId))
                    .findFirst()
                    .get();

            portfolioProductData.setQuantity(portfolioProductData.getQuantity() - order.getCumulativeQuantity());

        }
    }
}
