package com.clientService.order.service;

import com.clientService.order.model.MarketDataProduct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class CachedMarketDataService {
    private List<MarketDataProduct> cachedMarketDataProductsE1 = new ArrayList<>();
    private List<MarketDataProduct> cachedMarketDataProductsE2 = new ArrayList<>();

    private final RestTemplate restTemplate;

    @Value("${exchange.url1}")
    private String exchangeUrl1;

    @Value(("${exchange.url2}"))
    private String exchangeUrl2;

    @Value("${api.key}")
    private String apiKey;

    public CachedMarketDataService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(converter);
        this.restTemplate.setMessageConverters(messageConverters);
    }

    //     {
//        cachedMarketDataProductsE1 = new ArrayList<>();
//        cachedMarketDataProductsE2 = new ArrayList<>();
//        restTemplate = new RestTemplate();
//    }

    @CachePut(cacheNames = "marketData1")
    public void setMarketDataE1(List<MarketDataProduct> marketDataProducts) {
        this.cachedMarketDataProductsE1 = marketDataProducts;
    }

    @CachePut(cacheNames = "marketData2")
    public void setMarketDataE2(List<MarketDataProduct> marketDataProducts) {
        this.cachedMarketDataProductsE2 = marketDataProducts;
    }

    /**
     * @return cached list of most recent market data from exchange 1
     */
    @Cacheable(cacheNames = "marketData1")
    public List<MarketDataProduct> getMarketDataE1() {
        if (this.cachedMarketDataProductsE1.isEmpty()) {
            System.out.println("------------------------------------------- url " + exchangeUrl1 + "/md");
            MarketDataProduct[] marketData = restTemplate.getForObject(exchangeUrl1 + "/md", MarketDataProduct[].class);
            System.out.println("------------------------------------------- data1 " + exchangeUrl1 + marketData);
            List<MarketDataProduct> marketProductsList = Arrays.asList(marketData);
            System.out.println("------------------------------------------- dataList " + marketProductsList);
            this.setMarketDataE1(marketProductsList);
            return marketProductsList;
        }
        return this.cachedMarketDataProductsE1;
    }

    /**
     * @return cached list of most recent market data from exchange 2
     */
    @Cacheable(cacheNames = "marketData2")
    public List<MarketDataProduct> getMarketDataE2() {
        if (this.cachedMarketDataProductsE2.isEmpty()) {
            System.out.println("------------------------------------------- url " + exchangeUrl2 + "/md");
            MarketDataProduct[] marketData = restTemplate.getForObject(exchangeUrl2 + "/md", MarketDataProduct[].class);
            System.out.println("------------------------------------------- data " + exchangeUrl2 + marketData);
            List<MarketDataProduct> marketProductsList = Arrays.asList(marketData);
            System.out.println("------------------------------------------- data " + marketProductsList);
            this.setMarketDataE2(marketProductsList);
            return marketProductsList;
        }
        return this.cachedMarketDataProductsE2;
    }

//    Todo: schedule a chron job to call this method every hour

    /**
     * clears current cached market data from exchange 1
     */
    @CacheEvict(cacheNames = "marketData1")
    public void clearMarketDataE1() {

        this.cachedMarketDataProductsE1.clear();
    }

    /**
     * clears current cached market data from exchange 2
     */
    @CacheEvict(cacheNames = "marketData2")
    public void clearMarketDataE2() {

        this.cachedMarketDataProductsE2.clear();
    }

}
