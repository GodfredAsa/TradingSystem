package com.clientService.user.repository;

import com.clientService.user.model.MarketData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketDataRepository extends JpaRepository<MarketData, String> {
}
