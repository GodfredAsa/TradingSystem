package com.example.demo.repository;

import com.example.demo.entity.OrderLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface OrderLogRepository extends JpaRepository<OrderLog, Long> {

//    public OrderLog getReportByTimeStamp(LocalDateTime timestamp);
//    public OrderLog getReportByID(Long id);
    public OrderLog getReportByUserID(Long userID);
}