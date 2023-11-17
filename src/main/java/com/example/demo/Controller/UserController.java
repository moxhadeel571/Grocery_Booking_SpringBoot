package com.example.demo.Controller;


import com.example.demo.Service.*;
import com.example.demo.Modal.*;
import com.example.demo.Repository.*;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;


@Controller
public class UserController {
    @Autowired
    private CartService cartService;

    private CouponService couponService;
    private OrderReturnService orderReturnService; // Fixed typo in the variable name
    private ShipmentService shipmentService;
    private ShipmentRepository shipmentRepository; // Renamed for consistency
    private checkOutService checkOutService; // Fixed typo in the variable name
    private ProductsService productsService;
    private UserInfoRepository userInfoRepository;
    private ProductsRepository productsRepository;
    private PromocodeRepo promocodeRepo; // Fixed typo in the variable name
    private OrderItemRepository cartRepository;
    private List<Products> savedProducts = new ArrayList<>(); // Initialize an empty list to store saved products
    private UserInfoService userInfoService;
    private EmailService emailService;

    @Autowired
    public UserController(
            CartService cartService,
            CouponService couponService,
            OrderReturnService orderReturnService,
            ShipmentService shipmentService,
            ShipmentRepository shipmentRepository,
            checkOutService checkOutService,
            ProductsService productsService,
            UserInfoRepository userInfoRepository,
            ProductsRepository productsRepository,
            PromocodeRepo promocodeRepo,
            OrderItemRepository cartRepository,
            UserInfoService userInfoService, EmailService emailService) {

        this.cartService = cartService;
        this.couponService = couponService;
        this.orderReturnService = orderReturnService;
        this.shipmentService = shipmentService;
        this.shipmentRepository = shipmentRepository;
        this.checkOutService = checkOutService;
        this.productsService = productsService;
        this.userInfoRepository = userInfoRepository;
        this.productsRepository = productsRepository;
        this.promocodeRepo = promocodeRepo;
        this.cartRepository = cartRepository;
        this.userInfoService = userInfoService;
        this.emailService = emailService;
    }

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private static final String grocery = "http://www.amazon.in/alm/category/?almBrandId=ctnow&node=4859496031&ref=fs_dsk_sn_fruitsVegetables";
    @GetMapping("/users/home")
    public String gethome() {
        return "home";
    }
    @GetMapping("/users/contact")
    public String getContact(){
        return "contact";
    }
    @GetMapping("/users/shop")
    public String getshop(Model model, ObjectId id) throws IOException {
        List<Products> AllList=productsRepository.findAll();
        model.addAttribute("products",AllList);
        List<Products> FruitsVegetablesList=productsRepository.findAllFruitsVegetablesList();
        model.addAttribute("products2",FruitsVegetablesList);
        List<Products> Foodgrains_Oil_Masala_egetables=productsRepository.findAllFoodgrains_Oil_Masala_egetabless();
        model.addAttribute("products3",Foodgrains_Oil_Masala_egetables);
        List<Products> Bevarages=productsRepository.findAllBevarages();
        model.addAttribute("products4",Bevarages);
        List<Products> EggsMeatFish=productsRepository.findAllEggsMeatFish();
        model.addAttribute("products5",EggsMeatFish);


        return "shop";
    }



    @GetMapping("/users/search")
    public String searchProduct(@RequestParam("query") String query, Model model) {
        try {
            Optional<Products> searchResults = productsService.findByTitle(query);

            // Pass the search results to the Thymeleaf template
            model.addAttribute("results", searchResults);

            return "searchresult"; // Return the Thymeleaf template
        } catch (Exception e) {
            // Handle exceptions and return an appropriate error response
            throw new RuntimeException("An error occurred while processing the request.", e);
        }
    }



    @GetMapping("/users/detail/{id}")
    public String getDetail(Model model, @PathVariable("id") ObjectId id) {
        Products product = productsRepository.findByProductId(id);
        model.addAttribute("data",product);
        List<Products> productsListItem = productsRepository.findAll();
        model.addAttribute("image", productsListItem);


        return "product-single"; // or some other error page
        }

