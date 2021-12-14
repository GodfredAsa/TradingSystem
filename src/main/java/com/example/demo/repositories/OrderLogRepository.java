package com.example.demo.repositories;

import com.example.demo.entities.OrderLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderLogRepository extends JpaRepository<OrderLog, Long> {
    /**
     * Methods for  querying the database for order log
     **/

    public OrderLog findByTimestamp(LocalDateTime timeStamp);

    public OrderLog findByOrderID(String orderId);

    public List<OrderLog> getByUserID(long userID);
}