package com.clientService.order.repository;

import com.clientService.order.model.OrderModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderModel, String> {

    @Query(value = "SELECT o FROM order_model o Where o.user_id = :user_id and  o.product_id = :product_ticker and o.side = :side and o.cumulative_quantity < o.quantity", nativeQuery = true)
    ArrayList<OrderModel> getAllUsersBuyOrdersOfAProduct(@Param("product_ticker") String productTicker, @Param("user_id") Long user_id, @Param("side") String side);

}
