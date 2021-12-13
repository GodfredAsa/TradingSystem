package com.example.demo.repositories;

import com.example.demo.entities.OrderLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface OrderLogRepository extends JpaRepository<OrderLog, Long> {
    //Methods for  querying the database for order log
    public OrderLog getReportByTimestamp(String timestamp);

    public OrderLog getReportById(long id);

    public OrderLog getReportByUserID(long userID);
}