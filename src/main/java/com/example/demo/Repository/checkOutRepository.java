package com.example.demo.Repository;

import com.example.demo.Modal.UserInfo;
import com.example.demo.Modal.checkOut;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface checkOutRepository extends MongoRepository<checkOut, ObjectId> {
    @Query("{ 'user' : ?0 }")
    checkOut findByUser(UserInfo user);
    @Query("{ '_id' : ?0 }")
    Optional<Object> findById(String id);
}
