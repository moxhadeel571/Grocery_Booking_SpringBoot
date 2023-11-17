package com.example.demo.razorpay;
import com.example.demo.Modal.*;
import com.example.demo.Repository.*;
import com.example.demo.Service.*;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Path("/users/payment")
public class PaymentResource {
    ProductsRepository productsRepository;
    private UserInfoService sellerService;
    private RazorpayClient client;
    private int amount = 500;

    private String apiKey;
    private String secretKey;
    public PaymentResource(String apiKey, String secretKey) {
        this.apiKey = apiKey;
        this.secretKey = secretKey;
        try {
            this.client = new RazorpayClient(this.apiKey, this.secretKey);
        } catch (RazorpayException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    @GET
    @Produces(MediaType.TEXT_HTML)
    public Order getPaymentForm() throws RazorpayException {
        RazorpayClient razorpay = new RazorpayClient("[rzp_test_BGPTgqrkVagzn6]", "[l48xX7CnwytQmBn5twVyiavo]");
        Products checkOut=new Products();
        List<Products> cartItems = productsRepository.findAll();
        int totalItems = cartItems.stream().mapToInt(Products::getQuantity).sum();
        checkOut.setPrice((double) totalItems);
        double totalAmount = cartItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        JSONObject orderRequest = new JSONObject();
        Products info=new Products();
        orderRequest.put("amount",totalAmount   );
        orderRequest.put("currency","INR");
        orderRequest.put("receipt", "receipt#1");
        JSONObject notes = new JSONObject();
        notes.put("notes_key_1","Tea, Earl Grey, Hot");
        notes.put("notes_key_1","Tea, Earl Grey, Hot");
        orderRequest.put("notes",notes);

        Order order = razorpay.Orders.create(orderRequest);
        return order;
    }
    @POST
    @Path("/charge")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public String charge(MultivaluedMap<String, String> formParams,
                        Model model,
                         @RequestParam(value = "razorpayPaymentID" )String payment

    ) throws RazorpayException {

        String paymentId = formParams.getFirst("razorpay_payment_id");
        String razorpaySignature = formParams.getFirst("razorpay_signature");
        String orderId = formParams.getFirst("razorpay_order_id");

        JSONObject options = new JSONObject();

        if (StringUtils.isNotBlank(paymentId) && StringUtils.isNotBlank(razorpaySignature)
                && StringUtils.isNotBlank(orderId)) {
            try {
                options.put("razorpay_payment_id", paymentId);
                options.put("razorpay_order_id", orderId);
                options.put("razorpay_signature", razorpaySignature);
                boolean isEqual = Utils.verifyPaymentSignature(options, this.secretKey);

                if (isEqual) {
                    return "redirect:/users/payment/success";
                } else {
                    // Payment verification failed, handle accordingly
                    // For example, show an error message, log the issue, etc.

                    // Redirect to an error page or show an error message
                    return "redirect:/errorPage.html";
                }
            } catch (JSONException | RazorpayException e) {
                // Log the exception or perform any necessary actions
                e.printStackTrace(); // Log the stack trace
                model.addAttribute("errorMessage", "An error occurred during payment processing.");
                return "errorPage"; // Display a specific error page with a meaningful message
            }

        }

        // Redirect to an error page if required parameters are missing
        return "redirect:/errorPage.html";
    }



}