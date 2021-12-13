package com.example.demo.reportLoggerController;

import com.example.demo.entities.AuthenticationLog;
import com.example.demo.entities.MarketDataLog;
import com.example.demo.entities.OrderLog;
import com.example.demo.reportLoggerService.ReportLoggerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@Api(value = "Holds the endpoint for various entities", tags = "Reporting and Logging Service Controller")
public class ReportLoggerController {
    @Autowired
    ReportLoggerService reportLoggerService;

    /**
     * Endpoint for authentication log
     **/
    @PostMapping("/log/userAuthentication")
    @ApiOperation(" Endpoint for authentication log entity")
    public void logUserAuthentication(@RequestBody AuthenticationLog authenticationLog) {
        this.reportLoggerService.logUserAuthentication(authenticationLog);
    }

    //TODO: Review
    @PostMapping
    public void marketDataReport(@RequestBody MarketDataLog marketDataLog) {
        this.reportLoggerService.marketDataReport(marketDataLog);
    }

    /**
     * Endpoint for order log
     **/
    @PostMapping("/log/orders")
    @ApiOperation(" Endpoint for order log entity")
    public void orders(@RequestBody OrderLog orderLog) {
        this.reportLoggerService.orders(orderLog);
    }

    /**
     * Endpoint for getting authentication log by userID
     **/

    @GetMapping("/report/authLog/{userID}")
    @ApiOperation("Get authentication report by user id")
    public AuthenticationLog findByUserID(@PathVariable Long userID) {
        return reportLoggerService.findByUserID(userID);
    }

    /**
     * Endpoint for getting authentication log by userID
     **/
    @GetMapping("/report/authLog/{id}")
    @ApiOperation("Get authentication report by userID")
    public AuthenticationLog findUserById(@PathVariable Long id) {
        return reportLoggerService.findUserById(id);
    }

    /**
     * Endpoint for getting order log by order id
     **/
    @GetMapping("/report/orderLog/{id}")
    @ApiOperation("Get order report by orderID")
    public OrderLog getReportById(@PathVariable Long id) {
        return reportLoggerService.getReportById(id);
    }

    /**
     * Endpoint for getting order log by timestamp
     **/
    @GetMapping("/report/orderLog/{timestamp}")
    @ApiOperation("Get order report by timestamp")
    public OrderLog getReportByTimeStamp(@PathVariable String timestamp) {
        return reportLoggerService.getReportByTimestamp(timestamp);
    }

    /**
     * Endpoint for getting order log by userID
     **/
    @GetMapping("/report/orderLog/{userID}")
    @ApiOperation("Get order log report by user id")
    public OrderLog getReportByUserID(@PathVariable Long userID) {
        return reportLoggerService.getReportByUserID(userID);
    }


}

