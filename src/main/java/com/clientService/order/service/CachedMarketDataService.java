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

    private static List<MarketDataProduct> cachedMarketDataProductsE1;
    private static List<MarketDataProduct> cachedMarketDataProductsE2;

    private static RestTemplateBuilder restTemplateBuilder;
    private static final RestTemplate restTemplate;

    private static String exchangeUrl1;
    private static String exchangeUrl2;


    @Value("${exchange.url1}")
    public void setExchangeUrl1(String exchangeUrl1) {
        CachedMarketDataService.exchangeUrl1 = exchangeUrl1;
    }

    @Value(("${exchange.url2}"))
    public static void setExchangeUrl2(String exchangeUrl2) {
        CachedMarketDataService.exchangeUrl2 = exchangeUrl2;
    }


    static {
        restTemplateBuilder = new RestTemplateBuilder();
        restTemplate = restTemplateBuilder.build();
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(converter);
        restTemplate.setMessageConverters(messageConverters);

        cachedMarketDataProductsE1 = new ArrayList<>();
        cachedMarketDataProductsE2 = new ArrayList<>();
    }


    @CachePut(cacheNames = "marketData1"
    public static void setMarketDataE1(List<MarketDataProduct> marketDataProducts) {
        CachedMarketDataService.cachedMarketDataProductsE1 = marketDataProducts;
    }


    @CachePut(cacheNames = "marketData2")
    public static void setMarketDataE2(List<MarketDataProduct> marketDataProducts) {
        CachedMarketDataService.cachedMarketDataProductsE2 = marketDataProducts;
    }

    /**
     *
     * @return cached list of most recent market data from exchange 1
     */

    @Cacheable(cacheNames = "marketData1")
    public static List<MarketDataProduct> getMarketDataE1() {
        if (CachedMarketDataService.cachedMarketDataProductsE1.isEmpty()) {
            MarketDataProduct[] marketData = restTemplate.getForObject(exchangeUrl1 + "/md", MarketDataProduct[].class);
            List<MarketDataProduct> marketProductsList = Arrays.asList(marketData);
            CachedMarketDataService.setMarketDataE1(marketProductsList);
            return marketProductsList;
        }
        return CachedMarketDataService.cachedMarketDataProductsE1;
    }

    /**
     *
     * @return cached list of most recent market data from exchange 2
     */

    @Cacheable(cacheNames = "marketData2")
    public static List<MarketDataProduct> getMarketDataE2() {
        if (CachedMarketDataService.cachedMarketDataProductsE2.isEmpty()) {
            MarketDataProduct[] marketData = restTemplate.getForObject(exchangeUrl2 + "/md", MarketDataProduct[].class);
            List<MarketDataProduct> marketProductsList = Arrays.asList(marketData);
            CachedMarketDataService.setMarketDataE2(marketProductsList);
            return marketProductsList;

        }
        return CachedMarketDataService.cachedMarketDataProductsE2;
    }

//    Todo: schedule a chron job to call this method every hour
    /**
     * clears current cached market data from exchange 1
     */

    @CacheEvict(cacheNames = "marketData1")
    public static void clearMarketDataE1() {

        if (CachedMarketDataService.cachedMarketDataProductsE1.isEmpty())
            return;

        CachedMarketDataService.cachedMarketDataProductsE1.clear();
    }

    /**
     * clears current cached market data from exchange 2
     */

    @CacheEvict(cacheNames = "marketData2")
    public static void clearMarketDataE2() {
        if (CachedMarketDataService.cachedMarketDataProductsE2.isEmpty())
            return;

        CachedMarketDataService.cachedMarketDataProductsE2.clear();
    }

}
