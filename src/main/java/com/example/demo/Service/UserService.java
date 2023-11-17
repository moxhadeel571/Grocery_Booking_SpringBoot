package com.example.demo.Service;

import com.example.demo.Modal.User;
import com.example.demo.Modal.UserInfo;
import org.springframework.stereotype.Service;


@Service
public interface UserService {



 User saveUser(User User);

 public void removeSuccessMessage();

}