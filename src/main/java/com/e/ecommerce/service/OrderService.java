package com.e.ecommerce.service;

import com.e.ecommerce.dto.OrderResponse;
import com.e.ecommerce.dto.CheckoutRequest;
import com.e.ecommerce.entity.*;
import com.e.ecommerce.repository.CartItemRepo;
import com.e.ecommerce.repository.CartRepo;
import com.e.ecommerce.repository.OrderRepo;
import com.e.ecommerce.repository.ProductRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private CartRepo cartRepo;
    @Autowired
    private CartItemRepo cartItemRepo;
    @Autowired
    private ProductRepo productRepo;

    public OrderResponse checkout(Long userId, CheckoutRequest request) {
        Cart cart = cartRepo.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(cart.getUser());
        order.setTotal(cart.getTotal());
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        List<OrderItem> orderItems = cart.getItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            if (product.getStock() < cartItem.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for " + product.getName());
            }

            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepo.save(product);

            return new OrderItem(null, order, product, cartItem.getQuantity(), cartItem.getPrice());
        }).toList();

        order.setItems(orderItems);
        orderRepo.save(order);

        cart.getItems().clear();
        cart.setTotal(java.math.BigDecimal.ZERO);
        cartRepo.save(cart);

        return mapToResponse(order);
    }

    public List<OrderResponse> getUserOrders(Long userId) {
        return orderRepo.findByUserId(userId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    public OrderResponse updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepo.findById(orderId).orElseThrow(() ->
                new EntityNotFoundException("Order not found"));
        order.setStatus(status);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepo.save(order);
        return mapToResponse(order);
    }

    public OrderResponse getOrderById(Long orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        return mapToResponse(order);
    }




    private OrderResponse mapToResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getId());
        response.setStatus(order.getStatus());
        response.setTotal(order.getTotal());
        response.setCreatedAt(order.getCreatedAt());

        response.setItems(order.getItems().stream().map(item -> {
            OrderResponse.OrderItemResponse resp = new OrderResponse.OrderItemResponse();
            resp.setProductId(item.getProduct().getId());
            resp.setName(item.getProduct().getName());
            resp.setQuantity(item.getQuantity());
            resp.setPrice(item.getPrice());
            return resp;
        }).toList());

        return response;
    }
}
