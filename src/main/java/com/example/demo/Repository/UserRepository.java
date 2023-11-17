package com.example.demo.Repository;

import com.example.demo.Modal.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User ,String> {
    User findFirstByEmail(String email);


}