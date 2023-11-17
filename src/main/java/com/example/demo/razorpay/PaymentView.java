package com.example.demo.razorpay;

import io.dropwizard.views.View;

public class PaymentView extends View {

  private int amount;
  private String razorpayOrderId;

  public PaymentView(int amount, String razorpayOrderId) {
    super("/check");
    this.amount = amount;
    this.razorpayOrderId = razorpayOrderId;
  }

  public int getAmount() {
    return amount;
  }

  public String getRazorpayOrderId() {
    return razorpayOrderId;
  }
}
