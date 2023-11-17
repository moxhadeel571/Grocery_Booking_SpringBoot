package com.example.demo.razorpay;

import org.springframework.context.annotation.Configuration;

@Configuration

public abstract class AppConfiguration extends io.dropwizard.Configuration implements Configuration {

  private String apiKey;

  private String secretKey;

  public String getApiKey() {
    return "rzp_test_BGPTgqrkVagzn6";
  }

  public String getSecretKey() {
    return "l48xX7CnwytQmBn5twVyiavo";
  }

}
