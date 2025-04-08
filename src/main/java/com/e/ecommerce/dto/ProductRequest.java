package com.e.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductRequest {
    @NotBlank
    private String name;

    private String description;

    @NotBlank
    @DecimalMin(value = "0.0" , inclusive = false)
    private BigDecimal price;

    @NotNull
    @Min(0)
    private Integer stock;

    private String category;

    private String imageUrl;

}
