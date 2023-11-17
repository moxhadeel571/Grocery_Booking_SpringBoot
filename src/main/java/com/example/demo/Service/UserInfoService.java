package com.example.demo.Service;

import com.example.demo.Modal.OrderItem;
import com.example.demo.Modal.UserInfo;
import org.bson.types.ObjectId;

import java.util.Optional;

public interface UserInfoService {


    void saveCheckout(ObjectId id, UserInfo userInfo, OrderItem orderItem);
}