    @PostMapping("/users/checkoutPayment/{id}")
    public String submitCheckoutForm(@ModelAttribute checkOut checkout,
                                     @ModelAttribute UserInfo userInfo,
                                     @ModelAttribute OrderItem orderItem,
                                     @PathVariable ObjectId id,
                                     @ModelAttribute("UpdateEmail") Email email,
                                     HttpServletResponse response) throws Exception {
        userInfoService.saveCheckout(id,userInfo,orderItem);
//emailService.order_email(email, id);
        // Save the checkout information
        checkOut savedCheckout = checkOutService.saveCheckout(checkout, userInfo);



        return "redirect:/users/shop";
    }




    @GetMapping("/users/products/{productId}/image/{imageIndex}")
public ResponseEntity<byte[]> getProductImage(
        @PathVariable String productId,
        @PathVariable int imageIndex) {
    Products product = productsService.getProductDetails(productId);

    List<Image> images = product.getImages();
    if (imageIndex < 0 || imageIndex >= images.size()) {
        return ResponseEntity.notFound().build();
    }

    Image image = images.get(imageIndex);

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.parseMediaType(image.getContentType()));

    return new ResponseEntity<>(image.getFileData(), headers, HttpStatus.OK);
}
    @GetMapping("/users/add_to_cart/{id}")
    public String addCart(Model model, @PathVariable("id") String id) {
        cartService.addItemToCart(id);
        List<OrderItem> cartItems = cartService.getCartItems();

        // Calculate total items and total amount
        int totalItems = cartItems.stream().mapToInt(OrderItem::getQuantity).sum();
        double totalAmount = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalItems", totalItems);

        // Store the total items count in a hidden input field
        model.addAttribute("hiddenTotalItems", totalItems);

        return "redirect:/users/shop";
    }
    @GetMapping("/users/cart/{id}")
    public String getCart(Model model, @PathVariable("id") String id) {
        cartService.addItemToCart(id);
        List<OrderItem> cartItems = cartService.getCartItems();

        // Calculate total items and total amount
        int totalItems = cartItems.stream().mapToInt(OrderItem::getQuantity).sum();
        double totalAmount = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalItems", totalItems);

        // Store the total items count in a hidden input field
        model.addAttribute("hiddenTotalItems", totalItems);

        return "cart";
    }

    @GetMapping("/users/checkout/{id}")
    public String getcheckout(Model model,
                              @ModelAttribute("checkout") checkOut checkout,
                              @ModelAttribute("user") UserInfo user,
                              @ModelAttribute("promo") Coupon coupon,
                              @ModelAttribute OrderItem order,
                              @ModelAttribute("UpdateEmail") Email email,
                              @RequestParam(name = "couponCode", required = false) String couponCode,
                              @PathVariable("id") ObjectId id) {

        List<OrderItem> cartItems = cartService.getCartItems();
        model.addAttribute("cartItems", cartItems);
model.addAttribute("id", id);
        // Calculate the new total sum after applying the discount
        Products amount = productsRepository.findByProductId(id);
        model.addAttribute("image", amount);
        double totalAmount = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        model.addAttribute("totalAmount", totalAmount);
        // Retrieve the coupon by coupon code
        Coupon coupons = couponService.getCouponByCouponCode(couponCode);

        if (coupons != null) {
            // Calculate the new total sum using the discountPercentage from the coupon
            Double newTotalSum = calculateDiscountedPrice(totalAmount, coupons.getDiscountPercentage());

            // Pass the list of cart items, new total sum, and coupon details to the view
            model.addAttribute("newTotalSum", newTotalSum);
            model.addAttribute("couponCode", coupons.getCouponCode());
            model.addAttribute("discountPercentage", coupons.getDiscountPercentage());
        } else {
            // Handle the case where the coupon is not found (e.g., display an error message)
            // You can customize this part to provide appropriate feedback to the user.
            // For example, you can set an error message in the model.
            model.addAttribute("couponNotFound", true);
        }

        List<String> indianStates = Arrays.asList(
                "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh",
                "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jharkhand",
                "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur",
                "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab",
                "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura",
                "Uttar Pradesh", "Uttarakhand", "West Bengal"
        );

        model.addAttribute("state", indianStates);

        return "checkout";
    }


    @PostMapping("/users/redeem-promo/{id}")
    public String redeemPromoCode(
            @ModelAttribute("checkout") checkOut checkout,
            @ModelAttribute("user") UserInfo user,
            Model model,
            @PathVariable("id") ObjectId id,
            @RequestParam(name = "couponCode", required = false) String couponCode) {

        // Get the cart items
        List<OrderItem> cartItems = cartService.getCartItems();
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("id", id);

        // Calculate the total cart amount before applying the discount
        Double totalAmount = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        // Retrieve the coupon by coupon code
        Coupon coupon = couponService.getCouponByCouponCode(couponCode);

        if (coupon != null) {
            // Calculate the discount amount based on the total cart amount and discount percentage
            Double discountAmount = totalAmount * (coupon.getDiscountPercentage() / 100);

            // Calculate the new total sum after applying the discount to each item
            Double newTotalSum = totalAmount - discountAmount;

            // Calculate the discount amount for each item and update the cart items
            for (OrderItem item : cartItems) {
                Double itemPrice = item.getProduct().getPrice();
                int itemQuantity = item.getQuantity();
                Double itemDiscount = (itemPrice / totalAmount) * discountAmount;
                Double newItemPrice = itemPrice - itemDiscount;

                // Update the item price in the cart
                item.getProduct().setPrice(newItemPrice);
            }

            // Pass the list of cart items, new total sum, and coupon details to the view
            model.addAttribute("newTotalSum", newTotalSum);
            model.addAttribute("couponCode", coupon.getCouponCode());
            model.addAttribute("discountPercentage", coupon.getDiscountPercentage());
        } else {
            // Handle the case where the coupon is not found (e.g., display an error message)
            // You can customize this part to provide appropriate feedback to the user.
            // For example, you can set an error message in the model.
            model.addAttribute("couponNotFound", true);
        }
        List<String> indianStates = Arrays.asList(
                "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh",
                "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jharkhand",
                "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur",
                "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab",
                "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura",
                "Uttar Pradesh", "Uttarakhand", "West Bengal"
        );

        model.addAttribute("state", indianStates);
        // Redirect to the checkout page with the updated total sum
        return "checkout";
    }
    @PostMapping("/users/redeem-promo2/{id}")
    public String redeemPromoCode2(
            @ModelAttribute("checkout") checkOut checkout,
            @ModelAttribute("user") UserInfo user,
            Model model,
            @PathVariable("id") ObjectId id,
            @RequestParam(name = "couponCode", required = false) String couponCode) {

        // Get the cart items
        List<OrderItem> cartItems = cartService.getCartItems();
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("id", id);

        // Calculate the total cart amount before applying the discount
        Double totalAmount = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
        // Retrieve the coupon by coupon code
        Coupon coupon = couponService.getCouponByCouponCode(couponCode);

        if (coupon != null) {
            // Calculate the discount amount based on the total cart amount and discount percentage
            Double discountAmount = totalAmount * (coupon.getDiscountPercentage() / 100);

            // Calculate the new total sum after applying the discount to each item
            Double newTotalSum = totalAmount - discountAmount;

            // Calculate the discount amount for each item and update the cart items
            // Calculate the discount amount for each item and update the cart items
            for (OrderItem item : cartItems) {
                Double itemPrice = item.getProduct().getPrice();
                model.addAttribute("total", totalAmount);

                int itemQuantity = item.getQuantity();
                model.addAttribute("itemQuantity", itemQuantity);

                Double itemDiscount = (itemPrice / totalAmount) * discountAmount;
                Double newItemPrice = itemPrice - itemDiscount;

                // Update the item price in the cart
                item.getProduct().setPrice(newItemPrice);
            }


            // Pass the list of cart items, new total sum, and coupon details to the view
            model.addAttribute("newTotalSum", newTotalSum);
            model.addAttribute("couponCode", coupon.getCouponCode());
            model.addAttribute("discountPercentage", coupon.getDiscountPercentage());
        } else {
            // Handle the case where the coupon is not found (e.g., display an error message)
            // You can customize this part to provide appropriate feedback to the user.
            // For example, you can set an error message in the model.
            model.addAttribute("couponNotFound", true);
        }
        List<String> indianStates = Arrays.asList(
                "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh",
                "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jharkhand",
                "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur",
                "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab",
                "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura",
                "Uttar Pradesh", "Uttarakhand", "West Bengal"
        );

        model.addAttribute("state", indianStates);
        // Redirect to the checkout page with the updated total sum
        return "cart";
    }



    private Double calculateDiscountedPrice(Double totalAmount, Double discountPercentage) {
        // Apply the discount percentage to the total amount
        Double discountAmount = (totalAmount * discountPercentage) / 100;
        Double newTotalSum = totalAmount - discountAmount;
        return newTotalSum;
    }




    @PostMapping("/users/create")
    public String createOrderReturn(MultipartFile[] files, OrderReturn orderReturn, Model model) {
        try {
            // Call the service to create the order return
            OrderReturn createdOrderReturn = orderReturnService.createOrderReturn(files, orderReturn);

            if (createdOrderReturn != null) {
                model.addAttribute("message", "Order Return Created Successfully");
            } else {
                model.addAttribute("message", "Failed to Create Order Return");
            }
        } catch (IOException e) {
            model.addAttribute("message", "Failed to Create Order Return: " + e.getMessage());
        }

        // Redirect to a relevant page or display a success/error message
        return "redirect:/users/dashboard"; // Replace with your desired view name or URL
    }

    @GetMapping("/users/dashboard/{id}")
    public String getdashboard(Model model,@PathVariable String id) {
        List<OrderItem> cartItems = cartService.getCartItems();
        model.addAttribute("cartItems", cartItems);
        int totalItems = cartItems.stream().mapToInt(OrderItem::getQuantity).sum();
        double totalAmount = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();


        model.addAttribute("totalAmount", totalAmount);
        List<checkOut> checkouts=  checkOutService.getAllCustomers();
        List<OrderReturn> orders= orderReturnService.getAllOrderReturns();
        List<OrderItem> cart=cartRepository.findAll();
        System.out.println("ID being searched: " + id);

        List<Shipment> details=shipmentService.getAllShipments();
        System.out.println("shipment details is" +details);
        model.addAttribute("cart",cart);
        model.addAttribute("detail",details);
        model.addAttribute("orders", orders);
        model.addAttribute("checkouts", checkouts);
        model.addAttribute("totalItems", totalItems);


        return "dashboard";
    }
