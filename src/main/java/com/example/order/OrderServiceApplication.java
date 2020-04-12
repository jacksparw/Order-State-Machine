package com.example.order;

import com.example.order.config.OrderEvents;
import com.example.order.config.OrderStates;
import com.example.order.model.Order;
import com.example.order.service.OrderService;
import lombok.extern.java.Log;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@SpringBootApplication
public class OrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}
}

@Log
@Component
class Runner implements ApplicationRunner {

	private final OrderService orderService;

	Runner(OrderService orderService) {
		this.orderService = orderService;
	}

	@Override
	public void run(ApplicationArguments args) {

		Order order = this.orderService.create(new Date());

		StateMachine<OrderStates, OrderEvents> paymentStateMachine = orderService.pay(order.getId(), UUID.randomUUID().toString());
		log.info("after calling pay(): " + paymentStateMachine.getState().getId().name());
		log.info("order: " + orderService.byId(order.getId()));

		StateMachine<OrderStates, OrderEvents> fulfilledStateMachine = orderService.fulfill(order.getId());
		log.info("after calling fulfill(): " + fulfilledStateMachine.getState().getId().name());
		log.info("order: " + orderService.byId(order.getId()));
	}
}