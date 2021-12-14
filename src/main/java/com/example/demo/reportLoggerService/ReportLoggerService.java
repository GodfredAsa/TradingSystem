package com.example.demo.reportLoggerService;

import com.example.demo.entities.AuthenticationLog;
import com.example.demo.entities.MarketDataLog;
import com.example.demo.entities.OrderLog;
import com.example.demo.repositories.AuthenticationLogRepository;
import com.example.demo.repositories.MarketDataLogRepository;
import com.example.demo.repositories.OrderLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;

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
     * Method for getting order log by id
     */
    public OrderLog getReportById(Long id) {
        return orderLogRepository.getReportById(id);
    }

    /**
     * Method for getting order log by timestamp
     */
    public OrderLog getReportByTimestamp(String timestamp) {
        return orderLogRepository.getReportByTimestamp(timestamp);
    }

    /**
     * Method for getting order log by userID
     */
    public OrderLog getReportByUserID(Long userID) {
        return orderLogRepository.getReportByUserID(userID);
    }

    /**
     * Method for saving authentication log onto the database
     */
    public void logUserAuthentication(AuthenticationLog authenticationLog) {
        authenticationLogRepository.save(authenticationLog);
    }

    /**
     * Method for saving market data log onto the database
     */ //TODO: Review
    public void marketDataReport(MarketDataLog marketDataLog) {
        marketDataLogRepository.save(marketDataLog);
    }

    /**
     * Method for saving orders on the database
     */

    public void orders(OrderLog orderLog) {
        orderLogRepository.save(orderLog);
    }

}
