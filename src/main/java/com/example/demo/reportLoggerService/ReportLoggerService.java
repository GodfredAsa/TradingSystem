package com.example.demo.reportLoggerService;

import com.example.demo.entities.AuthenticationLog;
import com.example.demo.entities.MarketDataLog;
import com.example.demo.entities.OrderLog;
import com.example.demo.enums.Status;
import com.example.demo.repositories.AuthenticationLogRepository;
import com.example.demo.repositories.MarketDataLogRepository;
import com.example.demo.repositories.OrderLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportLoggerService {
    @Autowired
    private AuthenticationLogRepository authenticationLogRepository;
    @Autowired
    private OrderLogRepository orderLogRepository;
    @Autowired
    private MarketDataLogRepository marketDataLogRepository;

    /**
     * Method for querying authentication log by userID
     */
    public AuthenticationLog findByUserID(Long userID) {
        return authenticationLogRepository.findByUserID(userID);
    }

    /**
     * Method for querying authentication log by id
     */
    public AuthenticationLog findUserById(Long id) {
        return authenticationLogRepository.findUserById(id);
    }

    /**
     * Method for saving authentication log onto the database
     */
    public void logUserAuthentication(AuthenticationLog authenticationLog) {
        authenticationLogRepository.save(authenticationLog);
    }

    /**
     * Method for saving orders on the database
     */
    public void orders(OrderLog orderLog) {
        orderLogRepository.save(orderLog);
    }

    /**
     * Method for updating status in the repository
     **/
    public void statusUpdate(String orderID, Status status) {
        OrderLog orderLog = orderLogRepository.findByOrderID(orderID);
        orderLog.setStatus(status);
        orderLogRepository.save(orderLog);

    }

    /**
     * Method for getting order log by timestamp
     */
    public OrderLog findByTimestamp(LocalDateTime timestamp) {
        return orderLogRepository.findByTimestamp(timestamp);
    }

    /**
     * Method for getting order log by id
     **/
    public OrderLog findByID(Long id) {
        return orderLogRepository.findById(id).orElseThrow(() -> new RuntimeException("item not found"));
    }

    /**
     * Method for getting order log by userID
     */
    public List<OrderLog> getByUserID(Long userID) {
        return orderLogRepository.getByUserID(userID);
    }

    /**
     * Method for saving market data log onto the database
     */ //TODO: Review
    public void marketDataReport(MarketDataLog marketDataLog) {
        marketDataLogRepository.save(marketDataLog);
    }

}



