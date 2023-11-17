package com.example.demo.Implementation;


import com.example.demo.Modal.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class UserServiceImpl implements UserService {
      @Autowired
    private UserRepository userRepository;
      @Autowired
      private BCryptPasswordEncoder bcryptPasswordEncoder;



    @Override
    public User saveUser(User User) {

        String passsword=   bcryptPasswordEncoder.encode(User.getPassword());
        User.setPassword(passsword);
        User newUser= userRepository.save(User);
        return newUser;
    }

    @Override
    public void removeSuccessMessage() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        if (attr != null) {
            HttpSession session = attr.getRequest().getSession();
            if (session != null) {
                session.removeAttribute("msg");
            }
        }
    }





}
