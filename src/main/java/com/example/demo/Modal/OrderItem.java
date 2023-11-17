package com.example.demo.Modal;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Data
@Document(collection = "OrderItem")
public class OrderItem extends Coupon {
    @MongoId
    private String id;
    @DBRef
    private Products product;
    private List<String> productName;
    private int quantity;
    private UserInfo userInfo;
    private Coupon coupon;
    private double price;
    private double totalPrice;
    public OrderItem(Products product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }


    // Other fields and getters/setters
}