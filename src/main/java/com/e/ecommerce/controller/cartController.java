package com.e.ecommerce.controller;

import com.e.ecommerce.dto.AddToCartRequest;
import com.e.ecommerce.dto.CartResponse;
import com.e.ecommerce.dto.UpdateCartRequest;
import com.e.ecommerce.entity.User;
import com.e.ecommerce.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Cart", description = "Manage Cart")
@RestController
@RequestMapping("/api/cart")
public class cartController {

    @Autowired
    private CartService cartService;

    @Operation(summary = "Add to Cart")
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@AuthenticationPrincipal User user , @RequestBody AddToCartRequest request){
        return ResponseEntity.ok(cartService.addToCart(user.getId(),request));
    };

    @Operation(summary = "Update Cart")
    @PutMapping("/update")
    public ResponseEntity<?> updateQuantity(@AuthenticationPrincipal User user ,@RequestBody UpdateCartRequest request){
        return ResponseEntity.ok(cartService.updateCartItemQuantity(user.getId(),request));
    };

    @Operation(summary = "Remove from Cart")
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<?> removeFromCart(@AuthenticationPrincipal User user ,@PathVariable Long productId){
        return ResponseEntity.ok(cartService.removeFromCart(user.getId(),productId));
    };

    @Operation(summary = "Get Cart by user Id")
    @GetMapping("/{userId}")
    public ResponseEntity<CartResponse> getCartByUserId(@AuthenticationPrincipal User user ){
        return ResponseEntity.ok(cartService.getCartByUserId(user.getId()));
    };

    @Operation(summary = "Clear Cart")
    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<?> clearCart(@AuthenticationPrincipal User user) {
        cartService.clearCart(user.getId());
        return ResponseEntity.ok("Cart Cleared Successfully");
    }
}
