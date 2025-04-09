package com.e.ecommerce.service;

import com.e.ecommerce.dto.AddToCartRequest;
import com.e.ecommerce.dto.CartResponse;
import com.e.ecommerce.dto.UpdateCartRequest;
import com.e.ecommerce.entity.Cart;
import com.e.ecommerce.entity.CartItem;
import com.e.ecommerce.entity.Product;
import com.e.ecommerce.entity.User;
import com.e.ecommerce.repository.CartItemRepo;
import com.e.ecommerce.repository.CartRepo;
import com.e.ecommerce.repository.ProductRepo;
import com.e.ecommerce.repository.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private CartItemRepo cartItemRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private UserRepo userRepo;

    public CartResponse addToCart(Long userId, AddToCartRequest request) {
        Cart cart = cartRepo.findByUserId(userId)
                .orElseGet(() -> createCartForUser(userId));

        Product product = productRepo.findById(request.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        if (product.getStock() < request.getQuantity()) {
            throw new IllegalArgumentException("Insufficient stock for product");
        }

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            item.setPrice(product.getPrice());
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(request.getQuantity());
            newItem.setPrice(product.getPrice());
            cart.getItems().add(newItem);
        }

        recalculateCartTotal(cart);
        cartRepo.save(cart);

        return toResponse(cart);
    }

    private Cart createCartForUser(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setTotal(BigDecimal.ZERO);
        return cartRepo.save(cart);
    }

    public CartResponse updateCartItemQuantity(Long userId, UpdateCartRequest request) {
        Cart cart = cartRepo.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(request.getProductId()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Item not found in cart"));

        if (request.getQuantity() <= 0) {
            cart.getItems().remove(item);
            cartItemRepo.delete(item);
        } else {
            Product product = item.getProduct();
            if (product.getStock() < request.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock");
            }
            item.setQuantity(request.getQuantity());
        }

        recalculateCartTotal(cart);
        cartRepo.save(cart);

        return toResponse(cart);
    }

    public CartResponse getCartByUserId(Long userId) {
        Cart cart = cartRepo.findByUserId(userId)
                .orElseGet(() -> createCartForUser(userId));
        return toResponse(cart);
    }

    private void recalculateCartTotal(Cart cart) {
        BigDecimal total = cart.getItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotal(total);
    }

    public CartResponse removeFromCart(Long userId, Long productId) {
        Cart cart = cartRepo.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Item not found in cart"));

        cart.getItems().remove(item);
        cartItemRepo.delete(item);

        recalculateCartTotal(cart);
        cartRepo.save(cart);

        return toResponse(cart);
    }

    public void clearCart(Long userId) {
        Cart cart = cartRepo.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));
        cart.getItems().clear();
        cart.setTotal(BigDecimal.ZERO);
        cartRepo.save(cart);
    }

    private CartResponse toResponse(Cart cart) {
        CartResponse response = new CartResponse();
        response.setUserId(cart.getUser().getId());
        response.setTotal(cart.getTotal());

        response.setItems(cart.getItems().stream().map(item -> {
            CartResponse.CartItemResponse itemResp = new CartResponse.CartItemResponse();
            itemResp.setProductId(item.getProduct().getId());
            itemResp.setName(item.getProduct().getName());
            itemResp.setPrice(item.getPrice());
            itemResp.setQuantity(item.getQuantity());
            return itemResp;
        }).toList());

        return response;
    }
    
}
