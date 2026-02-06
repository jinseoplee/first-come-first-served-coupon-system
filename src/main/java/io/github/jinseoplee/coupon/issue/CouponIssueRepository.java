package io.github.jinseoplee.coupon.issue;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponIssueRepository extends JpaRepository<CouponIssue, Long> {

    boolean existsByCouponCampaign_IdAndUserId(Long campaignId, Long userId);
}
