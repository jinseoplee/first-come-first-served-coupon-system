package io.github.jinseoplee.coupon.issue;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponIssueRepository extends JpaRepository<CouponIssue, Long> {

    Optional<CouponIssue> findByCouponCampaign_IdAndUserId(Long campaignId, Long userId);
}
