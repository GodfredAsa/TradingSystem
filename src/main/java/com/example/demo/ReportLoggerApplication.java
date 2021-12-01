package com.example.demo;

//import com.example.demo.repository.AuthenticationLogRepository;
//import com.example.demo.repository.MarketDataLogRepository;
//import com.example.demo.repository.OrderLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import javax.annotation.sql.DataSourceDefinition;


@SpringBootApplication()
//@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})

public class ReportLoggerApplication {
	private static final Logger logger  = LoggerFactory.getLogger(ReportLoggerApplication.class);
//	@Autowired
//	private AuthenticationLogRepository authenticationLogRepository;
//	@Autowired
//	private OrderLogRepository orderLogRepository;
//	@Autowired
////	private MarketDataLogRepository marketDataLogRepository;
	public static void main(String[] args) {

		SpringApplication.run(ReportLoggerApplication.class, args);
		logger.info("-----> Reporting and Logging Service<-----");
	}

}
