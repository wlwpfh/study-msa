package org.example.orderservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.orderservice.dto.OrderDto;
import org.example.orderservice.jpa.OrderEntity;
import org.example.orderservice.message.KafkaProducer;
import org.example.orderservice.message.OrderProducer;
import org.example.orderservice.service.OrderService;
import org.example.orderservice.vo.RequestOrder;
import org.example.orderservice.vo.ResponseOrder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.modelmapper.convention.MatchingStrategies.STRICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequestMapping("/order-service")
public class OrderController {
    private Environment env;
    private OrderService orderService;
    private KafkaProducer kafkaProducer;
    private OrderProducer orderProducer;

    @Autowired
    public OrderController(Environment env, OrderService orderService, KafkaProducer kafkaProducer, OrderProducer orderProducer) {
        this.env = env;
        this.orderService = orderService;
        this.kafkaProducer = kafkaProducer;
        this.orderProducer = orderProducer;
    }

    @GetMapping("/health-check")
    public String status() {
        return String.format("it's working in order service on local port %s (server port %s)"
        , env.getProperty("local.server.port"), env.getProperty("server.port"));
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrder> createOrder(@PathVariable("userId") String userId,
                                                     @RequestBody RequestOrder orderDetails) {
        log.info("before add order data");

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(STRICT);

        OrderDto orderDto = mapper.map(orderDetails, OrderDto.class);
        orderDto.setUserId(userId);

        /* jpa */
        OrderDto createdOrder = orderService.createOrder(orderDto);
        ResponseOrder responseOrder = mapper.map(orderDto, ResponseOrder.class);

        /* kafka */
        //orderDto.setOrderId(UUID.randomUUID().toString());
        //orderDto.setTotalPrice(orderDetails.getQty() * orderDetails.getUnitPrice());
        //ResponseOrder responseOrder = mapper.map(orderDto, ResponseOrder.class);

        /* send this order */
        //kafkaProducer.send("example-catalog-topic", orderDto);
        //orderProducer.send("orders", orderDto);

        log.info("after add order data");

        return ResponseEntity.status(CREATED).body(responseOrder);
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getOrder(@PathVariable("userId") String userId) throws Exception {
        log.info("before get order data");

        Iterable<OrderEntity> orderList = orderService.getOrdersByUserId(userId);

        List<ResponseOrder> response = new ArrayList<>();
        orderList.forEach(v -> {
            response.add(new ModelMapper().map(v, ResponseOrder.class));
        });

        log.info("after get order data");

        return ResponseEntity.status(OK).body(response);
    }
}
