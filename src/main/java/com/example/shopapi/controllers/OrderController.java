package com.example.shopapi.controllers;

import com.example.shopapi.dto.OrderDTO;
import com.example.shopapi.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "/orders")
public class OrderController {
    private final OrderService orderService;
    private final JwtDecoder jwtDecoder;

    public OrderController(OrderService orderService, JwtDecoder jwtDecoder) {
        this.orderService = orderService;
        this.jwtDecoder = jwtDecoder;
    }

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderDTO orderDTO,
                                                @RequestHeader(name="Authorization") String token) {
        String jwt = token.replace("Bearer ", "");
        Long userId = Long.parseLong(jwtDecoder.decode(jwt).getSubject());
        System.out.println(userId);
        return new ResponseEntity<>(orderService.createOrder(orderDTO, userId), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<OrderDTO> updateOrder(@Valid @RequestBody OrderDTO orderDTO) {
        return new ResponseEntity<>(orderService.updateOrder(orderDTO), HttpStatus.OK);
    }
}
