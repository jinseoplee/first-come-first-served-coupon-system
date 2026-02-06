package io.github.jinseoplee.coupon.issue.exception;

public class CouponCampaignNotFoundException extends RuntimeException {

    public CouponCampaignNotFoundException(Long campaignId) {
        super("CAMPAIGN_NOT_FOUND: " + campaignId);
    }
}