package com.example.demo.Service;

import com.example.demo.Modal.OrderReturn;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface OrderReturnService {
    List<OrderReturn> getAllOrderReturns();
    OrderReturn getOrderReturnById(String id);

    OrderReturn createOrderReturn(MultipartFile[] files, OrderReturn orderReturn) throws IOException;

    OrderReturn updateOrderReturn(String id, OrderReturn updatedOrderReturn);

    OrderReturn approveOrderReturn(String id);

    OrderReturn rejectOrderReturn(String id);

    void deleteOrderReturn(String id);
}
