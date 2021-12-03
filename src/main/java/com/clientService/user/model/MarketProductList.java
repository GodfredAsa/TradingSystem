package com.clientService.user.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MarketProductList {
    private List<MarketProduct> marketProducts;

    public MarketProductList() {
        this.marketProducts = new ArrayList<>();
    }
}
