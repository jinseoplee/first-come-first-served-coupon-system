package io.github.jinseoplee.coupon.issue.exception;

public class CouponAlreadyIssuedException extends RuntimeException {

    public CouponAlreadyIssuedException() {
        super("ALREADY_ISSUED");
    }
}
