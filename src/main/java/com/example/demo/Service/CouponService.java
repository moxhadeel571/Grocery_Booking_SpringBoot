package com.example.demo.Service;

import com.example.demo.Modal.Coupon;
import com.example.demo.Repository.PromocodeRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CouponService  {

    private final PromocodeRepo couponRepository;

    @Autowired
    public CouponService(PromocodeRepo couponRepository) {
        this.couponRepository = couponRepository;
    }

    public Coupon saveCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    public Optional<Coupon> getCouponById(ObjectId id) {
        return couponRepository.findById(id);
    }


    public void deleteCoupon(ObjectId id) {
        couponRepository.deleteById(id);
    }

    public Coupon generateCoupon(String couponCode, Double discountPercentage) {
        // Create a new Coupon instance
        Coupon coupon = new Coupon();
        coupon.setCouponCode(couponCode);
        coupon.setDiscountPercentage(discountPercentage);

        // Save the coupon using the DAO
        return couponRepository.save(coupon);
    }

    public Coupon getCouponByCouponCode(String couponCode) {
        return couponRepository.findByCouponCode(couponCode);
    }

    // You can add more methods as needed
}
