package com.e.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckoutRequest {

    private String shippingAddress;
    private String paymentMethod;
    private String note;


}
