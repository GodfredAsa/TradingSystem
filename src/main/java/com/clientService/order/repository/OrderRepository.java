package com.clientService.order.repository;

import com.clientService.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    @Query("SELECT * FROM Order o " +
            "Where o.user_id = ?3 " +
            "and  o.product = ?1 " +
            "and o.side = 'BUY' " +
            "and o.cumulative_quantity < o.quantity")
    List<Order> getAllUsersBuyOrdersOfAProduct(String productTicker, Long user_id);

//    Order getOrderById(String id);



}
