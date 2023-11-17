package com.example.demo.Implementation;

import com.example.demo.Modal.*;
import com.example.demo.Repository.*;
import com.example.demo.Service.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserInfoServiceImpl implements UserInfoService {
  private UserInfoRepository userInfoRepository;
  private CartService cartService;
@Autowired
    public UserInfoServiceImpl(UserInfoRepository userInfoRepository, CartService cartService) {
        this.userInfoRepository = userInfoRepository;
    this.cartService = cartService;
}

    @Override
    public void saveCheckout(ObjectId id, UserInfo userInfo,OrderItem orderItem) {

// Set user information from checkoutUser to userInfo
        userInfo.setFirstName(userInfo.getFirstName());
        userInfo.setLastName(userInfo.getLastName());
        userInfo.setUsername(userInfo.getUsername());
        userInfo.setEmail(userInfo.getEmail());
        userInfo.setPhone(userInfo.getPhone());
        userInfo.setAddressLine1(userInfo.getAddressLine1());
        userInfo.setAddressLine2(userInfo.getAddressLine2());
        userInfo.setCountry(userInfo.getCountry());
        userInfo.setCity(userInfo.getCity());
        userInfo.setState(userInfo.getState());
        userInfo.setZipcode(userInfo.getZipcode());
        List<OrderItem> order=  cartService.getCartItems();


        userInfo.setOrderItem(order);
// Save the UserInfo object to the database
        userInfoRepository.save(userInfo);


    }



}
