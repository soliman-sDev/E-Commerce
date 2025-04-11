package com.e.ecommerce.service;

import com.e.ecommerce.dto.PaymentRequest;
import com.e.ecommerce.dto.PaymentResponse;
import com.e.ecommerce.entity.Order;
import com.e.ecommerce.entity.OrderStatus;
import com.e.ecommerce.entity.Payment;
import com.e.ecommerce.entity.PaymentStatus;
import com.e.ecommerce.repository.OrderRepo;
import com.e.ecommerce.repository.PaymentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Random;


@Service
public class PaymentService {
    @Autowired
    private PaymentRepo paymentRepo;

    @Autowired
    private OrderRepo orderRepo;

    public PaymentResponse makePayment(PaymentRequest request) {
        Order order = orderRepo.findById(request.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Order is not in a payable state");
        }

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getTotal());
        payment.setMethod(request.getMethod());
        payment.setPaidAt(LocalDateTime.now());

        // Simulate payment result
        boolean paymentSuccess = new Random().nextBoolean();
        payment.setStatus(paymentSuccess ? PaymentStatus.SUCCESS : PaymentStatus.FAILED);
        paymentRepo.save(payment);

        // Update order status
        if (paymentSuccess) {
            order.setStatus(OrderStatus.PROCESSING);
        } else {
            order.setStatus(OrderStatus.CANCELLED);
        }
        order.setUpdatedAt(LocalDateTime.now());
        orderRepo.save(order);


        PaymentResponse response = new PaymentResponse();
        response.setPaymentId(payment.getId());
        response.setOrderId(order.getId());
        response.setMethod(payment.getMethod());
        response.setStatus(payment.getStatus());
        response.setPaidAt(payment.getPaidAt());

        return response;
    }



}