@PostMapping("/users/CreateReturn/{id}")
public String createOrderReturn(@ModelAttribute OrderReturn orderReturn, @RequestParam("ReportImage") MultipartFile[] files) throws IOException {
    orderReturnService.createOrderReturn(files,orderReturn);
     return "redirect:/users/dashboard";
    }



    @GetMapping("/users/products/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable("id") String id) {
        try {
            Optional<Products> productsOptional = productsRepository.findById(id);

            if (productsOptional.isPresent()) {
                Products product = productsOptional.get();

                // Fetch the image data for the product
                byte[] fileData = product.getImages().get(0).getFileData(); // Assuming you want the first image

                // Set appropriate headers for the image response
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG); // Adjust the MediaType as needed
                headers.setContentLength(fileData.length);

                return new ResponseEntity<>(fileData, headers, HttpStatus.OK);
            } else {
                // Handle the case when the product with the given ID is not found
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            // Handle exceptions and return appropriate error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/users/order")
    public String getorder(Model model, String id)   {
        return "OrderTrack";
    }
    @GetMapping

    @PostMapping("/users/updateQuantity")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateQuantity(
            @RequestParam String productId,
            @RequestParam int change) {
        Map<String, Object> response = new HashMap<>();

        try {
            ObjectId objectId = new ObjectId(productId);
            Products product = productsService.getProductDetails(objectId);

            if (product != null) {
                int newQuantity = product.getQuantity() + change;
                if (newQuantity >= 1) {
                    product.setQuantity(newQuantity);
                    productsService.saveQuantity(product);

                    response.put("quantity", newQuantity);
                    response.put("price", product.getPrice());

                    List<Products> updatedProductsList = productsService.getProductDetails();
                    double totalPrice = calculateTotalPrice(updatedProductsList);
                    response.put("totalPrice", totalPrice);

                    return ResponseEntity.ok(response);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    private double calculateTotalPrice(List<Products> products) {
        double total = 0.0;
        for (Products product : products) {
            double itemTotal = product.getPrice() * product.getQuantity();
            total += itemTotal;
        }
        return total;
    }

    @PostMapping("/users/payment/success/{id}")
    public String showThankYouPage(@PathVariable("id") ObjectId    id, UserInfo userInfo,
                                   @ModelAttribute("UpdateEmail") Email email,
                                   OrderItem products
    ) throws MessagingException {
        userInfoService.saveCheckout(id,userInfo,products);
        emailService.order_email( email,id);
        return "thankyou";
    }


}

