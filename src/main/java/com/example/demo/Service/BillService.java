//package com.example.demo.Service;
//
//import com.example.demo.Modal.checkOut;
//import org.bson.types.ObjectId;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class BillService {
//
//    private final PdfGenerationService pdfGenerationService;
//
//    @Autowired
//    public BillService(PdfGenerationService pdfGenerationService) {
//        this.pdfGenerationService = pdfGenerationService;
//    }
//
//    public byte[] generateBillPdf(checkOut bill) {
//        Map<String, Object> dataMap = new HashMap<>();
//
//        // Populate dataMap with values from the checkOut object
//        String username = bill.getUserInfo().getUsername();
//        String address = bill.getUserInfo().getAddressLine1();
//        String address2 = bill.getUserInfo().getAddressLine2();
//        Double totalAmount = bill.getTotalAmount();
//        Integer items = bill.getTotalItems();
//        ObjectId id = bill.getId();
//        Date orderdate = bill.getOrderdate();
//
//        // Populate the dataMap with the retrieved values
//        dataMap.put("username", username);
//        dataMap.put("address", address);
//        dataMap.put("address2", address2);
//        dataMap.put("totalAmount", totalAmount);
//        dataMap.put("items", items);
//        dataMap.put("id", id);
//        dataMap.put("orderdate", orderdate);
//
//        return pdfGenerationService.generatePdf(dataMap);
//    }
//
//}
