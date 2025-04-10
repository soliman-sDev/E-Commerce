package com.e.ecommerce.controller;

import com.e.ecommerce.dto.CheckoutRequest;
import com.e.ecommerce.dto.OrderResponse;
import com.e.ecommerce.dto.UpdateOrderStatusRequest;
import com.e.ecommerce.entity.User;
import com.e.ecommerce.repository.OrderRepo;
import com.e.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepo orderRepo;

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@AuthenticationPrincipal User user, @RequestBody CheckoutRequest request) {
        return ResponseEntity.ok(orderService.checkout(user.getId(), request));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getUserOrders(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(orderService.getUserOrders(user.getId()));
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long orderId, @RequestBody UpdateOrderStatusRequest request) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, request.getStatus()));
    }

    @GetMapping("/all")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = orderRepo.findAll().stream()
                .map(order -> orderService.getOrderById(order.getId()))
                .toList();
        return ResponseEntity.ok(orders);
    }
}
