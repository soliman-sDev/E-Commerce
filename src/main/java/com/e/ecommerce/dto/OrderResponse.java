package com.e.ecommerce.dto;

import com.e.ecommerce.entity.OrderStatus;
import com.e.ecommerce.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponse {

    private Long orderId;
    private BigDecimal total;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderItemResponse> items;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class OrderItemResponse {
        private Long productId;
        private String name;
        private int quantity;
        private BigDecimal price;
    }

}
