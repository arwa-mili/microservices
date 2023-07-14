package com.productservice.productservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.MongoDBContainer;

@SpringBootTest
class ProductserviceApplicationTests {

	MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.2.2");

	@Test
	void contextLoads() {
	}

}
