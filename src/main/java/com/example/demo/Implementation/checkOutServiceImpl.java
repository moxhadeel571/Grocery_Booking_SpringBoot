    package com.example.demo.Implementation;
    import org.springframework.stereotype.Component;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.thymeleaf.context.Context;
    import org.thymeleaf.TemplateEngine;

    import com.example.demo.Modal.*;
    import com.example.demo.Repository.PromocodeRepo;
    import com.example.demo.Repository.UserInfoRepository;
    import com.example.demo.Repository.checkOutRepository;
    import com.example.demo.Service.*;
    import org.bson.types.ObjectId;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.data.mongodb.core.MongoTemplate;
    import org.springframework.data.mongodb.core.query.Criteria;
    import org.springframework.data.mongodb.core.query.Query;
    import org.springframework.stereotype.Service;



    import java.io.ByteArrayOutputStream;
    import java.util.ArrayList;
    import java.util.Date;
    import java.util.List;

    @Component
    public class checkOutServiceImpl implements checkOutService {
        private final TemplateEngine templateEngine;
        @Autowired
        private checkOutRepository checkOutRepository;
        private ProductsService ProductsService;
        private OrderService OrderService;
        private final MongoTemplate mongoTemplate;
        private PromocodeRepo promocodeRepo;
        private CartService cartService;
        private UserInfoService userInfoService;
        private final checkOutRepository orderRepository;
        private UserInfoRepository userInfoRepository;

        private final ProductsService productsService;
        @Autowired
        public checkOutServiceImpl(TemplateEngine templateEngine, MongoTemplate mongoTemplate,
                                   PromocodeRepo promocodeRepo, CartService cartService, UserInfoService userInfoService, checkOutRepository orderRepository,
                                   UserInfoRepository userInfoRepository, ProductsService productsService) {
            this.templateEngine = templateEngine;
            this.mongoTemplate = mongoTemplate;
            this.promocodeRepo = promocodeRepo;
            this.cartService = cartService;
            this.userInfoService = userInfoService;
            this.orderRepository = orderRepository;
            this.userInfoRepository = userInfoRepository;
            this.productsService = productsService; // Injected ProductsService
        }
        @Override
        public String processOrder(ObjectId id) {
            // Retrieve the checkOut object by its ID
            checkOut checkOutObject = checkOutRepository.findById(id).orElse(null);

            if (checkOutObject != null) {
                // Check if the order has already been processed (you may need to add a flag in your checkOut class)
                if (!checkOutObject.isProcessed()) {
                    // Perform order processing logic here
                    // For example, update the order status, send confirmation emails, update product quantities, etc.

                    // Update the order as processed
                    checkOutObject.setProcessed(true);
                    checkOutRepository.save(checkOutObject);

            } else {
                // Handle the case where the order with the given ID was not found
                return ("Order with ID " + id + " not found.");
            }
        }
            return null;
        }
        @Override
        public List<checkOut> getAllCustomers() {
            // You can modify the query to match your specific criteria
            Query query = new Query();
            List<checkOut> checkOutList = mongoTemplate.find(query, checkOut.class);
            return checkOutList;
        }

        @Override
        public checkOut saveCheckout(checkOut checkOut, UserInfo userInfo) throws Exception {

//            checkOut.getUserInfo().setFirstName(userInfo.getFirstName());
//            checkOut.getUserInfo().setLastName(userInfo.getLastName());
//            checkOut.getUserInfo().setUsername(userInfo.getUsername());
//            checkOut.getUserInfo().setEmail(userInfo.getEmail());
//            checkOut.getUserInfo().setPhone(userInfo.getPhone());
//            checkOut.getUserInfo().setAddressLine1(userInfo.getAddressLine1());
//            checkOut.getUserInfo().setAddressLine2(userInfo.getAddressLine2());
//            checkOut.getUserInfo().setCountry(userInfo.getCountry());
//            checkOut.getUserInfo().setCity(userInfo.getCity());
//            checkOut.getUserInfo().setState(userInfo.getState());
//            checkOut.getUserInfo().setZipcode(userInfo.getZipcode());
            checkOut.setUserInfo(userInfo);

                // Set UserInfo in checkOut

                List<String> productNames = new ArrayList<>();
                List<OrderItem> cartItems = cartService.getCartItems();
                for (OrderItem item : cartItems) {
                    productNames.add(item.getProduct().getTitle());
                }

                checkOut.setProductNames(productNames);
                checkOut.setOrderdate(new Date()); // Set the order date

                int totalItems = cartItems.stream().mapToInt(OrderItem::getQuantity).sum();
                checkOut.setTotalItems(totalItems);

                double totalAmount = cartItems.stream()
                        .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                        .sum();
                checkOut.setTotalAmount(totalAmount);

                if (checkOut.getCoupon() != null) {
                    Coupon coupon = promocodeRepo.findById(checkOut.getCoupon().getId()).orElse(null);
                    checkOut.setCoupon(coupon);
                }

                // Now, save the checkOut object with associated UserInfo
                return checkOutRepository.save(checkOut);

        }

        private Double calculateDiscountedPrice(Double totalAmount, Double discountPercentage) {
            // Apply the discount percentage to the total amount
            Double discountAmount = (totalAmount * discountPercentage) / 100;
            Double newTotalSum = totalAmount - discountAmount;
            return newTotalSum;
        }



        @Override
        public checkOut getAllCustomers(ObjectId id) {
            Query query = new Query(Criteria.where("_id").is(id));
            checkOut checkOutObject = mongoTemplate.findOne(query, checkOut.class);
            return checkOutObject;
        }






    }


