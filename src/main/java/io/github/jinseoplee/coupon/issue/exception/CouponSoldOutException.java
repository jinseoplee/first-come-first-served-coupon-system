package io.github.jinseoplee.coupon.issue.exception;

public class CouponSoldOutException extends RuntimeException {

    public CouponSoldOutException() {
        super("SOLD_OUT");
    }
}