package com.clientService.order.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class OrderBook {
    private ArrayList<FullOrderBook> fullOrderBooks;

    public OrderBook() {
        this.fullOrderBooks = new ArrayList<>();
    }
}