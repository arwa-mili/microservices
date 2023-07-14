package discoveryserver.registerdiscovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class RegisterDiscoveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(RegisterDiscoveryApplication.class, args);
	}

}
