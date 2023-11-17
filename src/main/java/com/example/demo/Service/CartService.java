package com.example.demo.Service;

import com.example.demo.Modal.OrderItem;

import java.util.List;

public interface CartService {
    OrderItem addItemToCart(String productId);
    void removeItemFromCart(String productId);
    void checkout(String couponCode);

    List<OrderItem>  findCartItemByProductId(String productId);

    List<OrderItem> getCartItems();
    double getTotalCost();

    // Implement methods to interact with the cart repository
}
