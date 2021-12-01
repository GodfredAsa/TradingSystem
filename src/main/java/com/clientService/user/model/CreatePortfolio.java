package com.clientService.user.model;

import com.clientService.enums.PortfolioStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreatePortfolio {
    private Long id;
    private PortfolioStatus status;
}
