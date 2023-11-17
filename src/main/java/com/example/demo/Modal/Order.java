package com.example.demo.Modal;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@ToString
@Document(collection = "Order")
public class Order extends Shipment {
    @MongoId
    private ObjectId id;

    @DBRef
    private UserInfo user;
    @DBRef

    private List<OrderItem> orderItems;

    private double totalAmount;

    private LocalDateTime orderDate;

    // Other fields and getters/setters
}
