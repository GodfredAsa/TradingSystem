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

    @PostMapping("/log/userAuthentication")
    public void logUserAuthentication(@RequestBody AuthenticationLog authenticationLog) {
        this.reportLoggerService.logUserAuthentication(authenticationLog);
    }

    @PostMapping
    public void marketDataReport(@RequestBody MarketDataLog marketDataLog) {
        this.reportLoggerService.marketDataReport(marketDataLog);
    }

    @PostMapping("/log/orders")
    public void orders(@RequestBody OrderLog orderLog) {
        this.reportLoggerService.orders(orderLog);
    }

    @GetMapping("/report/authLog/{userID}")
    public AuthenticationLog findByUserID(Long userID) {
        return reportLoggerService.findByUserID(userID);
    }

    @GetMapping("/report/authLog/{id}")
    public AuthenticationLog findUserById(Long id) {
        return reportLoggerService.findUserById(id);
    }

    @GetMapping("/report/orderLog/{id}")
    public OrderLog getReportById(Long id) {
        return reportLoggerService.getReportById(id);
    }

    @GetMapping("/report/orderLog/{timestamp}")
    public OrderLog getReportByTimeStamp(LocalDateTime timestamp) {
        return reportLoggerService.getReportByTimestamp(timestamp);
    }

    @GetMapping("/report/orderLog/{userID}")
    public OrderLog getReportByUserID(Long userID) {
        return reportLoggerService.getReportByUserID(userID);
    }


}

