package com.clientService.securityConfig;

import com.clientService.user.model.MarketData;
import com.clientService.user.model.MarketProduct;
import com.clientService.user.repository.MarketDataRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class Subscriber implements MessageListener {
    private final MarketDataRepository marketDataRepository;

//    Logger logger =  LoggerFactory.getLogger(Subscriber.class);

    Subscriber(MarketDataRepository marketDataRepository){
        this.marketDataRepository = marketDataRepository;
    };

    @SneakyThrows
    @Override
    public void onMessage(Message message, byte[] pattern) {
        ObjectMapper objectMapper = new ObjectMapper();
        String body = new String(message.getBody());

        MarketProduct[] msg = objectMapper.readValue(body, MarketProduct[].class);
//        logger.info("Consumed Message {}", Arrays.asList(msg));
        Arrays.stream(msg).forEach(product -> marketDataRepository.save(
               new MarketData(Double.parseDouble(product.getLastTradedPrice()), product.getSellLimit(), product.getBidPrice(),
                product.getAskPrice(), product.getBuyLimit(), product.getTicker(), product.getMaxPriceShift())
        ));
    }
}
