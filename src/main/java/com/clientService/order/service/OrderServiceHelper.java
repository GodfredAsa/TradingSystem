package com.clientService.order.service;

import com.clientService.order.model.MarketDataProduct;
import com.clientService.order.model.OrderRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class OrderServiceHelper {

    @Autowired
    private RestTemplate restTemplate;

    @Value(("${api.key}"))
    private String apiKey;

    @Value("${exchange.url1}")
    private String exchangeUrl1;

    @Value(("${exchange.url2}"))
    private String exchangeUrl2;

    /**
     * @param orderRequest Body of the users order
     * @return A map containing a key(quantity of the best deal) and value (bid or ask price of the best deal)
     */
    //Returns the exchange and quantity to buy from first before the other
    public Map<Integer, String> getBestBidAndQuantity(OrderRequest orderRequest) {

        Optional<MarketDataProduct> bestOfExc1;
        Optional<MarketDataProduct> bestOfExc2;

        //Setting Best trades per order side
        if (orderRequest.getSide().equals("BUY")) {

            bestOfExc1 = /*exchange1Products*/ CachedMarketDataService.getMarketDataE1()
                    .stream()
                    .filter(prod -> prod.getTicker().equals(orderRequest.getProduct()))
                    .reduce((a, b) -> a.getAskPrice() < b.getAskPrice() ? a : b);

            bestOfExc2 = CachedMarketDataService.getMarketDataE2()
                    .stream()
                    .filter(prod -> prod.getTicker().equals(orderRequest.getProduct()))
                    .reduce((a, b) -> a.getAskPrice() < b.getAskPrice() ? a : b);

        } else {

            bestOfExc1 = /*exchange1Products*/ CachedMarketDataService.getMarketDataE1()
                    .stream()
                    .filter(prod -> prod.getTicker().equals(orderRequest.getProduct()))
                    .reduce((a, b) -> a.getBidPrice() > b.getBidPrice() ? a : b);

            bestOfExc2 = CachedMarketDataService.getMarketDataE2()
                    .stream()
                    .filter(prod -> prod.getTicker().equals(orderRequest.getProduct()))
                    .reduce((a, b) -> a.getBidPrice() > b.getBidPrice() ? a : b);

        }

        //check whether the product is available on both exchanges
        if (bestOfExc1.isPresent() && bestOfExc2.isPresent()) {


            int firstOrSecond;

//            Get the best exchange for a BUY order
            if (orderRequest.getSide().equals("BUY")) {

                firstOrSecond = Double.compare(bestOfExc1.get().getAskPrice(), bestOfExc2.get().getAskPrice());

                //If exchange1 selling price is lower
                if (firstOrSecond < 0) {

                    return new HashMap<Integer, String>() {{
                        put(bestOfExc1.get().getBuyLimit(), exchangeUrl1);
                    }};

                }
                //If exchange2 selling price is lower
                else {

                    return new HashMap<Integer, String>() {{
                        put(bestOfExc2.get().getBuyLimit(), exchangeUrl2);
                    }};
                }

            }
//            Get the best exchange for a SELL order
            else {
                firstOrSecond = Double.compare(bestOfExc1.get().getBidPrice(), bestOfExc2.get().getBidPrice());

                //If exchange1 buy price is higher
                if (firstOrSecond > 0) {

                    return new HashMap<Integer, String>() {{
                        put(bestOfExc1.get().getBuyLimit(), exchangeUrl1);
                    }};

                }
                //If exchange2 buy price is higher
                else {

                    return new HashMap<Integer, String>() {{
                        put(bestOfExc2.get().getBuyLimit(), exchangeUrl2);
                    }};
                }
            }

        }
        //If product is only on exchange1
        else if (bestOfExc1.isPresent()) {

            return new HashMap<Integer, String>() {{
                put(orderRequest.getSide().equals("BUY") ? bestOfExc1.get().getBuyLimit() : bestOfExc1.get().getSellLimit(), exchangeUrl1);
            }};

        }
        //If product is only on exchange2
        else if (bestOfExc2.isPresent()) {

            return new HashMap<Integer, String>() {{
                put(orderRequest.getSide().equals("BUY") ? bestOfExc2.get().getBuyLimit() : bestOfExc2.get().getSellLimit(), exchangeUrl2);
            }};

        }
        //If product is not yet on any exchange
        else {

            //just split then in two equal halves
            return new HashMap<Integer, String>() {{
                put((orderRequest.getQuantity() / 2), exchangeUrl1);
            }};
        }
    }
}
