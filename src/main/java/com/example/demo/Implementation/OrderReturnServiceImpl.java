package com.example.demo.Implementation;

import com.example.demo.Modal.*;
import com.example.demo.Repository.*;
import com.example.demo.Service.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class OrderReturnServiceImpl implements OrderReturnService {
    private final CartService cartService;

    private final OrderReturnRepository orderReturnRepository;

    @Autowired
    public OrderReturnServiceImpl(CartService cartService, OrderReturnRepository orderReturnRepository) {
        this.cartService = cartService;
        this.orderReturnRepository = orderReturnRepository;
    }

    @Override
    public List<OrderReturn> getAllOrderReturns() {
        return orderReturnRepository.findAll();
    }

    @Override
    public OrderReturn getOrderReturnById(String id) {
        checkOut check=new checkOut();
        List<String> ProductNames= check.getProductNames();
        List<OrderItem> cartItems = cartService.getCartItems();
        int totalItems = cartItems.stream().mapToInt(OrderItem::getQuantity).sum();
        double totalAmount = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
        return orderReturnRepository.findById(id).orElse(null);
    }

    @Override
    public OrderReturn createOrderReturn(MultipartFile[] files, OrderReturn orderReturn) throws IOException {

        List<OrderImage> images = new ArrayList<>();
        List<String> productNames = new ArrayList<>();
        List<OrderItem> cartItem = cartService.getCartItems();
        for (OrderItem item : cartItem) {
            productNames.add(item.getProduct().getTitle());
        }

        orderReturn.setProductNames(productNames);
        for (MultipartFile file : files) {
            OrderImage image = new OrderImage();
            image.setFileName(file.getOriginalFilename());
            image.setContentType(file.getContentType());
            image.setFileData(file.getBytes());
            images.add(image);
        }


        checkOut check=new checkOut();

        ObjectId productID= check.getId();

        List<String> ProductNames= check.getProductNames();
        orderReturn.setProductNames(ProductNames);
        List<OrderItem> cartItems = cartService.getCartItems();
        int totalItems = cartItems.stream().mapToInt(OrderItem::getQuantity).sum();
        double totalAmount = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
        OrderReturn orderReturns = new OrderReturn();
        orderReturns.setId(orderReturn.getId());
        orderReturns.setOrderimages(images);
        orderReturns.setProductId(String.valueOf(productID));
        orderReturns.setProduct( ProductNames);
        orderReturns.setStatus(orderReturn.getStatus());
        orderReturns.setQuantity(totalItems);
        orderReturns.setReason(orderReturn.getReason());
        orderReturns.setTotalAmount(totalAmount);
        orderReturns.setComplaintMessage(orderReturn.getComplaintMessage());
        orderReturns.setName(orderReturn.getName());
        orderReturns.setEmail(orderReturn.getEmail());
        return orderReturnRepository.save(orderReturns);
    }

    @Override
    public OrderReturn updateOrderReturn(String id, OrderReturn updatedOrderReturn) {
        // Check if the order return with the given id exists
        if (orderReturnRepository.existsById(id)) {
            updatedOrderReturn.setId(id);
            return orderReturnRepository.save(updatedOrderReturn);
        }
        return null; // Return null if the order return does not exist
    }

    @Override
    public OrderReturn approveOrderReturn(String id) {
        // Check if the order return with the given id exists
        Optional<OrderReturn> orderReturnOptional = orderReturnRepository.findByReturnId(id);
        if (orderReturnOptional.isPresent()) {
            OrderReturn existingOrderReturn = orderReturnOptional.get();
            // Update the status to "Approved" or perform any other necessary actions
            existingOrderReturn.setStatus("Approved");
            // Save the updated order return
            return orderReturnRepository.save(existingOrderReturn);
        }
        return null; // Return null if the order return does not exist
    }

    @Override
    public OrderReturn rejectOrderReturn(String id) {
        // Check if the order return with the given id exists
        Optional<OrderReturn> orderReturnOptional = orderReturnRepository.findByReturnId(id);
        if (orderReturnOptional.isPresent()) {
            OrderReturn existingOrderReturn = orderReturnOptional.get();
            // Update the status to "Rejected" or perform any other necessary actions
            existingOrderReturn.setStatus("Rejected");
            // Save the updated order return
            return orderReturnRepository.save(existingOrderReturn);
        }
        return null; // Return null if the order return does not exist
    }


    @Override
    public void deleteOrderReturn(String id) {
        orderReturnRepository.deleteById(id);
    }
}
