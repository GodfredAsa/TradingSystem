package com.clientService.order.model;

import com.clientService.user.model.MarketProduct;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderBook {
    private ArrayList<FullOrderBook> fullOrderBooks;

    public OrderBook() {
        this.fullOrderBooks = new ArrayList<>();
    }
}