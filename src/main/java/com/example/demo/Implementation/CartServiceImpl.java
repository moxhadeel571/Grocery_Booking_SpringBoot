package com.example.demo.Implementation;

import com.example.demo.Modal.Coupon;
import com.example.demo.Modal.OrderItem;
import com.example.demo.Modal.Products;
import com.example.demo.Repository.OrderItemRepository;
import com.example.demo.Repository.ProductsRepository;
import com.example.demo.Repository.PromocodeRepo;
import com.example.demo.Service.CartService;
import com.example.demo.Service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Component
public class CartServiceImpl implements CartService {
    private CouponService couponService;
    private PromocodeRepo promocodeRepo;
    private OrderItemRepository orderItemRepository;
private ProductsRepository productsRepository;
    @Autowired
private CartService cartService;
    private List<OrderItem> cartItems = new ArrayList<>();
@Autowired
    public CartServiceImpl(CouponService couponService, PromocodeRepo promocodeRepo, OrderItemRepository orderItemRepository, ProductsRepository productsRepository) {
    this.couponService = couponService;
    this.promocodeRepo = promocodeRepo;
    this.orderItemRepository = orderItemRepository;
    this.productsRepository = productsRepository;
}

    @Override
    public OrderItem addItemToCart(String productId) {
        // Fetch the current user's cart items
        List<OrderItem> cartItems = cartService.getCartItems();

        // Find the product by its ID
        Products product = productsRepository.findById(productId).orElse(null);

        if (product != null) {
            // Check if the product is already in the cart
            OrderItem existingCartItem = findCartItemByProduct(cartItems, product);
            double totalAmount = cartItems.stream()
                    .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                    .sum();

            List<String> productNames = new ArrayList<>();
            List<OrderItem> cartItem = cartService.getCartItems();
            for (OrderItem item : cartItem) {
                Products productitem=new Products();
                productitem.setTitle(item.getProduct().getTitle());
                productitem.setPrice(item.getProduct().getPrice());

                productNames.add(String.valueOf(productitem));
            }

            if (existingCartItem != null) {
                existingCartItem.setProductName(productNames);
                existingCartItem.setTotalPrice(totalAmount);
                existingCartItem.setQuantity(existingCartItem.getQuantity() + 1);
                return orderItemRepository.save(existingCartItem);
            } else {
                // Create a new cart item with quantity 1
                OrderItem newItem = new OrderItem(product, 1);
                cartItems.add(newItem);
                return orderItemRepository.save(newItem);
            }
        }
        return null; // Product not found
    }

    // Helper method to find a cart item by product
    private OrderItem findCartItemByProduct(List<OrderItem> cartItems, Products product) {
        for (OrderItem item : cartItems) {
            if (item.getProduct().getId().equals(product.getId())) {
                return item;
            }
        }
        return null; // Cart item not found
    }


    // Add a method to clear the cart after a purchase
    public void clearCart() {
        cartItems.clear();
    }


    private OrderItem findCartItemByProduct(Products product) {
        return cartItems.stream()
                .filter(cartItem -> cartItem.getProduct().equals(product))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void removeItemFromCart(String productId) {
        cartItems.removeIf(cartItem -> cartItem.getProduct().getId().equals(productId));
        System.out.println("Item removed from cart: " + productId);
    }



    @Override
public List<OrderItem> findCartItemByProductId(String productId) {
        return (List<OrderItem>) cartItems.stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
    }




    @Override
    public List<OrderItem> getCartItems() {
        return orderItemRepository.findAll();
    }

    @Override
    public double getTotalCost() {
        return cartItems.stream()
                .mapToDouble(cartItem -> cartItem.getProduct().getPrice() * cartItem.getQuantity())
                .sum();
    }
    @Override
    public void checkout(String couponCode) {
        // Calculate total cost
        double totalCost = getTotalCost();

        // Create an OrderItem for each item in the cart
        for (OrderItem cartItem : cartItems) {
            // Save the OrderItem to your OrderItemRepository
            orderItemRepository.save(cartItem);
        }

        // Check if a valid coupon code is provided
        if (isValidCouponCode(couponCode)) {
            // Apply the coupon logic and save the Coupon entity
            Coupon coupon = couponService.getCouponByCouponCode(couponCode);
            // Save the Coupon to your CouponRepository
            promocodeRepo.save(coupon);
        }

        // Clear the cart after successful checkout
        cartItems.clear();
    }

    private boolean isValidCouponCode(String couponCode) {
        // Retrieve the coupon from your CouponRepository or CouponService
        Coupon coupon = couponService.getCouponByCouponCode(couponCode);
        // Check if the coupon exists and is active
        if (coupon != null ) {
            return true;
        }
        return false;
    }




}