package com.clientService.order.service;

import com.clientService.order.model.MarketData;
import com.clientService.order.model.MarketDataProduct;
import com.clientService.order.model.OrderModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


public class CachedMarketDataService {
    private static List<MarketDataProduct> cachedMarketDataProductsE1 = new ArrayList<>();
    private static List<MarketDataProduct> cachedMarketDataProductsE2 = new ArrayList<>();

    private static final RestTemplate restTemplate = new RestTemplate();
    private static String exchangeUrl1;
    private static String exchangeUrl2;
    private static String apiKey;

    @Value("${api.key}")
    public static void setApiKey(String apiKey) {
        CachedMarketDataService.apiKey = apiKey;
    }

    @Value("${exchange.url1}")
    public void setExchangeUrl1(String exchangeUrl1) {
        CachedMarketDataService.exchangeUrl1 = exchangeUrl1;
    }

    @Value(("${exchange.url2}"))
    public void setExchangeUrl2(String exchangeUrl2) {
        CachedMarketDataService.exchangeUrl2 = exchangeUrl2;
    }


//    static {
//        cachedMarketDataProductsE1 = new ArrayList<>();
//        cachedMarketDataProductsE2 = new ArrayList<>();
//        restTemplate = new RestTemplate();
//    }

    @CachePut(cacheNames = "marketData")
    public static void setMarketDataE1(List<MarketDataProduct> marketDataProducts) {
        CachedMarketDataService.cachedMarketDataProductsE1 = marketDataProducts;
    }

    @CachePut(cacheNames = "marketData")
    public static void setMarketDataE2(List<MarketDataProduct> marketDataProducts) {
        CachedMarketDataService.cachedMarketDataProductsE2 = marketDataProducts;
    }

    /**
     *
     * @return cached list of most recent market data from exchange 1
     */
    @Cacheable(cacheNames = "marketData")
    public static List<MarketDataProduct> getMarketDataE1() {
        if (CachedMarketDataService.cachedMarketDataProductsE1.isEmpty()) {
            MarketData marketData = restTemplate.getForObject(exchangeUrl1 + apiKey + "/md", MarketData.class);
            CachedMarketDataService.setMarketDataE1(marketData.getMarketDataProducts());
            return marketData.getMarketDataProducts();
        }
        return CachedMarketDataService.cachedMarketDataProductsE1;
    }

    /**
     *
     * @return cached list of most recent market data from exchange 2
     */
    @Cacheable(cacheNames = "marketData")
    public static List<MarketDataProduct> getMarketDataE2() {
        if (CachedMarketDataService.cachedMarketDataProductsE2.isEmpty()) {
            MarketData marketData = restTemplate.getForObject(exchangeUrl2 + apiKey + "/md", MarketData.class);
            CachedMarketDataService.setMarketDataE2(marketData.getMarketDataProducts());
            return marketData.getMarketDataProducts();
        }
        return CachedMarketDataService.cachedMarketDataProductsE2;
    }

//    Todo: schedule a chron job to call this method every hour
    /**
     * clears current cached market data from exchange 1
     */
    @CacheEvict(cacheNames = "marketData")
    public static void clearMarketDataE1() {

        CachedMarketDataService.cachedMarketDataProductsE1.clear();
    }

    /**
     * clears current cached market data from exchange 2
     */
    @CacheEvict(cacheNames = "marketData")
    public static void clearMarketDataE2() {

        CachedMarketDataService.cachedMarketDataProductsE2.clear();
    }

}
