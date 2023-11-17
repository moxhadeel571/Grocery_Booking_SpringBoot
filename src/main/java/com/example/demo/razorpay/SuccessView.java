package com.example.demo.razorpay;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.View;
import java.util.Map;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class SuccessView implements View {

  private String razorpayPaymentID;
  private String razorpayOrderId;
  private String message;

  public SuccessView(String razorpayPaymentID, String razorpayOrderId, String message) {
    this.razorpayPaymentID = razorpayPaymentID;
    this.razorpayOrderId = razorpayOrderId;
    this.message = message;
  }

  public String getRazorpayPaymentID() {
    return razorpayPaymentID;
  }

  public String getMessage() {
    return message;
  }

  public String getRazorpayOrderId() {
    return razorpayOrderId;
  }

  @Override
  public String getContentType() {
    return View.super.getContentType();
  }

  @Override
  public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
    response.sendRedirect("/users/payment/success");
  }

}