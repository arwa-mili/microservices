package notifications.service.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@Slf4j
@EnableDiscoveryClient
public class NotificationApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationApplication.class, args);
	}

	@KafkaListener(topics = "notificationTopic")
	public void handleNotification(OrderPlacedEvent orderPlacedEvent){
		//send an email notif:
<<<<<<< HEAD
		log.info("Received notification for order - {}",orderPlacedEvent.getOrderNumber());
=======
		log.info("Recieved notification for order - {}",orderPlacedEvent.getOrderNumber());
>>>>>>> origin/master

	}

}
