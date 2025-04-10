package com.e.ecommerce.repository;

import com.e.ecommerce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepo extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
}
