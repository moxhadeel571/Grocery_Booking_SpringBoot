package com.example.demo.Implementation;


import com.example.demo.Modal.*;
import com.example.demo.Repository.*;
import com.example.demo.Service.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class EmailServiceImpl implements EmailService {
    private  JavaMailSender javaMailSender;
@Autowired
private checkOutRepository checkOutRepository;

private UserInfoRepository userInfoRepository;
@Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender, UserInfoRepository userInfoRepository) {
        this.javaMailSender = javaMailSender;
    this.userInfoRepository = userInfoRepository;
}


    @Override
        public void sendEmail(Email email) throws MessagingException {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            try {
                String getSubject = "🍔🥗 Savor the Latest: Exclusive Food Delivery App Updates Await You!";
                String getBody = "Discover Delights, Delivered to You!\n" +
                        "\n" +
                        "Hungry for more? Stay ahead of the flavor curve with our food delivery app updates. From sizzling deals to new menu must-haves, be the first to know about all things delicious. Join now and let your cravings lead the way!";
                String displayName = "Savor Delights"; // Set the desired display name
                String fromEmail = "noreply@example.com"; // Set your actual email address

                helper.setFrom(new InternetAddress(fromEmail, displayName));
                helper.setTo(email.getTo());
                helper.setSubject(getSubject);
                helper.setText(getBody, true);

                javaMailSender.send(mimeMessage);

            } catch (Exception e) {
                // Handle other exceptions
                // For example, you may log the error or display a custom error message
                e.printStackTrace();
            }
        }

    @Override
    public void order_email(Email email, ObjectId id) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        try {
            // Obtain the necessary data for email and ID
            String toEmail = email.getTo();
            UserInfo userInfo = userInfoRepository.findById(id).orElse(null);

            if (userInfo != null) {
                ObjectId orderId = userInfo.getId();
                List<OrderItem> orderItems = userInfo.getOrderItem();

                String getSubject = "Order Confirmation - Your Order Id " + orderId;
                String displayName = "Your Company Name";
                String fromEmail = "noreply@example.com";

                helper.setFrom(new InternetAddress(fromEmail, displayName));
                helper.setTo(toEmail);
                helper.setSubject(getSubject);

                // Create the HTML content for the email
                String htmlContent = "<html><head>" +
                        "<link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\">" +
                        "<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css\">" +
                        "</head><body style=\"font-family: 'Arial', sans-serif;\">"
                        + "<div class=\"container\">"
                        + "<div class=\"jumbotron\">"
                        + "<h1 class=\"display-4\">Order Confirmation</h1>"
                        + "<p class=\"lead\">Thank you for placing your order with us. Your order details are as follows:</p>"
                        + "<hr class=\"my-4\">"
                        + "<p><strong>Order ID:</strong> " + orderId + "</p>"
                        + "<p><strong>Order Details:</strong></p>"
                        + "<ul class=\"list-group\">";

                // Loop through the product names and add them to the email
                for (OrderItem orderItem : orderItems) {
                    htmlContent += "<li class=\"list-group-item\">" +
                            "<i class=\"fas fa-shopping-cart\"></i>&nbsp;&nbsp;" +
                            orderItem.getProductName() + " - $" + orderItem.getPrice() +
                            "</li>";
                }

                htmlContent += "</ul>"
                        + "<p class=\"lead mt-4\"><strong>Total Amount:</strong> $" + getTotalAmount(orderItems) + "</p>"
                        + "<p class=\"lead\">For order tracking details, please visit our <a href=\"https://yourwebsite.com/order-tracking\">Order Tracking</a> page.</p>"
                        + "<p class=\"lead\">Delivery Address: " + userInfo.getAddressLine1() + " " + userInfo.getAddressLine2() + "</p>"
                        + "</div></div></body></html>";

                // Set the HTML content of the email
                helper.setText(htmlContent, true);

                javaMailSender.send(mimeMessage);
            } else {
                // Handle the case where userInfo is null
                System.out.println("User information not found for order ID: " + id);
            }
        } catch (MessagingException e) {
            // Handle messaging exception
            throw e;
        } catch (Exception e) {
            // Handle other exceptions
            // For example, you may log the error or display a custom error message
            e.printStackTrace();
        }
    }


    private Double getTotalAmount(List<OrderItem> orderItems) {
        return orderItems.stream()
                .mapToDouble(item -> item.getPrice())
                .sum();
    }


    // A utility method to format the date as a string
  }







