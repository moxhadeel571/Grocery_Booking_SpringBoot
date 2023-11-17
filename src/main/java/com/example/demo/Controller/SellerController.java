    package com.example.demo.Controller;

    import com.example.demo.Modal.*;
    import com.example.demo.Service.*;
    import com.example.demo.Repository.*;
    import com.example.demo.Service.*;
    import org.bson.types.ObjectId;
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
    import java.util.ArrayList;
    import java.util.List;

    @Controller
    public class SellerController {
        private final CouponService couponService;
        private OrderReturnService orderReturnService;
        private OrderReturnRepository orderReturnRepository;
        private checkOutService checkOutService;
        @Autowired
        private CartService cartService;
    private  ProductsService productsService;
        private final ShipmentService shipmentService;
        @Autowired
    private ProductsRepository productsRepository;
        public SellerController(CouponService couponService, OrderReturnRepository orderReturnRepository, checkOutService checkOutService, ProductsService productsService, ShipmentService shipmentService) {
            this.couponService = couponService;
            this.orderReturnService = orderReturnService;
            this.orderReturnRepository = orderReturnRepository;
            this.productsService = productsService;
            this.shipmentService = shipmentService;
        }


        @GetMapping("/seller/coupon-form")
        public String showCouponForm(Model model) {
            model.addAttribute("coupon", new Coupon());
            return "redirect:/seller/dashboard"; // Return the name of the HTML template for the coupon form
        }
        @PostMapping("/seller/create-shipment")
        public String createShipment(@ModelAttribute("shipment") Shipment shipment) {
            shipmentService.createShipment(shipment);
            return "redirect:/seller/dashboard"; // Redirect to the seller dashboard or another page
        }
        @PostMapping(value = "/users/process/{id}")
        public String processOrder(@PathVariable("id") ObjectId id, Model model) throws Exception {
            checkOut checkOutObject = checkOutService.getAllCustomers(id);
            checkOutObject.setProcessed(true);
            model.addAttribute("id",id);

            // Assuming you can obtain the associated userInfo object
            UserInfo userInfo = checkOutObject.getUserInfo();

            // Pass both checkOut and userInfo to the saveCheckout method
            checkOutService.saveCheckout(checkOutObject, userInfo);

            return "redirect:/seller/dashboard"; // Redirect to the seller dashboard or another page

        }


        @PostMapping("/seller/generate-coupon")
        public String generateCoupon(@ModelAttribute("coupon") Coupon coupon) {
            couponService.generateCoupon(coupon.getCouponCode(), Double.valueOf(coupon.getDiscountPercentage()));
            return "redirect:/seller/dashboard";// Redirect to a list of coupons or another page
        }

        @GetMapping("/seller/coupon-list")
        public String listCoupons(Model model) {
            List<Coupon> coupons = couponService.getAllCoupons();
            model.addAttribute("coupons", coupons);
            return "redirect:/seller/dashboard"; // Return the name of the HTML template for the coupon list
        }

        @GetMapping("/seller/dashboard")
        public String getDashboard(Model model, @ModelAttribute("product") Products products,
                                   @ModelAttribute("coupon") Coupon coupon,
                                   @ModelAttribute("shipment") Shipment shipment

        ) {
//            List<OrderReturn> orders = orderReturnService.getAllOrderReturns();
            List<Products> productsListItem = productsService.getProductDetails();
//            List<checkOut> cartItems = checkOutService.getAllCustomers();
//            model.addAttribute("order",orders);
//            model.addAttribute("cart" ,cartItems);
            model.addAttribute("info", productsListItem);
            return "sellerDashboard";
        }

        @GetMapping("/seller/order-returns/approve/{id}")
        public String approveOrderReturn(@PathVariable String id, Model model) {
            OrderReturn updatedOrderReturn = orderReturnService.approveOrderReturn(id);
            model.addAttribute("id",id);

            if (updatedOrderReturn != null) {
                model.addAttribute("message", "Order Return Approved Successfully");
            } else {
                model.addAttribute("message", "Failed to Approve Order Return");
            }

            // Redirect to a relevant page or display a success/error message
            return "redirect:/seller/dashboard"; // Replace with your desired view name or URL
        }
        @GetMapping("/seller/return/{returnId}/image/{imageIndex}")
        public ResponseEntity<byte[]> getProductImage(
                @PathVariable String returnId,
                @PathVariable int imageIndex) {
            OrderReturn imagesreturn = orderReturnService.getOrderReturnById(returnId);

            List<OrderImage> images = imagesreturn.getOrderimages();
            if (imageIndex < 0 || imageIndex >= images.size()) {
                return ResponseEntity.notFound().build();
            }

            OrderImage image = images.get(imageIndex);

            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(image.getContentType()));

            return new ResponseEntity<>(image.getFileData(), headers, HttpStatus.OK);
        }

        @GetMapping("/seller/order-returns/reject/{id}")
        public String rejectOrderReturn(@PathVariable String id, Model model) {
            OrderReturn updatedOrderReturn = orderReturnService.rejectOrderReturn(id);

            if (updatedOrderReturn != null) {
                model.addAttribute("message", "Order Return Rejected Successfully");
            } else {
                model.addAttribute("message", "Failed to Reject Order Return");
            }

            // Redirect to a relevant page or display a success/error message
            return "redirect:/seller/dashboard"; // Replace with your desired view name or URL
        }
        @PostMapping("/seller/updateQuantity")
        public ResponseEntity<String> updateQuantity(@RequestParam String productId, @RequestParam int newQuantity) {
            try {
                ObjectId objectId = new ObjectId(productId);
                Products product = productsService.getProductDetails(objectId);

                if (product != null) {
                    product.setQuantity(newQuantity);
                    productsService.saveQuantity(product);

                    return ResponseEntity.ok("Quantity updated successfully");
                } else {
                    return ResponseEntity.badRequest().body("Product not found");
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
            }
        }
        @PostMapping("/seller/saveForm")
        public String saveForm(@RequestParam("files") MultipartFile[] files, Model model,@ModelAttribute Products products) throws IOException {
            List<Products> productsListItem=  productsService.getProductDetails();
            model.addAttribute("info",productsListItem);
              productsService.saveFormWithImages(files, products);
            return "redirect:/seller/dashboard";
        }


        @GetMapping("/seller/dashboard/plus/{product-id}")
        @ResponseBody
        public ResponseEntity<Integer> increaseQuantity(@PathVariable("product-id") ObjectId id) throws IOException {
            Products products = productsRepository.findById(id.toString()).orElse(null);
            if (products != null) {
                int currentQuantity = products.getQuantity();
                products.setQuantity(currentQuantity + 1);
                productsService.saveQuantity(products);
                return ResponseEntity.ok(currentQuantity + 1);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }


        @GetMapping("/seller/dashboard/minus/{product-id}")
        @ResponseBody
        public ResponseEntity<String> decreaseQuantity(@PathVariable("product-id") ObjectId id) throws IOException {
            Products products = productsRepository.findById(id.toString()).orElse(null);
            if (products != null) {
                int currentQuantity = products.getQuantity();
                if (currentQuantity > 1) {
                    products.setQuantity(currentQuantity - 1);
                    productsService.saveQuantity(products);
                    return ResponseEntity.ok("Quantity decreased successfully");
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Quantity cannot go below 1");
                }
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }

        @GetMapping("/seller/dashboard/delete/{id}")
        public String deleteUser(@PathVariable ObjectId id, Model model) {
            productsService.deleteItem(id);
            List<Products> productsListItem = productsService.getProductDetails();
            model.addAttribute("info", productsListItem);
            return "redirect:/seller/dashboard";
        }


        @GetMapping("/seller/edit/{id}")
        public String editProduct(@PathVariable("id") ObjectId id, Model model) {
            Products product = productsRepository.findById(id.toString()).orElse(null);
            if (product != null) {
                model.addAttribute("product", product);
                return "editProduct"; // Return the name of the edit product view
            } else {
                // Handle product not found case
                return "redirect:/seller/dashboard";
            }
        }

        @PostMapping("/seller/update/{id}")
        public String updateProduct(@PathVariable("id") ObjectId id, @RequestParam("files") MultipartFile[] files, @ModelAttribute Products updatedProduct, Model model) throws IOException {
            Products existingProduct = productsRepository.findById(id.toString()).orElse(null);
            if (existingProduct != null) {
                // Update existingProduct with fields from updatedProduct
                existingProduct.setTitle(updatedProduct.getTitle());
                existingProduct.setCategory(updatedProduct.getCategory());
                existingProduct.setDescription(updatedProduct.getDescription());
                // ... other fields ...

                // Update images if necessary
                // Update images if necessaryde
                if (files != null && files.length > 0) {
                    List<Image> images = new ArrayList<>();
                }

                // Save the updated product
                productsRepository.save(existingProduct);
            }

            return "redirect:/seller/dashboard"; // Redirect back to the dashboard



        }
        // Handle updating shipment status
        @PostMapping("/seller/update-shipment-status/{shipmentId}")
        public String updateShipmentStatus(@PathVariable String shipmentId, @RequestParam("NewStatus") String NewStatus) {
            // Update the shipment status using the shipmentService for the "status" parameter
            shipmentService.updateShipmentStatus(shipmentId, NewStatus);

            // Redirect back to the seller dashboard or wherever you want
            return "redirect:/seller/dashboard";
        }




    }

