package com.example.demo.repositories;
import com.example.demo.entities.MarketDataLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
@Repository
public interface MarketDataLogRepository extends JpaRepository<MarketDataLog,Long> {

   public MarketDataLog getMarketDataById(Long id);
   public MarketDataLog getMarketDataByTimestamp(LocalDateTime timeStamp);
}
