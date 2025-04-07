package com.e.ecommerce.dto;


import com.e.ecommerce.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private int statusCode;
    private String error;
    private String message;
    private String token;
    private String expirationTime;
    private String username;
    private String email;
    private String role;
    private String password;
    private User user;
}
