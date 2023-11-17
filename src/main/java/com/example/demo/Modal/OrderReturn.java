package com.example.demo.Modal;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Document(collection = "order_returns")
public class OrderReturn extends OrderImage {
    @MongoId
    private String id;
    private List<String> productNames;
    private String name;
    private String email;
    private List<String> product;
    private String productId;
    private int quantity;
    private String complaintMessage;
    private String reason;
    private String status;
    private List<OrderImage> Orderimages;
    private Double TotalAmount;


    // Constructors, getters, setters, and other methods
}
