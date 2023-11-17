package com.example.demo.Repository;

import com.example.demo.Modal.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface OrderRepository extends MongoRepository<Order, String> {
}
