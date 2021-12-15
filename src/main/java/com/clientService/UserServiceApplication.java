package com.clientService;

import com.clientService.order.model.Product;
import com.clientService.order.repository.ProductRepository;
import org.json.JSONObject;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@SpringBootApplication
@EnableCaching
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner run(ProductRepository productRepository){
        return args -> {
            productRepository.saveAll(List.of(
                    new Product("IBM", "IBM", "IBM, Corp"),
                    new Product("AAPL", "APPLE", "Apple, Inc"),
                    new Product("GOOGL", "GOOGLE", "Google LLC"),
                    new Product("MSFT", "MICROSOFT", "Microsoft Corporation"),
                    new Product("NFLX", "NETFLIX", "NetFlix, Inc"),
                    new Product("ORCL", "ORACLE", "Oracle Corporation"),
                    new Product("TSLA", "TESLA", "Tesla Motors, Inc"),
                    new Product("AMZN", "AMAZON", "Amazon, Inc")));
        };
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

}
