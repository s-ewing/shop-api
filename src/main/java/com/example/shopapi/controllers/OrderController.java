package com.example.shopapi.controllers;

import com.example.shopapi.dto.OrderDTO;
import com.example.shopapi.dto.PaymentStatusRequestDTO;
import com.example.shopapi.services.OrderService;
import com.stripe.exception.StripeException;
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
    @PutMapping
    public ResponseEntity<OrderDTO> updateOrderWithPaymentStatus(@Valid @RequestBody PaymentStatusRequestDTO paymentStatusRequestDTO) throws StripeException {
        return new ResponseEntity<>(orderService.updateOrderWithPaymentStatus(paymentStatusRequestDTO.getSessionId()), HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderDTO orderDTO,
                                                @RequestHeader(name="Authorization") String token) throws StripeException {
        String jwt = token.replace("Bearer ", "");
        Long userId = Long.parseLong(jwtDecoder.decode(jwt).getSubject());
        return new ResponseEntity<>(orderService.createOrder(orderDTO, userId), HttpStatus.OK);
    }

}
