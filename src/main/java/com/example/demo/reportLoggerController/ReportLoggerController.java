//package com.example.demo.reportLoggerController;
//
//import com.example.demo.entity.AuthenticationLog;
//import com.example.demo.entity.MarketDataLog;
//import com.example.demo.entity.OrderLog;
//import com.example.demo.reportLoggerService.ReportLoggerService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.time.LocalDateTime;
//
//@RestController
//public class ReportLoggerController {
//    @Autowired
//    ReportLoggerService reportLoggerService;
//
//    @RequestMapping("/report/authLog/{userID}")
//    public AuthenticationLog findUser(Long userID){
//        return reportLoggerService.findUser(userID);
//    }
//    @RequestMapping("/report/authLog/{id}")
//    public AuthenticationLog findUserByAuthID(Long id){
//        return reportLoggerService.findUserByAuthID(id);
//    }
//    @RequestMapping("/report/orderLog/{id}")
//    public OrderLog getReportByID(Long id) {
//        return reportLoggerService.getReportByID(id);
//    }
//    @RequestMapping("/report/orderLog/{timestamp}")
//    public OrderLog getReportByTimeStamp(LocalDateTime timestamp){
//        return reportLoggerService.getReportByTimeStamp(timestamp);
//    }
//    @RequestMapping("/report/orderLog/{userID}")
//    public OrderLog getReportByUserID(Long userID){
//        return reportLoggerService.getReportByUserID(userID);
//    }
//    @RequestMapping("/report/marketDataLog/{id}")
//    public MarketDataLog getMarketDataByID(Long id){
//        return reportLoggerService.getMarketDataByID(id);
//    }
//    @RequestMapping("/report/marketDataLog/{timestamp}")
//    public MarketDataLog getMarketDataByTimeStamp(LocalDateTime timestamp){
//        return reportLoggerService.getMarketDataByTimeStamp(timestamp);
//    }
//
//
//}
//
