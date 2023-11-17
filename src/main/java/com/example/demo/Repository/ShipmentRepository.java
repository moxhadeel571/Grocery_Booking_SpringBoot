package com.example.demo.Repository;

import com.example.demo.Modal.Shipment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShipmentRepository extends MongoRepository<Shipment, String> {
    @Query("{ '_id.$oid' : ?0 }")
    Shipment findAllByShipmentId(String id);

    @Query("{ '_id' : ?0 }")
    List<Shipment> getShipmentById(String id);

    @Query("{'id': ?0}")
    Optional<Shipment> findShipmentById(String id);
    // Custom query methods can be added here
}
