package com.example.demo.Service;

import com.example.demo.Modal.UserInfo;
import com.example.demo.Modal.checkOut;
import org.bson.types.ObjectId;

import java.util.List;
public interface checkOutService {

    String processOrder(ObjectId id);
    List<checkOut>  getAllCustomers();
    checkOut saveCheckout(checkOut checkOut, UserInfo userInfo) throws Exception;

    checkOut getAllCustomers(ObjectId id);

}
