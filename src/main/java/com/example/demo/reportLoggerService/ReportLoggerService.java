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


    public AuthenticationLog findByUserID(Long userID){
        return authenticationLogRepository.findByUserID(userID);
    }
    public AuthenticationLog findUserById(Long id){
        return authenticationLogRepository.findUserById(id);
    }
    public OrderLog getReportById(Long id) {
        return orderLogRepository.getReportById(id);
    }
    public OrderLog getReportByTimestamp(LocalDateTime timestamp){
        return orderLogRepository.getReportByTimestamp(timestamp);
    }
    public OrderLog getReportByUserID(Long userID){
        return orderLogRepository.getReportByUserID(userID);
    }

    public void logUserAuthentication(AuthenticationLog authenticationLog) {
        authenticationLogRepository.save(authenticationLog);
    }
    public void marketDataReport(MarketDataLog marketDataLog){
        marketDataLogRepository.save(marketDataLog);
    }
    public void orders (OrderLog orderLog){
        orderLogRepository.save(orderLog);
    }

}
