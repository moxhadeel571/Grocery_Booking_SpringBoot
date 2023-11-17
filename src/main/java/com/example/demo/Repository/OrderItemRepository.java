package com.example.demo.Repository;

import com.example.demo.Modal.OrderItem;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderItemRepository extends MongoRepository<OrderItem, ObjectId> {
    @Query("{ '_id' : ?0 }")
    Optional<OrderItem> findByOrder(ObjectId id);
}
