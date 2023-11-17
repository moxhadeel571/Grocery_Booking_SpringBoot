package com.example.demo.Repository;

import com.example.demo.Modal.User;
import com.example.demo.Modal.UserInfo;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;



@Repository
public interface UserInfoRepository extends MongoRepository<UserInfo,ObjectId> {

    @Query("{ 'email' : ?0 }")
    User findByEmail(String email);
}
