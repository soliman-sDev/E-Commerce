package com.e.ecommerce.controller;

import com.e.ecommerce.dto.CheckoutRequest;
import com.e.ecommerce.dto.OrderResponse;
import com.e.ecommerce.dto.UpdateOrderStatusRequest;
import com.e.ecommerce.entity.User;
import com.e.ecommerce.repository.OrderRepo;
import com.e.ecommerce.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Orders", description = "Manage Orders")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepo orderRepo;

    @Operation(summary = "Checkout")
    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@AuthenticationPrincipal User user, @RequestBody CheckoutRequest request) {
        return ResponseEntity.ok(orderService.checkout(user.getId(), request));
    }

    @Operation(summary = "get User Orders")
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getUserOrders(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(orderService.getUserOrders(user.getId()));
    }

    @Operation(summary = "update Order Status")
    @PutMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long orderId, @RequestBody UpdateOrderStatusRequest request) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, request.getStatus()));
    }

    @Operation(summary = "get all orders form all users")
    @GetMapping("/all")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = orderRepo.findAll().stream()
                .map(order -> orderService.getOrderById(order.getId()))
                .toList();
        return ResponseEntity.ok(orders);
    }
}
