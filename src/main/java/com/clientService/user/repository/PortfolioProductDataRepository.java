package com.clientService.user.repository;

import com.clientService.user.model.PortfolioProductData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioProductDataRepository extends JpaRepository<PortfolioProductData, Long> {
}
