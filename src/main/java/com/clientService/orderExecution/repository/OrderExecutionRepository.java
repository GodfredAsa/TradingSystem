package com.clientService.orderExecution.repository;

import com.clientService.order.model.Order;
import com.clientService.orderExecution.model.OrderExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderExecutionRepository extends JpaRepository<OrderExecution, Long>{
}
