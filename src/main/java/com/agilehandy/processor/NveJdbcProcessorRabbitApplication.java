package com.agilehandy.processor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@SpringBootApplication
public class NveJdbcProcessorRabbitApplication {

	public static void main(String[] args) {
		SpringApplication.run(NveJdbcProcessorRabbitApplication.class, args);
	}


	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
}
