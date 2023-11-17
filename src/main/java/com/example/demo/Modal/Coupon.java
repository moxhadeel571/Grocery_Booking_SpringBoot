package com.example.demo.Modal;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@ToString
@Document(collection = "PromoCode")
public class Coupon extends Payment {
    private ObjectId id;
    private String couponCode;
    private Double  discountPercentage;
    private Double  discountAmount;
    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

}
