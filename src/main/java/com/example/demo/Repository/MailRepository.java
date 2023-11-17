package com.example.demo.Repository;

import com.example.demo.Modal.Email;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailRepository extends MongoRepository<Email,Long> {
}
