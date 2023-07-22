package com.orders.ordersService.service;

import com.orders.ordersService.dto.InventoryResponse;
import com.orders.ordersService.dto.OrderLineItemsDto;
import com.orders.ordersService.dto.OrderRequest;
import com.orders.ordersService.event.OrderPlacedEvent;
import com.orders.ordersService.model.Order;
import com.orders.ordersService.model.OrderLineItems;
import com.orders.ordersService.repository.OrderRepository;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import javassist.bytecode.stackmap.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    private final KafkaTemplate<String,OrderPlacedEvent> kafkaTemplate;



    private final ApplicationEventPublisher applicationEventPublisher;




    public String placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getSkuCode)
                .toList();
        InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
                .uri("http://invenory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve().bodyToMono(InventoryResponse[].class).block();
        /*
         */
        boolean allProductsInStock = Arrays.stream(inventoryResponseArray).allMatch(InventoryResponse::isInStock);
        if(allProductsInStock){
            orderRepository.save(order);
            kafkaTemplate.send("notificationTopic",new OrderPlacedEvent(order.getOrderNumber()));
            return "order placed successfully";
        }
        else{
            throw new IllegalArgumentException("Product is not in stock,please try again later");

        }



    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
