package com.snackshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class SnackShopApplication {
	public static void main(String[] args) {
		SpringApplication.run(SnackShopApplication.class, args);
		System.out.println("零食商城后端启动成功！");
		System.out.println("Swagger UI: http://localhost:8081/api/swagger-ui.html");
		System.out.println("API文档: http://localhost:8081/api/v2/api-docs");
	}
}