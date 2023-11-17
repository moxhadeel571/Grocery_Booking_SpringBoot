package com.example.demo.Service;

import com.example.demo.Modal.Shipment;
import com.example.demo.Repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

public interface ShipmentService {
    Shipment createShipment(Shipment shipment);
//    Shipment updateShipment(String id, Shipment shipment);

    Shipment updateShipmentStatus(String id, String NewStatus);

    List<Shipment> getAllShipments();
    Shipment getShipmentById(String id);
    void deleteShipment(String id);
}
