package com.example.demo.Repository;


import com.example.demo.Modal.Products;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileInfoRepository extends MongoRepository<Products, ObjectId> {
}
