    package com.example.demo.Modal;

    import lombok.*;
    import org.bson.types.ObjectId;
    import org.springframework.data.mongodb.core.mapping.DBRef;
    import org.springframework.data.mongodb.core.mapping.Document;
    import org.springframework.data.mongodb.core.mapping.Field;
    import org.springframework.data.mongodb.core.mapping.MongoId;
    import org.springframework.format.annotation.DateTimeFormat;

    import java.util.Date;
    import java.util.List;
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Getter
    @Setter
    @Data
    @Document(collection = "products")
    public class Products extends OrderItem {

        @MongoId
        private ObjectId id;
        private String title;
        @Field("quantity")
        private Integer quantity;
        private List<String> category; // Representing a list of selected categories
        private String description;
        private String brand;
        private double price;
    private List<Image> images;
        @DBRef
        private Products product;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private Date orderDate; // For handling file uploads
    }
