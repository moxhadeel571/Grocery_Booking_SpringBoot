package com.example.demo.Modal;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Document(collection="TotalOrder")
public class UserInfo  {
    @MongoId
    private ObjectId id;
    private String firstName;
    private String lastName;
    private String Username;
    private String email;
    private String phone;
    private String addressLine1;
    private String addressLine2;
    private String country;
    private String city;
    private String state;
    private String zipcode;
    @DBRef
    private List<OrderItem> orderItem;



}
