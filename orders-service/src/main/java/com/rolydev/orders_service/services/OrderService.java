package com.rolydev.orders_service.services;

import com.rolydev.orders_service.events.OrderEvent;
import com.rolydev.orders_service.model.dtos.*;
import com.rolydev.orders_service.model.entities.Order;
import com.rolydev.orders_service.model.entities.OrderItems;
import com.rolydev.orders_service.model.enums.OrderStatus;
import com.rolydev.orders_service.repositories.OrderRepository;
import com.rolydev.orders_service.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final KafkaTemplate<String,String> kafkaTemplate;

    public OrderResponse placeOrder(OrderRequest orderRequest) {

        //check for inventory
        BaseResponse result = this.webClientBuilder.build()
                .post()
                .uri("lb://inventory-service/api/inventory/in-stock")
                .bodyValue(orderRequest.getOrderItems())
                .retrieve()
                .bodyToMono(BaseResponse.class)
                .block();

        if (result != null && !result.hasErrors()){
            Order order = new Order();
            order.setOrderNumber(UUID.randomUUID().toString());
            order.setOrderItems(orderRequest.getOrderItems().stream()
                    .map(orderItemsRequest -> mapOrderItemRequestToOrderItem(orderItemsRequest, order))
                    .toList());
            var savedOrder = this.orderRepository.save(order);
            //TODO: Send message to order topic
            this.kafkaTemplate.send("orders-topic", JsonUtils.toJson(
                    new OrderEvent(savedOrder.getOrderNumber(), savedOrder.getOrderItems().size(), OrderStatus.PLACED)
            ));

            return mapOrderToOrderResponse(savedOrder);
        } else {
            throw new IllegalArgumentException("Some of the products are not in stock");
        }
    }

    public List<OrderResponse> getAllOrders(){
        List<Order> orders = this.orderRepository.findAll();
        return orders.stream().map(this::mapOrderToOrderResponse).toList();
    }

    private OrderResponse mapOrderToOrderResponse(Order order) {
        return new OrderResponse(order.getId(),
                order.getOrderNumber(),
                order.getOrderItems().stream().map(this::mapOrderToOrderItemRequest).toList());
    }

    private OrderItemsResponse mapOrderToOrderItemRequest(OrderItems orderItems) {
        return new OrderItemsResponse(orderItems.getId(),
                orderItems.getSku(),
                orderItems.getPrice(),
                orderItems.getQuantity());
    }

    private OrderItems mapOrderItemRequestToOrderItem(OrderItemsRequest orderItemsRequest, Order order) {
        return OrderItems.builder()
                .id(orderItemsRequest.getId())
                .sku(orderItemsRequest.getSku())
                .price(orderItemsRequest.getPrice())
                .quantity(orderItemsRequest.getQuantity())
                .order(order)
                .build();
    }

}
