//package com.example.demo.reportLoggerService;
//
//import com.example.demo.entity.AuthenticationLog;
//import com.example.demo.entity.MarketDataLog;
//import com.example.demo.entity.OrderLog;
//import com.example.demo.repository.AuthenticationLogRepository;
//import com.example.demo.repository.MarketDataLogRepository;
//import com.example.demo.repository.OrderLogRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import java.time.LocalDateTime;
//
//@Service
//public class ReportLoggerService {
//    @Autowired
//    private AuthenticationLogRepository authenticationLogRepository;
//    @Autowired
//    private OrderLogRepository orderLogRepository;
//    @Autowired
//    private MarketDataLogRepository marketDataLogRepository;
//
//
//    public AuthenticationLog findUser(Long userID){
//       return authenticationLogRepository.findUser(userID);
//    }
//    public AuthenticationLog findUserByAuthID(Long id){
//        return authenticationLogRepository.findUserByAuthID(id);
//    }
//    public OrderLog getReportByID(Long id) {
//        return orderLogRepository.getReportByID(id);
//    }
//    public OrderLog getReportByTimeStamp(LocalDateTime timestamp){
//        return orderLogRepository.getReportByTimeStamp(timestamp);
//    }
//    public OrderLog getReportByUserID(Long userID){
//        return orderLogRepository.getReportByUserID(userID);
//    }
//    public MarketDataLog getMarketDataByID(Long id){
//        return marketDataLogRepository.getMarketDataByID(id);
//    }
//    public MarketDataLog getMarketDataByTimeStamp(LocalDateTime timestamp){
//        return marketDataLogRepository.getMarketDataByTimeStamp(timestamp);
//    }
//
//}
