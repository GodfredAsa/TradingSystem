package com.example.demo.reportLoggerController;

import com.example.demo.entities.AuthenticationLog;
import com.example.demo.entities.MarketDataLog;
import com.example.demo.entities.OrderLog;
import com.example.demo.enums.Status;
import com.example.demo.reportLoggerService.ReportLoggerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/api")
@Api(value = "Holds the endpoint for various entities", tags = "Reporting and Logging Service Controller")
public class ReportLoggerController {
    @Autowired
    ReportLoggerService reportLoggerService;


    /**
     * Endpoint for getting authentication log by userID
     **/

    @GetMapping("/authLog/{userID}")
    @ApiOperation("Get authentication report by user id")
    public AuthenticationLog findByUserID(@PathVariable Long userID) {
        return reportLoggerService.findByUserID(userID);
    }

    /**
     * Endpoint for getting authentication log by userID
     **/
    @GetMapping("/authLog/{id}")
    @ApiOperation("Get authentication report by userID")
    public AuthenticationLog findUserById(@PathVariable Long id) {
        return reportLoggerService.findUserById(id);
    }

    /**
     * Endpoint for authentication log
     **/
    @PostMapping("/log/userAuthentication")
    @ApiOperation(" Endpoint for authentication log entity")
    public void logUserAuthentication(@RequestBody AuthenticationLog authenticationLog) {
        this.reportLoggerService.logUserAuthentication(authenticationLog);
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
     * Endpoint for updating status
     **/
    @PostMapping("/log/statusUpdate")
    @ApiOperation("Endpoint for status update")
    public void statusUpdate(@RequestBody String orderID, Status status) {
        this.reportLoggerService.statusUpdate(orderID, status);
    }

    /**
     * Endpoint for getting order log by timestamp
     **/
    @GetMapping("/orderLog/{timestamp}")
    @ApiOperation("Get order report by timestamp")
    public OrderLog findByTimeStamp(@PathVariable LocalDateTime timestamp) {
        return reportLoggerService.findByTimestamp(timestamp);
    }

    /**
     * Endpoint for getting order log by order id
     **/
    @GetMapping("/orderLog/{id}")
    @ApiOperation("Get order report by orderID")
    public OrderLog findByID(@PathVariable Long id) {
        return reportLoggerService.findByID(id);
    }


    /**
     * Endpoint for getting order log by userID
     **/
    @GetMapping("/orderLog/{userID}")
    @ApiOperation("Get order log report by user id")
    public List<OrderLog> getByUserID(@PathVariable Long userID) {
        return reportLoggerService.getByUserID(userID);
    }

    //TODO: Review
    @PostMapping
    public void marketDataReport(@RequestBody MarketDataLog marketDataLog) {
        this.reportLoggerService.marketDataReport(marketDataLog);
    }
}

