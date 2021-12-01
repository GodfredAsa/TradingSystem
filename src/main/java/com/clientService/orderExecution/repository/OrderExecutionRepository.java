package com.clientService.orderExecution.repository;

import com.clientService.orderExecution.model.OrderExecutionModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderExecutionRepository extends JpaRepository<OrderExecutionModule, Long>{
}
