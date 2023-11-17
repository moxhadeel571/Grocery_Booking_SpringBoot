package com.example.demo.Service;

import com.example.demo.Modal.Order;
import com.example.demo.Modal.OrderItem;
import com.example.demo.Modal.Products;
import com.example.demo.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
@Component
public interface OrderService {
    void addItemToCart(Products product);
    void removeItemFromCart(Products product);
    List<OrderItem> getCartItems();
    double getTotalCost();

    // You can add more methods like getOrderHistoryForUser, cancelOrder, etc.
}
