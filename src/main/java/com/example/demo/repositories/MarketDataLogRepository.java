package com.example.demo.repositories;
import com.example.demo.entities.MarketDataLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MarketDataLogRepository extends JpaRepository<MarketDataLog,Long> { }
