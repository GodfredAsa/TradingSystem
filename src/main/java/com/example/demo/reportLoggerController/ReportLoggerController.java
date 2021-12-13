package com.example.demo.reportLoggerController;

import com.example.demo.entities.AuthenticationLog;
import com.example.demo.entities.MarketDataLog;
import com.example.demo.entities.OrderLog;
import com.example.demo.reportLoggerService.ReportLoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@RestController
@RequestMapping("/api")
public class ReportLoggerController {
    @Autowired
    ReportLoggerService reportLoggerService;

    /**Endpoint for authentication log*/
    @PostMapping("/log/userAuthentication")
    public void logUserAuthentication(@RequestBody AuthenticationLog authenticationLog) {
        this.reportLoggerService.logUserAuthentication(authenticationLog);
    }

    //TODO: Review
    @PostMapping
    public void marketDataReport(@RequestBody MarketDataLog marketDataLog) {
        this.reportLoggerService.marketDataReport(marketDataLog);
    }

    /**Endpoint for order log*/
    @PostMapping("/log/orders")
    public void orders(@RequestBody OrderLog orderLog) {
        this.reportLoggerService.orders(orderLog);
    }

    /** Endpoint for getting authentication log by userID*/
    @GetMapping("/report/authLog/{userID}")
    public AuthenticationLog findByUserID(@PathVariable Long userID) {
        return reportLoggerService.findByUserID(userID);
    }

    // Endpoint for getting authentication log by userID
    @GetMapping("/report/authLog/{id}")
    public AuthenticationLog findUserById(@PathVariable Long id) {
        return reportLoggerService.findUserById(id);
    }

    // Endpoint for getting order log by id
    @GetMapping("/report/orderLog/{id}")
    public OrderLog getReportById(@PathVariable Long id) {
        return reportLoggerService.getReportById(id);
    }

    // Endpoint for getting order log by timestamp
    @GetMapping("/report/orderLog/{timestamp}")
    public OrderLog getReportByTimeStamp(@PathVariable String timestamp) {
        return reportLoggerService.getReportByTimestamp(timestamp);
    }

    //Endpoint for getting order log by userID
    @GetMapping("/report/orderLog/{userID}")
    public OrderLog getReportByUserID(@PathVariable Long userID) {
        return reportLoggerService.getReportByUserID(userID);
    }


}

