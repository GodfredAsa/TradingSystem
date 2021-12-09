package com.clientService.user.repository;

import com.clientService.order.model.MarketDataProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketDataRepository extends JpaRepository<MarketDataProduct, String> {
}
