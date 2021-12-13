package com.clientService.order.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FullOrderBook {

    @JsonProperty("fullOrderBook")
    ArrayList<OrderResponse> fullOrderBook;

}
